package circuits;

public class NotGate extends Gate {
	
	private Gate in;
	
	public NotGate(Gate in)
	{
		super(new Gate[] { in }); //array with only 1 val = {in}
		this.in = in;
	}
	
	@Override
	protected boolean func(boolean[] inValues) throws CircuitException
	{
		return !(in.func(new boolean[1]));
	}
	
	
	@Override
	public String getName() 
	{
		return "NOT";
	}
	
	@Override
	public Gate simplify()
	{
		
		if (in.simplify() instanceof FalseGate) //If the simplified son is FalseGate, then return TrueGate
		{
			return TrueGate.instance();
		}
		
		if (in.simplify() instanceof TrueGate) //If the simplified son is TrueGate, then return FalseGate
		{
			return FalseGate.instance();
		}
		
		
		 if (in instanceof NotGate) ////If the simplified son is NotGate, return the grandson
		 {
			 return inGates[0].inGates[0].simplify();
		 }
		 
		 return this; // if there is no value
		    
	}

}
