package circuits;

public class VarGate extends Gate{
	
	private String name;
	private boolean valueTemp;
	private int valueHasBeenSet=0;
	
	public VarGate(String name)
	{
		super(null); // no need for any array
		this.name = name;
	}
	
	
	@Override
	public String getName() 
	{
		return "V"+name;
	}
	
	
	@Override
	protected boolean func(boolean[] inValues) throws CircuitException
	{
		if (valueHasBeenSet==0) //If no variable value is set then throw the exception
		{
			throw new CircuitException("The variable value has not yet been determined");
		}
		return valueTemp;
	}
	
	
	//A value is set for the variable
	public void setVal(boolean value)
	{
		valueTemp = value;
		valueHasBeenSet=1;
	}
	
	@Override
	public Gate simplify()
	{
		if (valueHasBeenSet == 1) // If a value variable has not been set, returns the instance of FalseGate
		{
			if (valueTemp == false) 
			{
				return FalseGate.instance();
			}
			return TrueGate.instance(); // else, return the instance of TrueGate
		}
		
		return this;
		
	}
	

}
