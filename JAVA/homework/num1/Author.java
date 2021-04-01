package library;

public class Author {
	private String name;
	private int birthYear;

	public Author(String name, int birthYear) {
		this.name = name;
		this.birthYear = birthYear;
	}

	public String getName() // get the name of the Author
	{
		return name;
	}

	public int getBirthYear() // get the BirthYear of the author
	{
		return birthYear;
	}

	public int getAge(int thisYear) // calculate the age of the author
	{
		return (thisYear - birthYear);
	}

	public String toString()// print the data of the author
	{
		return name + "(" + birthYear + ")";
	}
}
