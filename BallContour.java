 package ffd;

 import java.awt.Graphics;  
 import java.awt.image.BufferedImage;  
 import java.util.ArrayList;  
 import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;  
 import javax.swing.JPanel;  
 import org.opencv.core.Core;  
 import org.opencv.core.Mat;
 import org.opencv.core.MatOfPoint;
 import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;  
 import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc; 
import org.opencv.imgproc.*;
 import org.opencv.core.CvType; 
 
 class MyPanel extends JPanel{  
   private static final long serialVersionUID = 1L;  
   private BufferedImage image;    
   // Create a constructor method  
   public MyPanel(){  
     super();  
   }  
   private BufferedImage getimage(){  
     return image;  
   }  
   public void setimage(BufferedImage newimage){  
     image=newimage;  
     return;  
   }  
   public void setimagewithMat(Mat newimage){  
     image=this.matToBufferedImage(newimage);  
     return;  
   }  
   /**  
    * Converts/writes a Mat into a BufferedImage.  
    *  
    * @param matrix Mat of type CV_8UC3 or CV_8UC1  
    * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
    */  
   public BufferedImage matToBufferedImage(Mat matrix) {  
     int cols = matrix.cols();  
     int rows = matrix.rows();  
     int elemSize = (int)matrix.elemSize();  
     byte[] data = new byte[cols * rows * elemSize];  
     int type;  
     matrix.get(0, 0, data);  
     switch (matrix.channels()) {  
       case 1:  
         type = BufferedImage.TYPE_BYTE_GRAY;  
         break;  
       case 3:  
         type = BufferedImage.TYPE_3BYTE_BGR;  
         // bgr to rgb  
         byte b;  
         for(int i=0; i<data.length; i=i+3) {  
           b = data[i];  
           data[i] = data[i+2];  
           data[i+2] = b;  
         }  
         break;  
       default:  
         return null;  
     }  
     BufferedImage image2 = new BufferedImage(cols, rows, type);  
     image2.getRaster().setDataElements(0, 0, cols, rows, data);  
     return image2;  
   }  
   @Override  
   protected void paintComponent(Graphics g){  
      super.paintComponent(g);  
      BufferedImage temp=getimage();  
      if( temp != null)
        g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);  
   }  
 }  
  
 public class BallContour {  
   public static void main(String arg[]){   
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
     JFrame frame1 = new JFrame("Camera");  
     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     frame1.setSize(800,640);  
     //frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
     MyPanel panel1 = new MyPanel();  
     panel1.setSize(200,200);
     frame1.setContentPane(panel1);  
     frame1.setVisible(true);  
     
     //-- 2. Read the video stream  
     VideoCapture cam = new VideoCapture(0);  
     
     Mat webcam_image=new Mat();  
     Mat hsv_image=new Mat();  
     Mat upper_threshold = new Mat();
     Mat circles = new Mat();
     
      cam.read(webcam_image);  
      panel1.setSize(webcam_image.width(),webcam_image.height());  
   
    Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
     array255.setTo(new Scalar(255));  
   
    Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
    List<Mat> lhsv = new ArrayList<Mat>(3);      

   // Baseline coordinates
  	int x1 = 200;
  	int y1 = panel1.getHeight() - 50;
  	int x2 = panel1.getWidth();
  	int y2 = y1;
  	
  	// Draw baseline on screen
  	Baseline bs = new Baseline(webcam_image, x1, y1, x2, y2);	
  	/*
  	System.out.println(bs.get_x1());
  	System.out.println(bs.get_y1());
  	System.out.println(bs.get_x2());
  	System.out.println(bs.get_y2());
     */
  	
     // Open camera
     if(cam.isOpened())  
     {  
      while( true )  
      {  
        cam.read(webcam_image);  
        if( !webcam_image.empty() )  
         { 
          	bs.drawBaseline();
        	
          // Detect object by color and shape
          Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);     
         Core.inRange(hsv_image, new Scalar(30,100,100), new Scalar(60,255,255), upper_threshold);
          
          Mat green_image = new Mat();
          Core.addWeighted(upper_threshold, 1.0, upper_threshold, 1.0, 0.0, green_image);
          Imgproc.GaussianBlur(green_image, green_image, new Size(9, 9), 2, 10);
          Imgproc.HoughCircles(green_image, circles, Imgproc.CV_HOUGH_GRADIENT, 2, green_image.height(), 100, 20, 0, 0); 

           int cols = circles.cols(); 
           int rows = circles.rows();  
           int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float) 
           float[] data = new float[rows * elemSize/4];
           if (data.length>0){  
             circles.get(0, 0, data); // Points to the first element and reads the whole thing into data2  0,0 is location is circle matrix
             for(int i = 0; i < data.length; i = i+3) {  
               float p1 = data[i];
               float p2 = data[i+1];
               Point center = new Point(p1, p2);  
               double px = (double)data[i+2];
               double py = (double)data[i+2];
               Size size = new Size(px, py);
            // Determine if object has intersected baseline
               if (center.x >= bs.get_x1() && center.x <= bs.get_x2() && center.y >= bs.get_y1()){
            	   System.out.println("Intersected");
               }
              // Imgproc.ellipse(webcam_image,center,size, 0, 0, 360, new Scalar(255,0,255), 4, 8, 0 ); 
               
               
               // Find contours of tennis ball
               // http://opencv-java-tutorials.readthedocs.io/en/latest/08-object-detection.html
               List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
               Mat hierarchy = new Mat();
               Imgproc.findContours(green_image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
               Scalar color = new Scalar(255,0,255);
               bs.drawBaseline();
              // Imgproc.line(webcam_image, new Point(x1,y1), new Point(x2,y2), new Scalar(255,255,255),10);
               if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
            	   for (int idx = 0; idx >= 0; idx = (int)hierarchy.get(0, idx)[0]) {
            		   Imgproc.drawContours(webcam_image, contours, idx, color, 5);
            	   }
               }
             } 
           }  
          panel1.setimagewithMat(webcam_image);  
          panel1.repaint();
         }  
         else  
         {  
           System.out.println("No camera frame detected.");  
           break;  
         }  
        } 
       }  
     return;  
   }  
 }   

