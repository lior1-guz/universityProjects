package images;

public class TwoColorImage extends BaseImage {

	private RGB zero, one;
	private TwoDFunc func;
	
	public TwoColorImage(int width, int height, RGB zero, RGB one, TwoDFunc func)
	{
		super(width,height);
		this.zero = zero;
		this.one = one;
		this.func = func;
	}
	
	@Override
	public RGB get(int x, int y) {
		double calculatedX = calculate(x, width);//calc the new x inside the picture
		double calculatedY = calculate(y, height);//calc the new y inside the picture
		double val = func.f(calculatedX, calculatedY);
		if (val == 1 || val >1) {
			return one;
		}
		if (val == 0 || val < 0) {
			return zero;
		}
		return RGB.mix(one, zero, val);//if it is between them then mix the colors
	}

	private double calculate(int firstVal, int secondVal) {
		return firstVal/(double)secondVal;
	}
	

}
