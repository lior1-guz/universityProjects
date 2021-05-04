package images;

public class RGB {
	private double red,green,blue;
	
	public static final RGB BLACK = new RGB(0);
	public static final RGB WHITE = new RGB(1);
	public static final RGB RED = new RGB(1,0,0);
	public static final RGB GREEN = new RGB(0,1,0);
	public static final RGB BLUE = new RGB(0,0,1);

	public RGB(double red, double green, double blue) {
		this.red=red;
		this.green=green;
		this.blue=blue;
	}
	public RGB(double grey) {
		red=grey;
		green=grey;
		blue=grey;
	}
	public double getRed() {
		return red;
	}
	public double getBlue() {
		return blue;
	}
	public double getGreen() {
		return green;
	}
	public RGB invert() {
		return new RGB(1-red,1-green,1-blue);
	}
	public RGB filter(RGB filter) {
		return new RGB(red*filter.red,green*filter.green,blue*filter.blue);
	}
	public static RGB superpose(RGB rgb1, RGB rgb2) {
		
		double newGreen=0.0,newRed=0.0,newBlue=0.0;
		if(rgb1.red+rgb2.red>1){
			newRed=1.0;
		}
		else {
			newRed=rgb1.red+rgb2.red;
		}
		if(rgb1.green+rgb2.green>1){
			newGreen=1.0;
		}
		else {
			newGreen=rgb1.green+rgb2.green;
		}
		if(rgb1.blue+rgb2.blue>1){
			newBlue=1.0;
		}
		else {
			newBlue=rgb1.blue+rgb2.blue;
		}
		return new RGB(newRed,newGreen,newBlue);
	}
	public static RGB mix(RGB rgb1, RGB rgb2, double alpha) {
		double newGreen=0.0,newRed=0.0,newBlue=0.0;
		newGreen=alpha*rgb1.green +(1-alpha)*rgb2.green;
		newRed=alpha*rgb1.red +(1-alpha)*rgb2.red;
		newBlue=alpha*rgb1.blue +(1-alpha)*rgb2.blue;
		//check if the value is in the right limits
		if(newGreen>1)
			newGreen=1;
		if(newRed>1)
			newRed=1;
		if(newBlue>1)
			newBlue=1;
		if(newGreen<0)
			newGreen=0;
		if(newRed<0)
			newRed=0;
		if(newBlue<0)
			newBlue=0;
		return new RGB(newRed,newGreen,newBlue);
	}

	public String toString() {
	 String str= String.format("<%.4f, %.4f, %.4f>",red,green,blue);
	 return str;
	}
}
