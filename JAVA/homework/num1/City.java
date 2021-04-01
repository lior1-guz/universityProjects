package cities;

public class City {
	private Road[] roads;
	private int numRoads;
	private String name;

	public City(String name) {
		this.name = name;
		numRoads = 0;
		roads = new Road[10];// create an array of roads
	}

	public void connect(Road r)// put the current road in the roads array
	{
		roads[numRoads] = r;
		numRoads++;
	}

	public City nearestCity() {

		if (numRoads == 0)// if there are no roads attached at all
		{
			return null;
		} else {
			int minKilometer = roads[0].getLength();
			int indx = 0;
			for (int i = 0; i < numRoads; i++)// find the nearest road using for loop
			{
				if (minKilometer > roads[i].getLength()) {
					minKilometer = roads[i].getLength();
					indx = i;
				}
			}
			return roads[indx].getCity2();
		}
	}

	public String toString() {
		return name;
	}

}
