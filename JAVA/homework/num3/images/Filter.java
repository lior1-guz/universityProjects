package images;

public class Filter extends ImageDecorator {
	private RGB filter;
	
	public Filter(Image base, RGB filter) {
		super(base);
		this.filter=filter;
	}
	
	public RGB get(int x, int y) {
		return base.get(x,y).filter(filter);
	}
	

}
