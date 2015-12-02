package ca.ece.ubc.cpen221.mp5;

public class Protocol {

	/**
	 * This method is what takes a client's request and formulates the correct
	 * response
	 * @param request
	 * @return The String to be given to the client
	 */

	public String processInput(String request) {
		String name;
		String result;
		String businessID;

		// If the client requests a random review
		if (request.contains("randomReview")) {
			name = request.substring(14, request.length() - 2);
			result = RestaurantDB.getReview(name);

			if (result.equalsIgnoreCase("Review Not Found")) {
				return "Review Not Found";
			} else {
				return result;
			}

			// If the client requests a certain restaurant
		} else if (request.contains("getRestaurant")) {
			businessID = request.substring(15, request.length() - 2);
			result = RestaurantDB.getRestaurant(businessID);

			if (result.equalsIgnoreCase("Restaurant Not Found")) {
				return "Restaurant Not Found";
			} else {
				return result;
			}

			// If the client requests to add a restaurant
		} else if (request.contains("addRestaurant")) {
			// Minus two because you should take one off to avoid string out
			// of bounds and then take off " and )but substring does one for you
			name = request.substring(15, request.length() - 2);
			RestaurantDB.addRestaurant(name);
			return "Restaurant Added";

			// If the client requests to add a user
		} else if (request.contains("addUser")) {
			name = request.substring(9, request.length() - 2);
			RestaurantDB.addUser(name);
			return "User Added";

			// If the client requests to add a review
		} else if (request.contains("addReview")) {
			name = request.substring(11, request.length() - 2);
			RestaurantDB.addReview(name);
			return "Review Added";

		}

		//Otherwise the string cannot be dealt with
		return "Invalid String";
	}

}
