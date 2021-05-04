package images;

public abstract class BinaryImageDecorator implements Image {
	protected Image base1,base2;
	
	public BinaryImageDecorator(Image base1,Image base2)
	{
		this.base1=base1;
		this.base2=base2;
	}
	
	@Override
	public int getWidth() {
		int width1= base1.getWidth();
		int width2= base2.getWidth();
		if(width1>width2)
			return width1;
		else 
			return width2;
	}
	
	@Override
	public int getHeight() {
		int height1= base1.getHeight();
		int height2= base2.getHeight();
		if(height1>height2)
			return height1;
		else 
			return height2;
	}

	public abstract RGB get(int x, int y);


}
