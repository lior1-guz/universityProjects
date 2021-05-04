package images;
 

public class Circle extends BaseImage {

	private int centerX,centerY,radius;
	private RGB center,outside;
	public Circle(int width, int height, int centerX, int centerY, int radius, RGB center, RGB outside) {
		super(width,height);
		this.centerX=centerX;
		this.centerY=centerY;
		this.radius=radius;
		this.center=center;
		this.outside=outside;
	}
	
	public Circle(int size, int radius, RGB center, RGB outside) {
		super(size,size);
		centerX=(int)(size/2);
		centerY=(int)(size/2);
		this.radius=radius;
		this.center=center;
		this.outside=outside;
	}
	
	public RGB get(int x, int y) {
		if(lenOfHypotenuse(x,y)/(double)radius<=1)
			return RGB.mix(outside, center,lenOfHypotenuse(x,y)/(double)radius);
		return RGB.mix(outside, center,1);//if the dot is outside of the circle
	}
	
	public double lenOfHypotenuse(int x,int y)//calculate distance between 2 points
	{
		double length = Math.sqrt(Math.pow(x-centerX,2)+Math.pow(y-centerY, 2));
		return length;
	}

}
