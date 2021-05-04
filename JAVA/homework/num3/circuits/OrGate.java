package circuits;

public class OrGate extends Gate {

		public OrGate(Gate[] orGates) 
		{
			super(orGates);
		}
		
		@Override
		protected boolean func(boolean[] orValues) throws CircuitException
		{
			boolean temp = false;
			
			if (orValues != null)  // if there is at least one true the result is true
			{
				for (int i = 0; i < orValues.length; i++)
	            {
					if (orValues[i] == true)
					{
						temp = true;
						break;
					}
				}
				return temp;
			}
			return calc();
		}
		
		@Override
		public String getName() 
		{
			return "OR";
		}
		
		@Override
		public Gate simplify()
		{
			int countHowManyNotFalse=0, j=0;
			Gate[] gateTemp = null;
			
			for(int i=0;i<inGates.length;i++)
			{
				//if one of the sons of OrGate is TrueGate, then return TrueGate
				if ((inGates[i].simplify() instanceof TrueGate) || (inGates[i] instanceof TrueGate))
				{
					return inGates[i].simplify();
				}
			}
			
			//if only one child left
			if (inGates.length == 1) 
			{
				if(inGates[0] instanceof FalseGate)
				{
					return inGates[0];
				}
				return inGates[0].simplify();
			}
			
			//Count How Many Not False
			for(int i=0;i<inGates.length;i++)
			{
				if ((inGates[i].simplify() instanceof FalseGate) || (inGates[i] instanceof FalseGate))
				{
					countHowManyNotFalse--;
				}
				countHowManyNotFalse++;
			}
			
			
			if (countHowManyNotFalse != 0) // If there are gates
			{
				gateTemp = new Gate[countHowManyNotFalse];
				
			 for(int i=0;i<inGates.length;i++)
			 {
				 if(!((inGates[i].simplify() instanceof FalseGate) || (inGates[i] instanceof FalseGate)))
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
				 return new OrGate(gateTemp); // return new OrGate gate with all the children that left
			 }
			 
			}
			
			return FalseGate.instance(); // if there is not one left
		}

}
