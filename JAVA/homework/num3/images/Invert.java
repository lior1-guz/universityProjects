package images;

public class Invert extends ImageDecorator {

	public Invert(Image base){
		super(base);
	}
	
	public RGB get(int x, int y) {
		return base.get(x, y).invert();
	}
}
