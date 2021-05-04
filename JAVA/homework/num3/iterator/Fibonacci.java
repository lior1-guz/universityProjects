package iterator;

public class Fibonacci implements MyIterator{
	private int upperBound;
	private int firstVal;
	private int secondVal;
	private int sum;
	public Fibonacci(int upperBound) {
		this.upperBound=upperBound;
		firstVal=0;
		secondVal=0;
		sum=0;
	}
	
	@Override
	public boolean hasNext() {
		return ((firstVal+secondVal<upperBound) || (firstVal==0 && secondVal==1));
		/*check if its the first two numbers 0 and 1 or if we have exceeded the boundaries*/
	}
	
	@Override
	public int next() {
		if(hasNext()) {
		if(firstVal<1){//check if its still the first two ones to be printed
			firstVal++;
			return 1;
		}
		sum=firstVal;
		firstVal=firstVal+secondVal;
		secondVal=sum;
		return firstVal;
		}
		return firstVal;
	}
}
