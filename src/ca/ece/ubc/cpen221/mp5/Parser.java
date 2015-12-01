package ca.ece.ubc.cpen221.mp5;

public class Parser {

	public String getName(String jsonString) {
		String name = null;
		int start, end;
		
		for (int i = 0; i < RestaurantDB.Restaurantsize(); i++) {
			if (RestaurantDB.getRes().get(i).contains(name)) {
				name = RestaurantDB.getRes().get(i);
				start = name.indexOf("name");
				end = name.indexOf("categories");
				name.substring(start + 8, end -4);
			}
		}

		return name;

	}

	public String getID(String name) {
		String ID = null;
		int start;

		for (int i = 0; i < RestaurantDB.Restaurantsize(); i++) {
			if (RestaurantDB.getRes().get(i).contains(name)) {
				ID = RestaurantDB.getRes().get(i);
				start = ID.indexOf("id");
				ID.substring(start + 6, start + 28);
			}
		}

		return ID;
	}

}
