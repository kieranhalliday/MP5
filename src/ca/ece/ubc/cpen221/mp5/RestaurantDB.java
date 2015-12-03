package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// TODO: This class represents the Restaurant Database.

/**
 * Rep Invariant: A Hashmap of Integers and Strings, the Strings must be in JSON
 * format
 * 
 * Abstraction Function: Each Hashmap represents a numbered lists of either all
 * the restaurants, reviews or users
 * 
 * Thread Safety Argument: Each hashmap has a lock associated with it. Any
 * function that calls a mutator method must attain the lock before mutating the
 * DB (represented by a hashmap)
 */

public class RestaurantDB {
	private static HashMap<Integer, String> restaurants;
	private static HashMap<Integer, String> reviews;
	private static HashMap<Integer, String> users;
	private static final Object restaurantLock = new Object();
	private static final Object usersLock = new Object();
	private static final Object reviewsLock = new Object();

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

		HashMap<Integer, String> restaurants = new HashMap<Integer, String>();
		HashMap<Integer, String> reviews = new HashMap<Integer, String>();
		HashMap<Integer, String> users = new HashMap<Integer, String>();

		RestaurantDB.restaurants = restaurants;
		RestaurantDB.reviews = reviews;
		RestaurantDB.users = users;

		createDB(restaurantJSONfilename, reviewsJSONfilename, usersJSONfilename);
	}

	/**
	 * @param queryString,
	 *            type of restaurant you are searching for
	 * @return set, the set of all restaurants that match the search
	 * @throws IllegalArgumentException
	 *             if queryString is malformed
	 */

	// TODO Parse query compare to restaurants hashmap
	// Cannot handle || yet
	// To handle || and &&, go through string first and assign a 0 for AND and a
	// 1 for OR
	// Then get results based on if they are AND or OR

	public Set<Restaurant> query(String queryString) throws IllegalArgumentException {

		StringBuilder stringBuilder = new StringBuilder();

		ArrayList<Integer> andOrlist = new ArrayList<Integer>();
		ArrayList<String> commands = new ArrayList<String>();

		ArrayList<Restaurant> location = new ArrayList<Restaurant>();
		ArrayList<Restaurant> categories = new ArrayList<Restaurant>();
		ArrayList<Restaurant> names = new ArrayList<Restaurant>();
		ArrayList<Restaurant> prices = new ArrayList<Restaurant>();
		ArrayList<Restaurant> ratings = new ArrayList<Restaurant>();

		Set<Restaurant> results = new HashSet<Restaurant>();

		setUpArrays(queryString, prices, ratings, names, categories, location);

		setUpandOrlist(stringBuilder, queryString, andOrlist);

		setUpCommandslist(queryString, queryString, stringBuilder, commands);

		// TODO add all the ones that fullfill all request to result
		// Still can't do OR this is assuming everything is ANDed

		addtoArray(prices, ratings, names, categories, location, results, andOrlist, commands);

		return results;

	}

	/**
	 * Get the commands from the query string and places them into an arraylist
	 * 
	 * @Postcondition: This will return an ArrayList with one command per index
	 *                 Commands that are surrounded with brackets will be left
	 *                 together
	 * 
	 * @param queryString
	 * @param sample
	 * @param sample1
	 * @param stringBuilder
	 */
	private void setUpCommandslist(String sample, String sample1, StringBuilder stringBuilder,
			ArrayList<String> destination) {
		int numOpen = 0, numClose = 0, end;
		String to_add;
		for (int counter1 = 0; counter1 < sample.length(); counter1++) {
			stringBuilder.append(sample.charAt(counter1));
			if (stringBuilder.equals("(")) {
				numOpen++;
			} else if (stringBuilder.equals(")")) {
				numClose++;
				// Making the middle command of the example one command
				if (numClose == numOpen) {
					end = sample1.indexOf(")");
					to_add = sample1.substring(0, end + 2);
					sample1.substring(end + 2, sample1.length());
					destination.add(to_add);
					sample = sample1;
				}
			}
		}
	}

	/**
	 * Post condition of this will be that you have an arrayList with 0 and 1
	 * indicating which commands are ANDed or ORed
	 */
	private void setUpandOrlist(StringBuilder stringBuilder, String sample, ArrayList<Integer> andOrlist) {
		for (int counter1 = 0; counter1 < sample.length(); counter1++) {
			stringBuilder.append(sample.charAt(counter1));
			if (stringBuilder.equals("&") || stringBuilder.equals("|")) {
				// They need to occur twice in a row
				stringBuilder.append(sample.charAt(counter1 + 1));
				if (stringBuilder.equals("&") || stringBuilder.equals("|")) {
					if (stringBuilder.equals("&"))
						andOrlist.add(0);
					else
						andOrlist.add(1);
				}
			}
			stringBuilder.delete(0, 1);
		}
	}

	/**
	 * This method goes through a2 and checks if it exists in a1, a3, a4, and a5
	 * If yes, it add it to the destination List
	 *
	 * @param a1
	 * @param a2
	 * @param a3
	 * @param a4
	 * @param a5
	 * @param destination
	 */
	public void addtoArray(ArrayList<Restaurant> prices, ArrayList<Restaurant> ratings, ArrayList<Restaurant> names,
			ArrayList<Restaurant> categories, ArrayList<Restaurant> location, Set<Restaurant> results, ArrayList<Integer> andOR,
			ArrayList<String> commands) {
		int currentANDOR;
		//If commands has a || or && symbol split it up and add it to the arraylist
		// in the same place
		// 1 is OR

		for (int counter = 0; counter < prices.size(); counter++) {
			currentANDOR=andOR.get(counter);
			
			if (commands.get(counter).contains("in")) {
				//GET wanted location, check all maps to see if it is there
				//if current command is OR, add it
				// if current command is AND, put true into arraylist but dont move onto next command until 2 cycles
				if(currentANDOR ==1){
				}else{
					
				}

				
			} else if (commands.get(counter).contains("category")) {
				//GET wanted category, check all maps to see if it is there
			} else if (commands.get(counter).contains("name")) {
				//GET wanted name, check all maps to see if it is there
			} else if (commands.get(counter).contains("rating")) {
				//GET wanted rating, check all maps to see if it is there
			} else if (commands.get(counter).contains("price")) {
				//GET wanted price, check all maps to see if it is there
			}
		}
	}

	/**
	 * @modifies RestaurantDB.restaurants by adding a new restaurant
	 * @param request
	 */
	public static void addRestaurant(String request) {
		synchronized (restaurantLock) {
			restaurants.put(restaurants.size(), request);
		}
	}

	/**
	 * @modifies RestaurantDB.users by adding a new user
	 * @param request
	 */
	public static void addUser(String request) {
		synchronized (usersLock) {
			users.put(users.size(), request);
		}
	}

	/**
	 * @modifies RestaurantDB.reviews by adding a new review
	 * @param request
	 */
	public static void addReview(String request) {
		synchronized (reviewsLock) {
			reviews.put(reviews.size(), request);
		}

	}

	/**
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
			double randomnum = randInt();
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
			double randomnum = randInt();
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
	 * @param min
	 * @param max
	 * @return A random number between those two numbers
	 */
	public static double randInt() {

		double randomNum = Math.random();

		return randomNum;
	}

	/**
	 * 
	 * @return the size of restaurants
	 */
	public static int Restaurantsize() {
		synchronized (restaurantLock) {
			return restaurants.size();
		}
	}

	/**
	 * 
	 * @return the size of reviews
	 */
	public static int Reviewsize() {
		synchronized (reviewsLock) {
			return reviews.size();
		}
	}

	/**
	 * 
	 * @return the size of users
	 */
	public static int Usersize() {
		synchronized (usersLock) {
			return users.size();
		}
	}

	/**
	 * 
	 * @return The hashmap restaurants
	 */
	public static HashMap<Integer, String> getRes() {
		synchronized (restaurantLock) {
			return restaurants;
		}
	}

	/**
	 * 
	 * @return the hashmap reviews
	 */
	public static HashMap<Integer, String> getReviews() {
		synchronized (reviewsLock) {
			return reviews;
		}
	}

	/**
	 * 
	 * @return the hashmap users
	 */
	public static HashMap<Integer, String> getUsers() {
		synchronized (usersLock) {
			return users;
		}
	}

	private void createDB(String restaurantJSONfilename, String reviewsJSONfilename, String usersJSONfilename) {
		String line = null;
		int nextChar, i = 0;
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
		// This reads the next character as long as there are still more lines
		// If the char is the start of a new entry, then it puts it into
		// reviews,
		// otherwise it adds it to the entry it should be in
		try {
			String holder;
			String replacer;
			FileReader filereader1 = new FileReader(reviewsJSONfilename);
			BufferedReader reader1 = new BufferedReader(filereader1);
			StringBuilder SB = new StringBuilder();

			// Read the lines
			while ((line = reader1.readLine()) != null) {
				nextChar = reader1.read();
				SB.append((char) nextChar);
				// If the line you just read was an actual entry not a return
				// feed of a review
				if (SB.equals("{") && SB.charAt(0) != (-1)) {
					reviews.put(i, line);
					i++;
				} else {
					// Get what is in the spot, add the new string you found,
					// put it back
					holder = reviews.get(i);
					replacer = holder + line;
					reviews.put(i, replacer);
				}
				SB.deleteCharAt(0);
			}
			reader1.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create users hashmap
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

	private void setUpArrays(String queryString, ArrayList<Restaurant> prices, ArrayList<Restaurant> ratings,
			ArrayList<Restaurant> names, ArrayList<Restaurant> categories, ArrayList<Restaurant> location) {

		// B and D are used for the range of the price and
		// Rating, E is the price of the restaurant in the json file
		int n, b = 0, d = 0, e = 0;
		// Range of price and rating
		int upperbounds, lowerbounds;
		Parser p = new Parser();
		String sample = queryString;

		// Check location
		if (sample.contains("in")) {
			n = sample.indexOf("in"); // Get where the entry was
			sample.substring(n + 2); // Take off everything before it
			sample.split(")", 1); // Take off everything after the close parens

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

		// Check rating needs to be for a range of ints

		if (sample.contains("rating")) {
			n = sample.indexOf("rating");
			sample.substring(n + 8);
			sample.split(")", 1);
			String sample2 = sample;

			String a = sample.substring(0, 0);
			String c = sample2.substring(3);

			// Get the first number in the range
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
			// Get the second number in the range
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

			// Set upperbounds to higher number
			// and lowerbounds to lower number
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

		// Check price, needs to be for a range of ints
		// Same logic as for rating

		if (sample.contains("price")) {
			n = sample.indexOf("price");
			sample.substring(n + 7);
			sample.split(")", 1);
			String sample2 = sample;

			String a = sample.substring(0, 0);
			String c = sample2.substring(3);

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

	}
}
