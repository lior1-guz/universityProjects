package cards;

public class Deck {

	private Card[] arrayOfCards;
	private int numOfCards;

	public Deck(int num)
	/* constructor for creating an array of Cards when the size is given */
	{
		arrayOfCards = new Card[num * 4];
		numOfCards = 4 * num;
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < 4; j++) {
				arrayOfCards[4 * i + j] = new Card(i, j);
			}
		}
	}

	public Deck(Deck from, int num)
	/* constructor for creating an array of Cards by taking cards from other Deck */
	{
		int len = num;
		if (num > from.getNumCards()) {
			len = from.getNumCards();
		}
		arrayOfCards = new Card[len];
		numOfCards = len;
		for (int i = 0; i < numOfCards; i++) {
			arrayOfCards[i] = from.takeOne();
		}
	}

	public Deck(Deck first, Deck second)
	/* constructor for creating an array of Cards by taking cards from two Decks */
	{
		numOfCards = first.getNumCards() + second.getNumCards();
		// sets the length of the needed arrayOfCards
		arrayOfCards = new Card[this.getNumCards()];// create the new deck
		int i = 0;
		while (first.getNumCards() != 0 && second.getNumCards() != 0)// until one of the decks is empty
		{
			arrayOfCards[i] = first.takeOne();
			arrayOfCards[i + 1] = second.takeOne();
			i = i + 2;
		}

		while (first.getNumCards() != 0)// if the first is not empty continue with it
		{
			arrayOfCards[i] = first.takeOne();
			i++;
		}
		while (second.getNumCards() != 0)// if the second is not empty continue with it
		{
			arrayOfCards[i] = second.takeOne();
			i++;
		}
	}

	public int getNumCards() {
		return numOfCards;
	}

	public Card takeOne()
	/* takeOne takes a card from the end of the Deck and returns it */
	{
		if (numOfCards == 0) {
			return null;
		} else {
			Card returnCard = arrayOfCards[--numOfCards];
			Card[] tempArrayOfCards = new Card[numOfCards];
			for (int i = 0; i < numOfCards; i++)
				tempArrayOfCards[i] = arrayOfCards[i];
			arrayOfCards = tempArrayOfCards;
			return returnCard;
		}
	}

	public String toString() {
		String str = arrayOfCards[0].toString() + ", ";
		for (int i = 1; i < numOfCards; i++) {
			if (i != numOfCards - 1) {
				str = str + arrayOfCards[i] + ", ";
			} else {
				str = str + arrayOfCards[i];
			}
		}
		return "[" + str + "]";
	}

	public void sort()
	/* using BubbleSort to Sort the Deck */
	{
		boolean sorted = false;
		while (!sorted) { // this while loop will sort the cards until the swaps are not needed
			sorted = true; // assume sorted
			for (int j = 0; j < numOfCards - 1; j++) {
				if (arrayOfCards[j].compareTo(arrayOfCards[j + 1]) > 0)
				/*
				 * using comareTo method to compare the current card with the next in the Deck
				 */
				{
					Card temp = arrayOfCards[j];
					arrayOfCards[j] = arrayOfCards[j + 1];
					arrayOfCards[j + 1] = temp;
					sorted = false; // if we had to swap anything
					// then we assume the array is not sorted
				}
			}
		}
	}
}
