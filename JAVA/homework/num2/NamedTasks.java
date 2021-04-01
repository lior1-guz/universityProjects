package tasks;

public class NamedTasks extends Tasks{
	private String[] names;
	public NamedTasks(String[] names)
	{
		super(names.length);//num in Tasks = names.length
		this.names=names;
	}
	
	
	public boolean dependsOn(String task1,String task2)
	{
		int taskNum1 = -1, taskNum2 = -1;//this will take care of a problem if appears
		for (int i = 0; i < names.length; i++)
		{
			if (names[i].equals(task1))
				taskNum1 = i;
		}
		for (int i = 0; i < names.length; i++)
		{
			if (names[i].equals(task2))
				taskNum2 = i;
		}
		return super.dependsOn(taskNum1, taskNum2);
	}

	public String[] nameOrder()
	{
		int[] sortedArray=super.order();//call the superclass
		String[] sortedStringArray=new String[names.length];
		for(int i=0;i<names.length;i++)
		{
			sortedStringArray[i]=names[sortedArray[i]];
		}
		return sortedStringArray;
	}

}
