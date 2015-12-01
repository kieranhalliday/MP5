package ca.ece.ubc.cpen221.mp5;

public class Protocol {

	public String processInput(String request) {
		// TODO This is where we decide if it's a valid argument
		// TODO implement this class
		String name;
		String result;
		String businessID;

		if (request.contains("randomReview")) {
			name = request.substring(14,request.length()-2);
			result = RestaurantDB.getReview(name);
			
			if (result.equalsIgnoreCase("Review Not Found")) {
				return "Review Not Found";
			} else {
				return result;
			}
			
		} else if (request.contains("getRestaurant")) {
			businessID = request.substring(15, request.length() - 2);
			result = RestaurantDB.getRestaurant(businessID);
			
			if (result.equalsIgnoreCase("Restaurant Not Found")) {
				return "Restaurant Not Found";
			} else {
				return result;
			}

		} else if (request.contains("addRestaurant")) {
			// Minus two because you should take one off to avoid string out
			// of bounds and then take off " and )but substring does one for you
			name = request.substring(15, request.length() - 2);
			RestaurantDB.addRestaurant(name);
			return "Restaurant Added";

		} else if (request.contains("addUser")) {
			name = request.substring(9, request.length() - 2);
			RestaurantDB.addUser(name);
			return "User Added";

		} else if (request.contains("addReview")) {
			name = request.substring(11, request.length() - 2);
			RestaurantDB.addReview(name);
			return "Review Added";

		}

		return null;
	}

}
