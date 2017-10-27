package ffd;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

class Baseline {
	 
	 private Mat src;			// image source matrix
	 private Point p1;			// starting point
	 private Point p2;			// ending point
	 private Scalar s;			// color
	 private int t;				// thickness
	 private int x1,y1,x2,y2; 	// coordinate points
	 
	 public Baseline(Mat image, int x_1, int y_1, int x_2, int y_2) {
		 
		 src = image;
		 x1 = x_1;
		 y1 = y_1;
		 x2 = x_2;
		 y2 = y_2;
		 p1 = new Point(x_1,y_1);
		 p2 = new Point(x_2,y_2);
		 s = new Scalar(255,255,255);
		 t = 10;
	 }
	 
	public void drawBaseline() {
		Imgproc.line(src, p1, p2, s, t);
	}
	 
	 public Mat get_src() {
		 return src;
	 }
	 
	 public Point get_p1() {
		 return p1;
	 }
	 
	 public Point get_p2() {
		 return p2;
	 }
	 
	 public Scalar get_s() {
		 return s;
	 }
	 
	 public int get_t() {
		 return t;
	 }
	 
	 public int get_x1() {
		 return x1;
	 }
	 
	 public int get_y1() {
		 return y1;
	 }
	 
	 public int get_x2() {
		 return x2;
	 }
	 
	 public int get_y2() {
		 return y2;
	 }
 }
