package tree;
import java.util.Scanner;

public class ReversedWords {
	
	public static int checkReversed()
	{
		Node root = new Node();
		boolean stopWhile=false;
		int counterOfReversed=0;
		Scanner s = new Scanner(System.in);
		String tempStr;
		while(s.hasNext() && !stopWhile)//run until there is no more strings in the buff
		{
			tempStr=s.next();
			if(!tempStr.equals("X")) //if we haven't reached to the end get the data from the user
			{
				if(root.num(reveseString(tempStr))>=1)
				{
					counterOfReversed++;
				}
				root.add(tempStr);//save the word in the Node tree
			}
			else
			{
				stopWhile=true;
			}
		}
		return counterOfReversed;
	}
	private static String reveseString(String toReverse)
	{
		String newReversedString="";
		char tempForSwap;
		char[] reversedString=toReverse.toCharArray();
		for(int i=0;i<reversedString.length/2;i++)
		{
			tempForSwap=reversedString[reversedString.length-i-1];
			reversedString[reversedString.length-i-1]=reversedString[i];//swap the values
			reversedString[i]=	tempForSwap;
		}
		for(int i=0;i<reversedString.length;i++)
		{
			newReversedString=newReversedString+reversedString[i];
		}
		return newReversedString;
	}
	
	

}
