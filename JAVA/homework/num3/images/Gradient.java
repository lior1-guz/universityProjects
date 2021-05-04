package images;

public class Gradient extends BaseImage  {
	private RGB start,end;
	
	public Gradient(int width, int height, RGB start, RGB end) {
		super(width,height);
		this.start=start;
		this.end=end;
	}
	
	@Override
	public RGB get(int x, int y) {
		return RGB.mix(start, end, (1-(double)x/(double)getWidth()));
	}
	
}
