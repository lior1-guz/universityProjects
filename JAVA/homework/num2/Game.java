package game;

import java.util.Scanner;

public class Game extends Board  {
	
	protected Player[] players;
	protected Scanner s;
	
	public Game(int n, int m, Player p1, Player p2)
	{
		super(n, m);//call the contractor of Board
		players=new Player[2];
		players[0]=p1;
		players[1]=p2;
		s = new Scanner(System.in);//open for reading values from the user
	}

	protected boolean doesWin(int i, int j)
	{
		if(i==0 && j==0)
		{
			return true;
		}
		return false;
	}
	
	protected boolean onePlay(Player p)
	{
		if(isFull())
		{
			return false;
		}
		System.out.println(p + ", please enter x and y: ");
		int i=s.nextInt();
		int j=s.nextInt();	
		boolean isEmpty=isEmpty(i,j);
		while(isEmpty==false)//check if the place the user asked is valid for use
		{
			System.out.println("There is a piece there already...");
			System.out.println(p + ", please enter x and y: ");
			i=s.nextInt();
			j=s.nextInt();	
			isEmpty=isEmpty(i,j);
		}
		set(i,j,p);
		System.out.println(toString());
		return doesWin(i,j);
	} 
	
	public Player play()
	{
		boolean continuePlaying = true;
		while (continuePlaying)
		{	
			if (onePlay(players[0]))
			{
				System.out.println(players[0]+" Won!");
				return players[0];
			}
	
			if (onePlay(players[1]))
			{
				System.out.println(players[1]+" Won!");
				return players[1];
			}
			if(isFull())
			{
				System.out.println("The game ended in a draw!");
				break;
			}
		}
		return null;
	}
}
