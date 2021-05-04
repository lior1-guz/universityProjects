package images;

public class Mix extends BinaryImageDecorator {
	private double alpha;
	
	public Mix(Image base1, Image base2, double alpha) {
		super(base1,base2);
		this.alpha=alpha;
	}
	
	@Override
	public RGB get(int x, int y) {
		if(x<=base1.getWidth() && y<=base1.getHeight() && x<=base2.getWidth() && y<=base2.getHeight())
		{
			return RGB.mix(base1.get(x,y),base2.get(x,y),alpha);
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
