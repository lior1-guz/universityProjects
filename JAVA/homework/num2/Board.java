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
		int rMove, dMove, lMove, uMove, ldMove, rdMove, luMove, ruMove;
		int maxLenMove = 0;
		//r - right , d - down , l - left , u - up , ld - left + down and so on..
		ldMove = rayLength(i, j, -1, 1);
		rdMove = rayLength(i, j, 1, 1);
		luMove = rayLength(i, j, -1, -1);
		ruMove = rayLength(i, j, 1, -1);
		rMove = rayLength(i, j, 1, 0);
		dMove = rayLength(i, j, 0, 1);
		lMove = rayLength(i, j, -1, 0);
		uMove = rayLength(i, j, 0, -1);
		
		
		//check if we need to update maxLenMove 
		if (uMove + dMove - 1 > maxLenMove)
			maxLenMove = uMove + dMove - 1;
		if (rMove + lMove - 1 > maxLenMove)
			maxLenMove = rMove + lMove - 1;
		if (ruMove + ldMove - 1 > maxLenMove)
			maxLenMove = ruMove + ldMove - 1;
		if (rdMove + luMove - 1 > maxLenMove)
			maxLenMove = rdMove + luMove - 1;
		return maxLenMove;
	}
	
	private int rayLength(int x, int y, int dx, int dy)
	{
		int NewXDirec = x, NewYDirec = y, len = 0;
		if (board[x][y] == null) //stop looking there is nothing out there
		{
			return 0;
		}
		while ((NewXDirec < n && NewXDirec > -1) && (NewYDirec < m && NewYDirec > -1)) //Check if we are in the boundaries
		{
			if ((board[NewXDirec][NewYDirec] == board[x][y])) // check if the next one is the same as the current
			{ 
				len++;
				NewXDirec += dy;
				NewYDirec += dx;
			}
			else
				{
				return len;
				}

		}
		return len;
	}
	

}
