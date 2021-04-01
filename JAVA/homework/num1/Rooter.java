package root;

public class Rooter {

	private double precision;

	public Rooter(double precision) {
		this.precision = precision;
	};

	public void setPrecision(double precision)/*
												 * setPrecision is a method that changes the precision of a given
												 * precision field in the class
												 */
	{
		this.precision = precision;
	};

	private double abs(double y)/*
								 * abs is a method that calculates the positive value of a given number
								 */
	{
		if (y < 0)
			y = (y) * (-1);
		return y;
	}

	public double sqrt(double x)/*
								 * This sqrt method calculates the square root of a given number using a given
								 * algorithm
								 */

	{
		double res;
		double one = x / 2;
		while (true) {
			double two = x / one;
			if (one == two)
				return one;
			res = abs(one - two);
			if (res < precision)
				return one;
			one = (one + two) / 2;
		}
	};
}