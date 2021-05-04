package iterator;

public class IteratorToString {

	public static String toString(MyIterator it) {
		StringBuilder b = new StringBuilder();
		b.append("[");
		while(it.hasNext()) {
			b.append(it.next());
			b.append(" ");
		}
		b.deleteCharAt(b.length()-1);
		b.append("]");
		return b.toString();
		}
}
