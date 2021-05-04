package images;

public abstract class BaseImage implements Image{
	protected  int width,height;
	
	public BaseImage( int width,int height) {
		this.height=height;
		this.width=width;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	public abstract RGB get(int x, int y);

}
