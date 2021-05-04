package images;

public abstract class ImageDecorator implements Image {
	
	protected Image base;
	public ImageDecorator(Image base)
	{
		this.base=base;
	}
	
	@Override
	public int getWidth() {
		return base.getWidth();
	}
	
	@Override
	public int getHeight() {
		return base.getHeight();
	}

	public abstract RGB get(int x, int y);


}
