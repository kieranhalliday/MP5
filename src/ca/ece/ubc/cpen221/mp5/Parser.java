package ca.ece.ubc.cpen221.mp5;

/**
 * This class is used to preform common parsing tasks in 
 * other classes
 */

public class Parser {

	/**
	 * 
	 * @param jsonString
	 * @return A string that is the name of the restaurant whose
	 *  jsonRepresentation in the DB is the param
	 */
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

	/**
	 * 
	 * @param name
	 * @return A string that is the ID of the restaurant whose
	 *  jsonRepresentation in the DB is the param
	 */
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
