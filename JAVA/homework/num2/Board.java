package game;

public class Board {
	
	protected Player[][] board;
	protected int n,m;
	public Board(int n, int m)
	{
		 this.n=n;
		 this.m=m;
		 board= new Player[n][m];
	}
	
	protected boolean set(int i, int j, Player p)
	{
		if(isEmpty(i,j))//it means that the place is empty 
		{
			board[i][j]=p;
			return true;
		}
		return false;
	}
	
	public boolean isEmpty(int i, int j) 
	{
		return board[i][j]==null? true : false;
	}

	public Player get(int i, int j)
	{
		return board[i][j];
	}
	
	public boolean isFull()
	{
		boolean isFullBoard=true;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				if(get(i,j)==null)//empty
				{
					isFullBoard=false;
				}
			}
		}
		
		return isFullBoard;
	}
	
	public String toString()
	{
		String outPutBoard="";
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				if(isEmpty(i,j)==true)
				{
					outPutBoard=outPutBoard+".";
				}
				else
				{
					outPutBoard=outPutBoard+board[i][j].getMark();
				}
			}
			outPutBoard=outPutBoard+"\n";
		}
		return outPutBoard;
	}
	
	protected int maxLineContaining(int i, int j)
	{
		int currentSequence=0;
		int directionX=-1,directionY=-1;
		int maxSequence=0;
		while(directionX!=1 || directionY!=1)
		{
			if(directionX!=0 || directionY!=0)
				currentSequence=rayLength(i,j,directionX,directionY);
			directionY++;
			if(directionY==1 && directionX!=1)
			{
				directionX++;
				directionY=-1;
			}
			if(currentSequence>maxSequence)
				maxSequence=currentSequence;
		}
		return maxSequence;
	}
	
	private int rayLength(int x, int y, int dx, int dy)
	{
		int directionX=x+dx;
		int directionY=y+dy;
		int sequenceInARow=1;
		while(!(directionX>=n||directionX<0 ||directionY>=m || directionY<0))/*check if we 
		are still in the boundaries of the board*/	
		{
			if(board[directionX][directionY]==get(x,y))
			{
				sequenceInARow++;
			}
			else
				{
				return sequenceInARow;
				}
			directionX=directionX+dx;
			directionY=directionY+dy;
		}
		return sequenceInARow;
	}
	
	

}
