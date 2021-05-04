package images;

public class Superpose extends BinaryImageDecorator{
	
	public Superpose(Image base1, Image base2) {
		super(base1,base2);
	}
	
	@Override
	public RGB get(int x, int y) {
		if(x<=base1.getWidth() && y<=base1.getHeight() && x<=base2.getWidth() && y<=base2.getHeight())
		{
			return RGB.superpose(base1.get(x,y),base2.get(x,y));
		}
		if(!(x<=base1.getWidth() && y<=base1.getHeight()) && (x<=base2.getWidth() && y<=base2.getHeight()))
		{
			return base2.get(x, y);
		}
		if(x<=base1.getWidth() && y<=base1.getHeight() && !(x<=base2.getWidth() && y<=base2.getHeight()))
		{
			return base1.get(x, y);
		}
		else return RGB.BLACK;
	}
}
