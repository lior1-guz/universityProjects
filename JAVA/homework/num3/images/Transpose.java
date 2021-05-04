package images;

public class Transpose extends ImageDecorator{
	
	public Transpose(Image base) {
		super(base);
	}
	
	public  RGB get(int x, int y) {
		return base.get(y,x);
	}
	
	//need to override because of the transposition
	@Override
	public int getWidth() {
		return base.getHeight();
	}
	
	@Override
	public int getHeight() {
		return base.getWidth();
	}
	

}
