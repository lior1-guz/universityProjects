package tree;

public class Node {
	private int count;
	private Node[] children;

	public Node() {
		count = 0;
		children = new Node['z' - 'a' + 1];// create a new array of Nodes
	}
	
	public int num(String s)
	{
		
		if (s.equals(""))//check if this is an empty string
		{
			return count;
		}
		if (children[s.charAt(0)-'a']==null)//no such place in the array
		{
			return 0;
		}
		return children[s.charAt(0)-'a'].num(s.substring(1));
	}
	
	public void add(String s)
	{
		if (s.equals(""))
		{
			count++;
			return;
		}
		if (children[s.charAt(0)-'a']!=null)
		{
			 children[s.charAt(0)-'a'].add(s.substring(1));
		}
		else
		{
		Node temp =new Node();
		children[s.charAt(0)-'a']=temp;//allocate and merge to the Node array
		children[s.charAt(0)-'a'].add(s.substring(1));
		}
	}

}
