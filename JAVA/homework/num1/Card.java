package cards;

public class Card {

	private int num;
	private int suit;
	private String[] options = { "C", "D", "H", "S" };

	public Card(int num, int suit) {
		this.num = num;
		this.suit = suit;
	}

	public int getNum() {
		return num;
	}

	public int getSuit() {
		return suit;
	}

	public String toString() {
		return num + options[suit];
	}

	public int compareTo(Card other) {
		if ((other.getNum() == num) && (other.getSuit() == suit))// if the cards are the same
		{
			return 0;
		} else if (num > other.getNum())// if this card is bigger than the other
		{
			return 1;
		} else if (num < other.getNum())// if this card is smaller than the other
		{
			return -1;
		} else // if we are here than the numbers are equal and we will check the suit
		{
			if (suit > other.getSuit())// if this cards suit is bigger
			{
				return 1;
			}
			return -1;
		}
	}

}
