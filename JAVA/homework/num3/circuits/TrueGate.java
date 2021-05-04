package circuits;

public class TrueGate extends Gate{
	
	private static TrueGate instance = null;

	private TrueGate() 
	{
		super(null); // no need for any array
	}
	
	public static Gate instance()
	{
		if (instance == null) // if we did not create yet an instance . else return the same instance
		{
		   instance = new TrueGate();
		}
	    return instance;
	}
	
	@Override
	protected boolean func(boolean[] inValues) throws CircuitException
	{
		return true;
	}

	@Override
	public String getName() 
	{
		return "T";
	}
	
	@Override
	public Gate simplify()
	{
		return this;
	}
	
}
