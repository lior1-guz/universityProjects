package library;

public class Book {
	private String title;
	private Author auth;

	public Book(String title, Author auth) {
		this.title = title;
		this.auth = auth;
	}

	public String getTitle() // get the Book title
	{
		return title;
	}

	public String getAuthorName() // get the author's name
	{
		return auth.getName();
	}

	public int getAuthorBirthYear() // get the author's BirthYear
	{
		return auth.getBirthYear();
	}

	public String toString() // print the data of the Book
	{
		return title + " written by " + auth.toString();
	}
}
