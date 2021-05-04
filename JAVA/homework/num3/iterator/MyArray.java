package iterator;

import java.util.NoSuchElementException;

public class MyArray implements MyIterator{
	
	private int[] arrayOfInt;
	private int indexInArray;
	
	public MyArray(int[] array) {
		indexInArray=0;
		arrayOfInt=array;
		
	}
	
	@Override
	public boolean hasNext() {
		return indexInArray<arrayOfInt.length;
	}
	
	@Override
	public int next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		 return arrayOfInt[indexInArray++];
	}
}
