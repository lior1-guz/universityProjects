package circuits;

public class FalseGate extends Gate{
	
	private static FalseGate instance = null;

	private FalseGate() 
	{
		super(null); // no need for any array
	}
	
	public static Gate instance()
	{
		if (instance == null) // if we did not create yet an instance . else return the same instance
		{
		   instance = new FalseGate();
		}
	    return instance;
	}
	
	@Override
	protected boolean func(boolean[] inValues) throws CircuitException
	{
		return false;
	}

	@Override
	public String getName() 
	{
		return "F";
	}
	
	@Override
	public Gate simplify()
	{
		return this;
	}
	
}

