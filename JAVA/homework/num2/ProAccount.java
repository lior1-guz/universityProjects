package bank;


public class ProAccount extends Account {

	private int numberAdded;
	private int[] accountHistory;
	public ProAccount(String name)
	{
		super(name);
		accountHistory= new int[100];
		numberAdded=0;
	}
	
	@Override
	public void add(int amount)
	{
		super.add(amount);
		accountHistory[numberAdded]=getShekels();
		numberAdded++;
	}
	
	
	public String toString()
	{
		
		String printStr=super.toString()+ " [" ;
		for(int i=0;i<numberAdded;i++)
		{
			printStr=printStr+ accountHistory[i];
			if(i!=numberAdded-1)
			{
				printStr=printStr+',';
			}
		}
		return printStr+']';
	}
	
	public static void transfer(ProAccount from, ProAccount to, int amount)
	{
		from.add(amount*(-1));
		to.add(amount);
	}
	
}
