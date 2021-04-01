package tasks;

public class Tasks {
	
private int[][] tasks;
private int numOfTasks;

public Tasks(int num)
{
	tasks=new int[num][num];
	numOfTasks=num;
	for(int i=0;i<num;i++)//generate the other cells to -1 as if the cells are not used yet
	{
		for(int j=0;j<num;j++)
		{
			tasks[i][j]=-1;
		}
	}
}

public boolean dependsOn(int task1, int task2)
{
	if(task1<0 ||task1>=numOfTasks||task2<0 || task2>=numOfTasks)
	{
		return false;
	}
	tasks[task2][task1]=task2;//put in the array[task2][task1] who is the task that you can't pass
	return true;
}

public int[] order()//using topological sort
{
	int[]dependsArray=new int[numOfTasks];//this array will show how many dependences each task has
	for(int i=0;i<numOfTasks;i++)
	{
		for(int j=0;j<numOfTasks;j++)
		{
			if(tasks[i][j]!=-1)//if there is dependence Inc
			{
				dependsArray[j]++;
			}
		}
	}
	//now we know how many dependences each task has
	int stack[]=new int[numOfTasks];
	int stackIndex=0;
	int currentStackIndex=0;
	for(int i=0;i<numOfTasks;i++)//fill the stack with tasks with no dependences
	{
		if(dependsArray[i]==0)
		{
			stack[stackIndex++]=i;
		}
	}
	int[]orderedArray=new int[numOfTasks];//this is the outcome array
	int orderIndex=0;
	while(stackIndex!=currentStackIndex && stackIndex<=numOfTasks)//if the stack is not empty yet
	{
		int at=stack[currentStackIndex++];
		orderedArray[orderIndex++]=at;//fill the ordered array
		for(int j=0;j<numOfTasks;j++)
		{
			if(tasks[at][j]!=-1)//check if someone is dependent on that task
			{
				dependsArray[j]--;
				if(dependsArray[j]==0)
				{
					stack[stackIndex++]=j;
				}
				tasks[at][j]=-1;
			}
		}	
	}
	if(orderIndex!=numOfTasks)
	{
		return null;
	}
	return orderedArray;
}

}
