package ffd;

 import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;  
 import java.util.ArrayList;  
 import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
 
 
 // CITE SOURCE HERE
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
  
 public class Program2 {  
	 
	 static boolean startButtonPressed = false;
	 static boolean stopButtonPressed = true;
	 
		static long startTimeCam1;
		static long stopTimeCam1;
		static long finalTimeCam1;
		
		static long startTimeCam2;
		static long stopTimeCam2;
		static long finalTimeCam2;
	 
   public static void main(String arg[]){   
	   
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
    int frameWidth = 1300;
    int frameHeight = 1000;
     
     JFrame frame1 = new JFrame("Foot-Fault Detection System"); 
     
     Container c = frame1.getContentPane();
     c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
  
     JPanel cameraPanel = new JPanel();
     MyPanel camPanel = new MyPanel();
     MyPanel cam2Panel = new MyPanel();
    // JPanel bufferPanel = new JPanel();
     JPanel timePanel = new JPanel();
     JPanel startStopPanel = new JPanel();
     JPanel buttonPanel = new JPanel();
     JPanel buttonStatusPanel = new JPanel();
     
     
     cameraPanel.setLayout(new FlowLayout());
     
     //bufferPanel.setSize(new Dimension(1000,20));
     //bufferPanel.setLocation(1000, webcam_image.height() + 100);
     
     timePanel.setPreferredSize(new Dimension(frameWidth,50));
     timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
     timePanel.setBorder(new EmptyBorder(20,20,20,20));
     timePanel.setBorder(new LineBorder(Color.BLACK, 10));
     timePanel.setBackground(Color.WHITE);
     //timePanel.setLocation(1000,1000);
     
     JPanel footBallPanel = new JPanel();
     footBallPanel.setLayout(new BoxLayout(footBallPanel, BoxLayout.X_AXIS));
     footBallPanel.setBackground(Color.WHITE);
     
     JPanel footTimePanel = new JPanel();
     footTimePanel.setLayout(new BoxLayout(footTimePanel, BoxLayout.Y_AXIS));
     footTimePanel.setBackground(Color.WHITE);
     JLabel footTimeLabel = new JLabel("Foot to Court Time", JLabel.CENTER);
     //footTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
     footTimeLabel.setFont(new Font("Arial",Font.BOLD, 32));
     footTimeLabel.setBackground(Color.WHITE);
     footTimeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
     //footTimeLabel.setPreferredSize(new Dimension(50,50));
     //footTimeLabel.setBorder(new LineBorder(Color.BLACK, 5));
     JLabel footTime = new JLabel ("-");
    // footTime.setHorizontalAlignment(SwingConstants.CENTER);
     footTime.setFont(new Font("Arial",Font.BOLD, 32));
     footTime.setBackground(Color.WHITE);
     //footTime.setBorder(new LineBorder(Color.BLACK, 5));
     footTimePanel.add(footTimeLabel);
     footTimePanel.add(footTime);
     footTimePanel.setBorder(new EmptyBorder(50,50,50,50));
     
     JPanel ballTimePanel = new JPanel();
     ballTimePanel.setLayout(new BoxLayout(ballTimePanel, BoxLayout.Y_AXIS));
     ballTimePanel.setBackground(Color.WHITE);
     JLabel ballTimeLabel = new JLabel("Racquet to Ball Time", JLabel.CENTER);
     ballTimeLabel.setBackground(Color.WHITE);
     ballTimeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
     //ballTimeLabel.setBorder(new LineBorder(Color.BLACK, 5));
     ballTimeLabel.setFont(new Font("Arial",Font.BOLD, 32));
     JLabel ballTime = new JLabel("-");
     //ballTime.setBorder(new LineBorder(Color.BLACK, 5));
     ballTime.setFont(new Font("Arial",Font.BOLD, 32));
     ballTime.setBackground(Color.WHITE);
     ballTimePanel.add(ballTimeLabel);
     ballTimePanel.add(ballTime);
    // ballTimePanel.setBorder(new EmptyBorder(50,50,50,50));
     
     JPanel faultStatusPanel = new JPanel();
     faultStatusPanel.setBackground(Color.WHITE);
     faultStatusPanel.setLayout(new FlowLayout());
     JLabel statusLabel = new JLabel("Status:");
     statusLabel.setBackground(Color.WHITE);
     statusLabel.setFont(new Font("Arial",Font.BOLD, 32));
     
     JLabel status = new JLabel("-");
     status.setFont(new Font("Arial",Font.BOLD,32));
     
     faultStatusPanel.add(statusLabel);
     faultStatusPanel.add(status);
     //faultStatusPanel.setBorder(new EmptyBorder(40,40,40,40));
    
     footBallPanel.add(footTimePanel);
     footBallPanel.add(ballTimePanel);
     timePanel.add(footBallPanel);
     timePanel.add(faultStatusPanel);
     //timePanel.setBorder(new EmptyBorder(50,50,50,50));
     
     JButton start = new JButton();
     start.setPreferredSize(new Dimension(200,100));
     start.setBorder(new LineBorder(Color.GREEN, 10));
     
     JButton stop = new JButton();
     stop.setPreferredSize(new Dimension(200,100));
     stop.setBorder(new LineBorder(Color.RED, 10));
     
     JLabel startLabel = new JLabel("Start", JLabel.CENTER);
     startLabel.setFont(new Font("Arial", Font.BOLD, 32));
     startLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
     
     JLabel stopLabel = new JLabel("Stop", JLabel.CENTER);
     stopLabel.setFont(new Font("Arial", Font.BOLD, 32));
     stopLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
     JLabel startStopStatus = new JLabel();
     
     start.add(startLabel);
     stop.add(stopLabel);
     
     buttonPanel.setLayout(new FlowLayout());
     buttonPanel.add(start);
     buttonPanel.add(stop);
     
     buttonStatusPanel.setLayout(new BoxLayout(buttonStatusPanel, BoxLayout.X_AXIS));
     JLabel buttonStatusLabel = new JLabel("STOPPED");
     buttonStatusLabel.setFont(new Font("Arial", Font.BOLD, 32));
     buttonStatusLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
     buttonStatusPanel.add(buttonStatusLabel);
     
     startStopPanel.setLayout(new BoxLayout(startStopPanel, BoxLayout.Y_AXIS));
     startStopPanel.setBorder(new EmptyBorder(50,50,50,50));
     startStopPanel.setPreferredSize(new Dimension(frameWidth,0));
     startStopPanel.add(buttonPanel);
     startStopPanel.add(buttonStatusPanel);
     
     cameraPanel.add(camPanel);
     cameraPanel.add(cam2Panel);
     
   //-- 2. Read the video stream  
     VideoCapture cam = new VideoCapture(0);  
     VideoCapture cam2 = new VideoCapture(0);
     
     Mat webcam_image=new Mat();  
     Mat webcam_image2=new Mat();
     Mat hsv_image=new Mat();  
     Mat upper_threshold = new Mat();
     Mat circles = new Mat();
     Mat green_image = new Mat();
     
     cam.read(webcam_image);  
     cam2.read(webcam_image2);
     
     camPanel.setSize(webcam_image.width(), webcam_image.height()); 
     //cameraPanel.setBorder(new LineBorder(Color.BLACK, 10));
     
     frame1.add(cameraPanel);
     //frame1.add(bufferPanel);
     frame1.add(timePanel);
     frame1.add(startStopPanel);
     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     frame1.setSize(frameWidth,frameHeight);  

     frame1.setVisible(true); 
   
    Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
     array255.setTo(new Scalar(255));  
   
    Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
    List<Mat> lhsv = new ArrayList<Mat>(3);      
     
   // Baseline coordinates
  	int x1 = 1000;
  	int y1 = camPanel.getHeight() + 250;
  	int x2 = camPanel.getWidth() + 150;
  	int y2 = y1;
  	
  	// -- Draw baseline on screen
  	Baseline bs = new Baseline(webcam_image, x1, y1, x2, y2);	
  	
  	 
  	
  	// -- Open camera
    if(cam.isOpened())  
    {  
     while(true)  
     {  
       cam.read(webcam_image);  
       cam2.read(webcam_image2);
       if( !webcam_image.empty() )  
        { 
         bs.drawBaseline();
       
     
               // -- Determine if object has intersected baseline
             //} 
          // }
         
         start.addActionListener(new ActionListener(){
        	 public void actionPerformed(ActionEvent e) {
        		 
        		 startTimeCam1 = System.currentTimeMillis();
        		 startTimeCam2 = System.currentTimeMillis();
        		 
        		// while (stopButtonPressed == true) {
        			 
        			// Find contours of tennis ball
        	         // http://opencv-java-tutorials.readthedocs.io/en/latest/08-object-detection.html
        	         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        	         Mat hierarchy = new Mat();
        	         Imgproc.findContours(green_image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        	         Scalar color = new Scalar(255,0,255);
        	         //bs.drawBaseline();
        	        // Imgproc.line(webcam_image, new Point(x1,y1), new Point(x2,y2), new Scalar(255,255,255),10);
        	         if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
        	      	   for (int idx = 0; idx >= 0; idx = (int)hierarchy.get(0, idx)[0]) {
        	      		   Imgproc.drawContours(webcam_image, contours, idx, color, 5);
        	      	   }
        	         }  
        	         
	        		 // -- Detect object by color and shape
	     		    Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);     
	     		    Core.inRange(hsv_image, new Scalar(30,100,100), new Scalar(60,255,255), upper_threshold);
	     		    
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
	                     //Size size = new Size(px, py);
		        		 if (center.x >= bs.get_x2() && center.y >= bs.get_y1()){
		           	      System.out.println("Intersected"); 
		           	      stopTimeCam1 = System.currentTimeMillis();
		           	      finalTimeCam1 = (stopTimeCam1 - startTimeCam1);
		           	      String finalTimeString = Long.toString(finalTimeCam1);
		           	      footTime.setText(finalTimeString + " ms");
		           	      
		           	      stopTimeCam2 = System.currentTimeMillis();
		           	      finalTimeCam2 = (stopTimeCam2 - startTimeCam2);
		           	      String finalTimeString2 = Long.toString(finalTimeCam2);
		           	      ballTime.setText(finalTimeString2 + " ms");
		           	      
		           	      if (finalTimeCam1 >= finalTimeCam2) {
		           	    	  status.setText("FOOT FAULT");
		           	    	  status.setForeground(Color.RED);
		           	      }
		           	      else {
		           	    	  status.setText("VALID");
		           	    	  status.setForeground(Color.GREEN);
		           	      }
		           	      stopButtonPressed = false;
		        		 }
		        		 else {
		        			 camPanel.setimagewithMat(webcam_image);  
			        	     camPanel.repaint();
			        	     camPanel.setSize(new Dimension(webcam_image.width(), webcam_image.height()));
			        	     camPanel.setLocation(0, 0);
		        		 }
	                   }
	                // }
        		 }
        	   }
        	});
         camPanel.setimagewithMat(webcam_image);  
	     camPanel.repaint();
	     camPanel.setSize(new Dimension(webcam_image.width(), webcam_image.height()));
	     camPanel.setLocation(0, 0);
	     // Imgproc.ellipse(webcam_image,center,size, 0, 0, 360, new Scalar(255,0,255), 4, 8, 0 ); 
         
         cam2Panel.setimagewithMat(webcam_image2);  
         cam2Panel.repaint();
         cam2Panel.setSize(new Dimension(webcam_image2.width(), webcam_image2.height()));
         cam2Panel.setLocation(webcam_image2.width(), 0);
         
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
 
 
 

 

