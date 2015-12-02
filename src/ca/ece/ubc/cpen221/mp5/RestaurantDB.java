package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// TODO: This class represents the Restaurant Database.
// Define the internal representation and
// state the rep invariant and the abstraction function.

/**
 * 	Rep Invariant: A Hashmap of Integers and Strings, the Strings must be in JSON format
 * 
 *  Abstraction Function: Each Hashmap represents either a numbered lists of all the restaurants, reviews or users
 */

public class RestaurantDB {
	private static HashMap<Integer, String> restaurants;
	private static HashMap<Integer, String> reviews;
	private static HashMap<Integer, String> users;

	
	/**
	 * Create a database from the Yelp dataset given the names of three files:
	 * <ul>
	 * <li>One that contains data about the restaurants;</li>
	 * <li>One that contains reviews of the restaurants;</li>
	 * <li>One that contains information about the users that submitted reviews.
	 * </li>
	 * </ul>
	 * The files contain data in JSON format.
	 * 
	 * @param restaurantJSONfilename
	 *            the filename for the restaurant data
	 * @param reviewsJSONfilename
	 *            the filename for the reviews
	 * @param usersJSONfilename
	 *            the filename for the users
	 * 
	 * @creates Three Hashmaps with one for each of following categories: the
	 *          reviews and the restaurants and the users
	 */
	public RestaurantDB(String restaurantJSONfilename, String reviewsJSONfilename, String usersJSONfilename) {
		// Currently this method has one entry per every JSON entry, no
		// seperation of each field

		String line = null;
		int i = 0;

		HashMap<Integer, String> restaurants = new HashMap<Integer, String>();
		HashMap<Integer, String> reviews = new HashMap<Integer, String>();
		HashMap<Integer, String> users = new HashMap<Integer, String>();

		RestaurantDB.restaurants = restaurants;
		RestaurantDB.reviews = reviews;
		RestaurantDB.users = users;

		// Create restaurant hashmap
		try {
			FileReader filereader = new FileReader(restaurantJSONfilename);
			BufferedReader reader = new BufferedReader(filereader);

			while ((line = reader.readLine()) != null) {
				restaurants.put(i, line);
				System.out.println(line);
				i++;
			}
			reader.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Create reviews hashmap
		// TODO In reviews sometimes there is a /n need to find new way to include the part after in same review
		try {
			FileReader filereader1 = new FileReader(reviewsJSONfilename);
			BufferedReader reader1 = new BufferedReader(filereader1);

			while ((line = reader1.readLine()) != null) {
				reviews.put(i, line);
				i++;
			}
			reader1.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		//Create users hashmap
		try {
			FileReader filereader2 = new FileReader(usersJSONfilename);
			BufferedReader reader2 = new BufferedReader(filereader2);

			while ((line = reader2.readLine()) != null) {
				users.put(i, line);
				i++;
			}
			reader2.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param queryString,
	 *            type of restaurant you are searching for
	 * @return set, the set of all restaurants that match the search
	 */

	// TODO Write specs, etc.
	// Parse query compare to restaurants hashmap
	// Assuming queryString is a valid argument
	// Cannot handle || yet

	public Set<Restaurant> query(String queryString) throws IllegalArgumentException {
		// B and D are used for the range of the price and
		// Rating, E is the price of the restaurant in the json file
		int n, b = 0, d = 0, e = 0;

		// Range of price and rating
		int upperbounds, lowerbounds;
		Parser p = new Parser();

		String sample = queryString; // Need this to preserve the original query
		Set<Restaurant> location = new HashSet<Restaurant>();
		Set<Restaurant> categories = new HashSet<Restaurant>();
		Set<Restaurant> names = new HashSet<Restaurant>();
		Set<Restaurant> prices = new HashSet<Restaurant>();
		Set<Restaurant> ratings = new HashSet<Restaurant>();
		Set<Restaurant> result = new HashSet<Restaurant>();

		// Check location

		if (sample.contains("in")) { // Get where the entry was
			n = sample.indexOf("in"); // Take off everything before it
			sample.substring(n + 2); // Take off everything after the close
			sample.split(")", 1); // parens...end of in query

			for (int i = 0; i < restaurants.size(); i++) {
				if (restaurants.get(i).contains(sample)) {
					String k = p.getName(restaurants.get(i));
					Restaurant temp = new Restaurant(k);
					location.add(temp);
				}
			}
		}

		sample = queryString;

		// Check categories

		if (sample.contains("category")) {
			n = sample.indexOf("category");
			sample.substring(n + 10);
			sample.split(")", 1);

			for (int i = 0; i < restaurants.size(); i++) {
				if (restaurants.get(i).contains(sample)) {
					String k = p.getName(restaurants.get(i));
					Restaurant temp = new Restaurant(k);
					categories.add(temp);
				}
			}

		}
		sample = queryString;

		// Check name

		if (sample.contains("name")) {
			n = sample.indexOf("name");
			sample.substring(n + 6);
			sample.split(")", 1);

			for (int i = 0; i < restaurants.size(); i++) {
				if (restaurants.get(i).contains(sample)) {
					String k = p.getName(restaurants.get(i));
					Restaurant temp = new Restaurant(k);
					names.add(temp);
				}
			}

		}
		sample = queryString;

		//  needs to be for a range of ints

		if (sample.contains("rating")) {
			n = sample.indexOf("rating");
			sample.substring(n + 8);
			sample.split(")", 1);
			String sample1 = sample;

			String a = sample.substring(0, 0);
			String c = sample1.substring(3);

			if (a.contains("1")) {
				b = 1;
			} else if (a.contains("1")) {
				b = 2;
			} else if (a.contains("1")) {
				b = 3;
			} else if (a.contains("1")) {
				b = 4;
			} else if (a.contains("1")) {
				b = 5;
			}
			if (c.contains("1")) {
				d = 1;
			} else if (c.contains("2")) {
				d = 2;
			} else if (c.contains("3")) {
				d = 3;
			} else if (c.contains("4")) {
				d = 4;
			} else if (c.contains("5")) {
				d = 5;
			}

			if (b > d) {
				upperbounds = b;
				lowerbounds = d;
			} else {
				upperbounds = d;
				lowerbounds = b;
			}

			for (int i = 0; i < restaurants.size(); i++) {
				int size = restaurants.get(i).length();
				String num = restaurants.get(i).substring(size - 2, size - 2);
				if (num.contains("1")) {
					e = 1;
				} else if (num.contains("2")) {
					e = 2;
				} else if (num.contains("3")) {
					e = 3;
				} else if (num.contains("4")) {
					e = 4;
				} else if (num.contains("5")) {
					e = 5;
				}
				if (e <= upperbounds && e >= lowerbounds) {
					String k = p.getName(restaurants.get(i));
					Restaurant temp = new Restaurant(k);
					ratings.add(temp);
				}
			}

		}

		sample = queryString;

		//  Check price, needs to be for a range of ints

		if (sample.contains("price")) {
			n = sample.indexOf("price");
			sample.substring(n + 7);
			sample.split(")", 1);
			String sample1 = sample;

			String a = sample.substring(0, 0);
			String c = sample1.substring(3);

			if (a.contains("1")) {
				b = 1;
			} else if (a.contains("1")) {
				b = 2;
			} else if (a.contains("1")) {
				b = 3;
			} else if (a.contains("1")) {
				b = 4;
			} else if (a.contains("1")) {
				b = 5;
			}
			if (c.contains("1")) {
				d = 1;
			} else if (c.contains("2")) {
				d = 2;
			} else if (c.contains("3")) {
				d = 3;
			} else if (c.contains("4")) {
				d = 4;
			} else if (c.contains("5")) {
				d = 5;
			}

			if (b > d) {
				upperbounds = b;
				lowerbounds = d;
			} else {
				upperbounds = d;
				lowerbounds = b;
			}

			for (int i = 0; i < restaurants.size(); i++) {
				int size = restaurants.get(i).length();
				String num = restaurants.get(i).substring(size - 2, size - 2);
				if (num.contains("1")) {
					e = 1;
				} else if (num.contains("2")) {
					e = 2;
				} else if (num.contains("3")) {
					e = 3;
				} else if (num.contains("4")) {
					e = 4;
				} else if (num.contains("5")) {
					e = 5;
				}
				if (e <= upperbounds && e >= lowerbounds) {
					String k = p.getName(restaurants.get(i));
					Restaurant temp = new Restaurant(k);
					prices.add(temp);
				}
			}

		}

		// TODO add all the ones that fullfill all request to result

		return result;

	}
	
	/**
	 * @modifies RestaurantDB.restaurants by adding a new restaurant
	 * @param request
	 */
	public static void addRestaurant(String request) {
		restaurants.put(restaurants.size(), request);
	}

	/**
	 * 
	 * @modifies RestaurantDB.users by adding a new user
	 * @param request
	 */
	public static void addUser(String request) {
		users.put(users.size(), request);

	}

	/**
	 * @modifies RestaurantDB.reviews by adding a new review
	 * @param request
	 */
	public static void addReview(String request) {
		reviews.put(reviews.size(), request);

	}

	/**
	 * 
	 * @param ID
	 * @return The restaurant whose ID is the param
	 */
	public static String getRestaurant(String ID) {
		Parser p = new Parser();

		for (int i = 0; i < restaurants.size(); i++) {
			if (restaurants.get(i).contains(ID)) {
				return p.getName(restaurants.get(i));
			}
		}
		return ("Restaurant Not Found");
	}

	/**
	 * 
	 * @param name
	 * @return A random review of the restaurant whose name is the param
	 */
	public static String getReview(String name) {

		String ID = null;
		int start, end;
		String review;

		for (int i = 0; i < restaurants.size(); i++) {
			if (restaurants.get(i).contains(name)) {
				ID = restaurants.get(i);
				start = ID.indexOf("id");
				ID.substring(start + 6, start + 28);
			}
		}
		for (int j = 0; j < reviews.size(); j++) {
			int randomnum = randInt(1, 0);
			if (reviews.get(j).contains(ID) && randomnum > 0.66) {
				start = ID.indexOf("text:");
				ID.substring(start + 9);
				end = ID.indexOf("stars");
				review = ID.substring(0, end - 4);

				return review;
			}
		}

		// Try twice for good measure
		for (int k = 0; k < reviews.size(); k++) {
			int randomnum = randInt(1, 0);
			if (reviews.get(k).contains(ID) && randomnum > 0.66) {
				start = ID.indexOf("text:");
				ID.substring(start + 9);
				end = ID.indexOf("stars");
				review = ID.substring(0, end - 4);

				return review;
			}
		}

		return "Review not Found";
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return A random number between those two numbers
	 */
	public static int randInt(int min, int max) {

		Random rand = null;
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/**
	 * 
	 * @return the size of restaurants
	 */
	public static int Restaurantsize() {
		return restaurants.size();
	}

	/**
	 * 
	 * @return the size of reviews
	 */
	public static int Reviewsize() {
		return reviews.size();
	}

	/**
	 * 
	 * @return the size of users
	 */
	public static int Usersize() {
		return users.size();
	}

	/**
	 * 
	 * @return The hashmap restaurants
	 */
	public static HashMap<Integer, String> getRes() {
		return restaurants;

	}

	/**
	 * 
	 * @return the hashmap reviews
	 */
	public static HashMap<Integer, String> getReviews() {
		return reviews;

	}

	/**
	 * 
	 * @return the hashmap users
	 */
	public static HashMap<Integer, String> getUsers() {
		return users;

	}
}
