package ca.ece.ubc.cpen221.mp5;
//TODO CHECK NUMBERS IN SUBSTRINGS

/**
 * This class dictates the protocol the clients and server will use
 * 
 * @THREAD_SAFETY_ARGUMENT: None of these methods modify any of the DB themselves
 *   Ones that call mutator methods only call the mutator methods from ResDB that have
 *   locks.
 *   
 *   @REP_INVARIANT: Any string is a valid input into this class but the class itself doesn't have
 *    any values.
 *   
 *   @ABSTRACTION_FUNCTION: This call represents a protocol, it dictates the responses
 *    the server will give based on predefined client requests.
 */
public class Protocol {

	/**
	 * This method is what takes a client's request and formulates the correct
	 * response
	 * 
	 * @param request,
	 *            given from the client, received from RestaurantDBServer
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
				return "Review Not Found or Malformed Request";
			} else {
				return result;
			}

			// If the client requests a certain restaurant
		} else if (request.contains("getRestaurant")) {
			businessID = request.substring(15, request.length() - 2);
			result = RestaurantDB.getRestaurant(businessID);

			if (result.equalsIgnoreCase("Restaurant Not Found")) {
				return "Restaurant Not Found or Malformed Request";
			} else {
				return result;
			}

			// If the client requests to add a restaurant
		} else if (request.contains("addRestaurant")) {
			// Minus 2 because you should take 3 off to avoid string out
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

		// Otherwise the string cannot be dealt with
		return "Invalid String";
	}

}
