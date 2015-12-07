package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Rep Invariant: A Hashmap of Integers and Strings, the Strings must be in JSON
 * format are valid values for each database
 * 
 * Abstraction Function: Each Hashmap represents a numbered lists of either all
 * the restaurants, reviews or users
 * 
 * Thread Safety Argument: Each hashmap has a lock associated with it. Any
 * function that calls a mutator method must attain the lock before mutating the
 * DB (represented by a hashmap)
 */

public class RestaurantDB {
	public static HashMap<Integer, String> restaurants;
	public static HashMap<Integer, String> reviews;
	public static HashMap<Integer, String> users;
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
		// Currently each Map has one entry per every JSON entry, no
		// seperation of each field

		HashMap<Integer, String> restaurants = new HashMap<Integer, String>();
		HashMap<Integer, String> reviews = new HashMap<Integer, String>();
		HashMap<Integer, String> users = new HashMap<Integer, String>();

		RestaurantDB.restaurants = restaurants;
		RestaurantDB.reviews = reviews;
		RestaurantDB.users = users;
		System.out.println("Starting to create the DB");

		createDB(restaurantJSONfilename, reviewsJSONfilename, usersJSONfilename);
	}

	/**
	 * @param queryString,
	 *            type of restaurant you are searching for
	 * @return set, the set of all restaurants that match the search
	 * @throws IllegalArgumentException
	 *             if queryString is malformed
	 */

	// Cannot handle || yet
	// To handle || and &&, go through string first and assign a 0 for AND and a
	// 1 for OR
	// Then get results based on if they are AND or OR

	public Set<Restaurant> query(String queryString) throws IllegalArgumentException {

		StringBuilder stringBuilder = new StringBuilder();

		ArrayList<Integer> andOrlist = new ArrayList<Integer>();
		ArrayList<String> commands = new ArrayList<String>();

		// These arraylists hold the restaurants that sastify at least
		// one area of the query
		ArrayList<Restaurant> location = new ArrayList<Restaurant>();
		ArrayList<Restaurant> categories = new ArrayList<Restaurant>();
		ArrayList<Restaurant> names = new ArrayList<Restaurant>();
		ArrayList<Restaurant> prices = new ArrayList<Restaurant>();
		ArrayList<Restaurant> ratings = new ArrayList<Restaurant>();

		Set<Restaurant> results = new HashSet<Restaurant>();

		setUpArrays(queryString, prices, ratings, names, categories, location);

		setUpandOrlist(stringBuilder, queryString, andOrlist);

		setUpCommandslist(queryString, queryString, stringBuilder, commands);

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
	 * Objective of this method is to take the lists of restaurants that satisfy
	 * at least one portion of the query and figure out how to combine them in a
	 * way that satifies the client's request
	 * 
	 * @The_way_it_works: First, you have an arraylist of all the AND's and OR's
	 *                    from the original query string in the order they
	 *                    appeared in the string. Second, You have an arraylist
	 *                    of all the requirements of the restaurant the client
	 *                    is searching for with one entry per paramter. These
	 *                    are passed as parameters to the function in the andOR
	 *                    arraylist and the commands arraylist
	 * 
	 * 
	 *                    Then you go through the list, if index n of the AND/OR
	 *                    array represents an AND, then you automatically add
	 *                    any restaurants from the five paramter fulling arrays
	 *                    passed to this function the fulfill query n in the
	 *                    commands list.
	 * 
	 * 
	 *                    If index n of the AND/OR array represents an OR,then
	 *                    you add any restaurants from the five paramter fulling
	 *                    arrays passed to this function the fulfill query n and
	 *                    n+1 in the commands list.
	 * 
	 *                    Then you add the last element of the commands list to
	 *                    the results because it will always be a valid option
	 *                    no matter if it is AND or OR infront of it.
	 * 
	 *                    EX: A and B and C or D and E
	 * 
	 *                    -AND/OR [0] = AND --> Check all query that fulfill A
	 *                    and add them to the results list.
	 * 
	 *                    -AND/OR [1] = AND --> Check all query that fulfill B
	 *                    and add them to the results list.
	 * 
	 *                    -AND/OR [2] = OR --> Check all query that fulfill C
	 *                    and D and add them to the results list.
	 * 
	 *                    -AND/OR [3] = AND --> Check all query that fulfill D
	 *                    and add them to the results list. -Check all query
	 *                    that fulfill E and add them to the results list.
	 *                    because it is the last element
	 * 
	 *                    If A was in("Chinatown"); You would see in Chinatown
	 *                    and B etc... Then you go to the location list, which
	 *                    holds all the restaurants in Chinatown and add them
	 *                    all because any restaurant the fulfill the query must
	 *                    be in Chinatown.
	 * 
	 *                    You now have a list with many replicas of the same
	 *                    restaurant. That is a good thing because in order for
	 *                    a resutaurant to satisfy all the different parts of
	 *                    the query it must appear one more time than the number
	 *                    of AND's in the statement. Go through the list and
	 *                    take out all elements that do not appear at least once
	 *                    more than the number of AND's in the query.
	 * 
	 *                    Then to satisfy the OR statements, a restaurant must
	 *                    appear at least once per set of consecutive OR's.
	 *                    Remove all restaurants that don't appear at least once
	 *                    per set of consecutive ORs.
	 * 
	 * 
	 *                    The contains method for string comparison is safe to
	 *                    use in this entire program because the DB is
	 *                    constructed using the entire json string from the
	 *                    .json file passed as a parameter so it is searching
	 *                    for the entire string which can only ever appear once
	 *                    in the database assuming no restaurant has two entries.
	 * 
	 * 
	 * 
	 *
	 * @param Five
	 *            array lists holding the restaurants that satisfy at least one
	 *            portion of the query
	 * @param results
	 *            set to be returned to the client
	 */

	public void addtoArray(ArrayList<Restaurant> prices, ArrayList<Restaurant> ratings, ArrayList<Restaurant> names,
			ArrayList<Restaurant> categories, ArrayList<Restaurant> location, Set<Restaurant> results,
			ArrayList<Integer> andOR, ArrayList<String> commands) {

		// Add results to resultsCopy for easier manipulation
		ArrayList<Restaurant> resultsCopy = new ArrayList<Restaurant>();

		int counter;
		// 1 is OR

		// Figure out which commands are ANDed
		for (counter = 0; counter < prices.size(); counter++) {
			if (andOR.get(counter) == 0) {
				if (commands.get(counter).contains("in")) {
					if (commands.get(counter).contains("price")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < location.size(); counter1++) {
							if (location.contains(prices.get(counter1))) {
								resultsCopy.add(prices.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("rating")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < location.size(); counter1++) {
							if (location.contains(ratings.get(counter1))) {
								resultsCopy.add(ratings.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("name")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < location.size(); counter1++) {
							if (location.contains(names.get(counter1))) {
								resultsCopy.add(names.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("category")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < location.size(); counter1++) {
							if (location.contains(categories.get(counter1))) {
								resultsCopy.add(categories.get(counter1));
							}
						}
					}

					// BREAK

				} else if (commands.get(counter).contains("price")) {
					if (commands.get(counter).contains("in")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < prices.size(); counter1++) {
							if (prices.contains(location.get(counter1))) {
								resultsCopy.add(location.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("rating")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < prices.size(); counter1++) {
							if (prices.contains(ratings.get(counter1))) {
								resultsCopy.add(ratings.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("name")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < prices.size(); counter1++) {
							if (prices.contains(names.get(counter1))) {
								resultsCopy.add(names.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("category")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < prices.size(); counter1++) {
							if (prices.contains(categories.get(counter1))) {
								resultsCopy.add(categories.get(counter1));
							}
						}
					}

					// BREAK

				} else if (commands.get(counter).contains("rating")) {
					if (commands.get(counter).contains("in")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < ratings.size(); counter1++) {
							if (ratings.contains(location.get(counter1))) {
								resultsCopy.add(location.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("price")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < ratings.size(); counter1++) {
							if (ratings.contains(prices.get(counter1))) {
								resultsCopy.add(prices.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("name")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < ratings.size(); counter1++) {
							if (ratings.contains(names.get(counter1))) {
								resultsCopy.add(names.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("category")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < ratings.size(); counter1++) {
							if (ratings.contains(categories.get(counter1))) {
								resultsCopy.add(categories.get(counter1));
							}
						}
					}
				}

				// BREAK

				else if (commands.get(counter).contains("name")) {
					if (commands.get(counter).contains("in")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < names.size(); counter1++) {
							if (names.contains(location.get(counter1))) {
								resultsCopy.add(location.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("price")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < names.size(); counter1++) {
							if (names.contains(prices.get(counter1))) {
								resultsCopy.add(prices.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("rating")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < names.size(); counter1++) {
							if (names.contains(ratings.get(counter1))) {
								resultsCopy.add(ratings.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("category")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < names.size(); counter1++) {
							if (names.contains(categories.get(counter1))) {
								resultsCopy.add(categories.get(counter1));
							}
						}
					}
				}

				// BREAK

				else if (commands.get(counter).contains("category")) {
					if (commands.get(counter).contains("in")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < categories.size(); counter1++) {
							if (categories.contains(location.get(counter1))) {
								resultsCopy.add(location.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("price")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < categories.size(); counter1++) {
							if (categories.contains(prices.get(counter1))) {
								resultsCopy.add(prices.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("rating")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < categories.size(); counter1++) {
							if (categories.contains(ratings.get(counter1))) {
								resultsCopy.add(ratings.get(counter1));
							}
						}
					}
					if (commands.get(counter).contains("name")) {
						// Cycle through and get res in both
						for (int counter1 = 0; counter1 < categories.size(); counter1++) {
							if (categories.contains(names.get(counter1))) {
								resultsCopy.add(names.get(counter1));
							}
						}
					}
				}
				// If it is OR add all results that matched the parameter to the
				// resultsCopy list because they will be valid
			} else if (andOR.get(counter) == 1) {
				if (commands.get(counter).contains("in")) {
					for (int counter2 = 0; counter2 < location.size(); counter2++) {
						resultsCopy.add(location.get(counter2));
					}
				} else if (commands.get(counter).contains("rating")) {
					for (int counter2 = 0; counter2 < ratings.size(); counter2++) {
						resultsCopy.add(ratings.get(counter2));
					}
				} else if (commands.get(counter).contains("name")) {
					for (int counter2 = 0; counter2 < names.size(); counter2++) {
						resultsCopy.add(names.get(counter2));
					}
				} else if (commands.get(counter).contains("price")) {
					for (int counter2 = 0; counter2 < prices.size(); counter2++) {
						resultsCopy.add(prices.get(counter2));
					}
				} else if (commands.get(counter).contains("category")) {
					for (int counter2 = 0; counter2 < categories.size(); counter2++) {
						resultsCopy.add(categories.get(counter2));
					}
				}
				if (commands.get(counter + 1).contains("in")) {
					for (int counter2 = 0; counter2 < location.size(); counter2++) {
						resultsCopy.add(location.get(counter2));
					}
				} else if (commands.get(counter + 1).contains("rating")) {
					for (int counter2 = 0; counter2 < ratings.size(); counter2++) {
						resultsCopy.add(ratings.get(counter2));
					}
				} else if (commands.get(counter + 1).contains("name")) {
					for (int counter2 = 0; counter2 < names.size(); counter2++) {
						resultsCopy.add(names.get(counter2));
					}
				} else if (commands.get(counter + 1).contains("price")) {
					for (int counter2 = 0; counter2 < prices.size(); counter2++) {
						resultsCopy.add(prices.get(counter2));
					}
				} else if (commands.get(counter + 1).contains("category")) {
					for (int counter2 = 0; counter2 < categories.size(); counter2++) {
						resultsCopy.add(categories.get(counter2));
					}
				}

			}
		}

		// Add any restaurants that match the last part of the query
		if (commands.get(commands.size() - 1).contains("in")) {
			for (int counter2 = 0; counter2 < location.size(); counter2++) {
				resultsCopy.add(location.get(counter2));
			}
		} else if (commands.get(commands.size() - 1).contains("rating")) {
			for (int counter2 = 0; counter2 < ratings.size(); counter2++) {
				resultsCopy.add(ratings.get(counter2));
			}
		} else if (commands.get(commands.size() - 1).contains("name")) {
			for (int counter2 = 0; counter2 < names.size(); counter2++) {
				resultsCopy.add(names.get(counter2));
			}
		} else if (commands.get(commands.size() - 1).contains("price")) {
			for (int counter2 = 0; counter2 < prices.size(); counter2++) {
				resultsCopy.add(prices.get(counter2));
			}
		} else if (commands.get(commands.size() - 1).contains("category")) {
			for (int counter2 = 0; counter2 < categories.size(); counter2++) {
				resultsCopy.add(categories.get(counter2));
			}
		}

		// Count number of ANDS
		int numANDs = 0;
		for (int counter3 = 0; counter3 < andOR.size(); counter3++) {
			if (andOR.get(counter3) == 0) {
				numANDs++;
			}
		}

		// Remove any element that doesn't appear at least as many times as the
		// # ANDS.
		int numTimesAppears = 0;
		int n = 0;
		Restaurant R = new Restaurant("Sample");
		for (int counter4 = 0; counter4 < resultsCopy.size(); counter4++) {

			R = resultsCopy.get(n);

			if (resultsCopy.get(counter4) == resultsCopy.get(n)) {
				numTimesAppears++;
			}
			if (numTimesAppears <= numANDs) {
				for (int counter5 = 0; counter5 < numTimesAppears; counter5++) {
					resultsCopy.remove(R);
				}

			}
			n++;
		}

		// Remove any element that does not appear at least once per set of
		// consecutive OR's
		// Number of sets of OR can be found by finding the first OR and
		// continuing through the list until you find an and,
		// then incrementing number of sets of OR and repeating until you reach
		// the end of the array

		int numsetsofOR = 0;

		for (int counter6 = 0; counter6 < andOR.size(); counter6++) {
			// Go until you find an OR
			if (andOR.get(counter6) == 1) {
				// Continue through array until you find an AND
				counter6++;
				while (andOR.get(counter6) == 1 && counter6 < andOR.size()) {
					counter6++;
				}
				numsetsofOR++;
			}
		}

		// Remove all restaurants that appear at least as many times as the
		// number of sets of OR's
		numTimesAppears = 0;
		n = 0;
		Restaurant R1 = new Restaurant("Sample");
		for (int counter4 = 0; counter4 < resultsCopy.size(); counter4++) {

			R1 = resultsCopy.get(n);

			if (resultsCopy.get(counter4) == resultsCopy.get(n)) {
				numTimesAppears++;
			}
			if (numTimesAppears <= numsetsofOR) {
				for (int counter5 = 0; counter5 < numTimesAppears; counter5++) {
					resultsCopy.remove(R1);
				}

			}
			n++;
		}

		// Copy resultsCopy arraylist into results and return results because it
		// needs to be a set
		for (int counter8 = 0; counter8 < resultsCopy.size(); counter8++) {
			results.add(resultsCopy.get(counter8));
		}
	}

	/**
	 * @modifies RestaurantDB.restaurants by adding a new restaurant
	 * @param request
	 */
	public static void addRestaurant(String request) {
		synchronized (restaurantLock) {
			restaurants.put(restaurants.size(), "name:" + request + ", " + "'categories'");
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
			reviews.put(reviews.size(), "text" + ":" + request);
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
			if (reviews.get(j).contains(ID) && randomnum > 0.85) {
				start = ID.indexOf("text:");
				end = ID.indexOf("stars");
				review = ID.substring(start + 9, end - 4);

				return review;
			}
		}

		// Try twice for good measure, easier to get it
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
		int i = 0;
		// Create restaurant hashmap
		File res = new File("C:/School/CPEN221/Workspace/mp5-fall2015/data/restaurants.json");
		File rev = new File("C:/School/CPEN221/Workspace/mp5-fall2015/data/reviews.json");
		File use = new File("C:/School/CPEN221/Workspace/mp5-fall2015/data/users.json");

		try {
			FileReader filereader = new FileReader(res);
			BufferedReader reader = new BufferedReader(filereader);

			while ((line = reader.readLine()) != null) {
				restaurants.put(i, line);
				i++;
			}
			reader.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Successfully made the restaurants DB");

		// Create users hashmap
		try {
			FileReader filereader2 = new FileReader(use);
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
		System.out.println("Successfully made the users DB");

		// Create reviews hashmap
		// This reads the next character as long as there are still more lines
		// If the char is the start of a new entry, then it puts it into
		// reviews,
		// otherwise it adds it to the entry it should be in
		try {
			FileReader filereader1 = new FileReader(rev);
			BufferedReader reader1 = new BufferedReader(filereader1);
			String stringtoadd = " ";

			// Read the lines
			while ((line = reader1.readLine()) != null) {
				// add line to the string to add
				stringtoadd.concat(line);
				// if the line is actually a new review
				if (line.contains("business_id")) {
					// take off the new line and add the old line to the reviews
					// DB
					reviews.put(i, stringtoadd.replace(line, " "));
					i++;
					stringtoadd = line;
				}
			}
			reader1.close();
			i = 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Successfully made the reviews DB");

	}

	private void setUpArrays(String queryString, ArrayList<Restaurant> prices, ArrayList<Restaurant> ratings,
			ArrayList<Restaurant> names, ArrayList<Restaurant> categories, ArrayList<Restaurant> location) {

		// B and D are used for the range of the price and
		// Rating, E is the price of the restaurant in the json file
		int n, b = 0, d = 0, e = 0;
		// Range of price and rating
		int upperbounds, lowerbounds;
		Parser p = new Parser();
		String sample1, sample = queryString;

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

		do {
			if (sample.contains("category")) {
				sample1 = sample;
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
				sample1.substring(n + 10);
				sample = sample1;
			}
		} while (sample.contains("category"));
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

	// Runs the server
	public static void main(String[] args) {
		try {
			RestaurantDBServer server = new RestaurantDBServer(4951, "restaurants.json", "reviews.json", "users.json");
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
