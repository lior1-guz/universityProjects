package library;

public class Library {

	private Book bookArray[];

	public Library(int size)// create a library with array of books
	{
		bookArray = new Book[size];
	}

	public void setBook(int bookNum, String title, Author auth) // create a Book and put it in the array
	{
		bookArray[bookNum] = new Book(title, auth);
	}

	public Book getBook(int bookNum)// get Book data
	{
		if (bookArray[bookNum] == null) {
			return null;
		} else {
			return bookArray[bookNum];
		}
	}
}
