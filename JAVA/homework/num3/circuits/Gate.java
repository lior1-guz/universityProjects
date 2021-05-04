package circuits;

public abstract class Gate {

	protected Gate[] inGates; //An array containing all the gates they input to that gate
	
	public Gate(Gate[] inGates) //Constructor for initializing the gate
	{
		this.inGates = inGates;
	}
	
	
	public boolean calc() throws CircuitException //Calculates the boolean value of the gate
	{
		boolean[] tempArr = null;
		if (inGates != null) 
		{
			tempArr = new boolean[inGates.length]; // Creates an array with true or false values
			int i = 0;
			for (Gate gateTemp : inGates) 
			{
				tempArr[i] = gateTemp.func(null);
				i++;
			}
		}
		return func(tempArr);
	}
	
	//An abstract method that calculates what the gate is supposed to calculate 
	//given an array of Boolean inputs
	protected abstract boolean func(boolean[] inValues) throws CircuitException;
	
	public abstract String getName(); // Returns the name of the gate
	
	public abstract Gate simplify();
	
	public String toString()
	{
		String str=getName();
		
		if(inGates!=null) 
		{
		   for(int i=0;i<inGates.length;i++)
		   {
			   if(i == 0)
			   {
				str += "[";
			   }
				
			   if(inGates[i] != null)
			   {
				str += inGates[i].toString();
			   }
			
			   if(inGates.length-i != 1)
			   {
				str+=", ";
			   }
		   }
		
		   if(!str.equals(getName()))
		   {
			  str+="]";
		   }
		}
		return str;
	}
	
}
	
