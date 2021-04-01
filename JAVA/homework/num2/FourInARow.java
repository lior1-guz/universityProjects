package game;


public class FourInARow extends Game {
	
	public FourInARow(String player1, String player2)
	{
		super(6,7,new Player(player1,'W'),new Player(player2,'B'));
	}

	@Override
	protected boolean doesWin(int i, int j)
	{
		if(maxLineContaining(i,j)==4)
		{
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean onePlay(Player p)
	{
		System.out.println(p + ", please enter column: ");
		int col=s.nextInt();
		int row=5;
		boolean colFull=false;
		boolean stopWhile=false;
		while(row>=0 &&!isEmpty(row,col))
		{
			if(row==0)
			{
				colFull=true;
				break;//full column 
			}
			row--;
		}
		
		while(colFull)//check if the place the user asked is valid for use
		{
			System.out.println("The column is Full...");
			System.out.println(p + ", please enter column: ");
			col=s.nextInt();
			row=5;
			while(row>=0 &&!isEmpty(row,col))
			{
				if(row==0)
				{
					colFull=true;
					stopWhile=true;
					break;//full column 
				}
				row--;
			}
			if(!stopWhile)
			{
				colFull=false;
			}
			
		}
		set(row,col,p);
		return doesWin(row,col);
	}
}
