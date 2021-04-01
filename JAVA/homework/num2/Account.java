package bank;

public class Account {

	private int moneyInBank;
	private String name;

public Account(String name)
{
	this.name=name;
	moneyInBank=0;
}

public int getShekels()
{
	return moneyInBank;
}

public String getName()
{
	return name;
}

public void add(int amount)
{
	moneyInBank=moneyInBank+amount;
}

public String toString()
{
	return name+ " has " + moneyInBank + " shekels";
}

}
