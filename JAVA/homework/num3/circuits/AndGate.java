package circuits;

public class AndGate extends Gate{
	
	public AndGate(Gate[] andGates) 
	{
		super(andGates);
	}
	
	@Override
	protected boolean func(boolean[] andValues) throws CircuitException
	{
		boolean temp = true;
		
		//If one of the values is false then it will always return false
		if (andValues != null) 
		{
			for (int i = 0; i < andValues.length; i++)
            {
				if (!(andValues[i] == true))
					temp = false;
			}
			return temp;
		}
		return calc();
	}
	
	@Override
	public String getName() 
	{
		return "AND";
	}
	
	@Override
	public Gate simplify()
	{
		int countHowManyNotTrue=0, j=0;
		Gate[] gateTemp = null;
		
		for(int i=0;i<inGates.length;i++)
		{
			//if one of the sons of OrGate is FalseGate, then return FalseGate
			if ((inGates[i].simplify() instanceof FalseGate) || (inGates[i] instanceof FalseGate))
			{
				return inGates[i].simplify();
			}
		}
		
		//if only one child left
		if (inGates.length == 1) 
		{
			if(inGates[0] instanceof TrueGate)
			{
				return inGates[0];
			}
			return inGates[0].simplify();
		}
		
		//Count How Many Not False
		for(int i=0;i<inGates.length;i++)
		{
			if ((inGates[i].simplify() instanceof TrueGate) || (inGates[i] instanceof TrueGate))
			{
				countHowManyNotTrue--;
			}
			countHowManyNotTrue++;
		}
		
		
		if (countHowManyNotTrue != 0) // If there are gates
		{
			gateTemp = new Gate[countHowManyNotTrue];
			
		 for(int i=0;i<inGates.length;i++)
		 {
			 if(!((inGates[i].simplify() instanceof TrueGate) || (inGates[i] instanceof TrueGate)))
			 {
				 gateTemp[j++] = inGates[i].simplify();
			 }
		 }
		 
		 if(gateTemp.length == 1) // if only one gate
		 {
			 return gateTemp[0];
		 }
			    
		 else
		 {
			 return new AndGate(gateTemp); // return new AndGate gate with all the children that left
		 }
		 
		}
		
		return TrueGate.instance(); // if there is not one left
	}
	
}
