package ca.ece.ubc.cpen221.mp5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;

// TODO: This class represents the Restaurant Database.
// TODO: Set method on bottom, figure out how to parse json

// Define the internal representation and
// state the rep invariant and the abstraction function.

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
		// seperation

		String line = null;
		int i = 0;

		HashMap<Integer, String> restaurants = new HashMap<Integer, String>();
		HashMap<Integer, String> reviews = new HashMap<Integer, String>();
		HashMap<Integer, String> users = new HashMap<Integer, String>();

		RestaurantDB.restaurants = restaurants;
		RestaurantDB.reviews = reviews;
		RestaurantDB.users = users;

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
	/*
	 * public Set<Restaurant> query(String queryString) throws
	 * IllegalArgumentException { // TODO: Implement this method // Write specs,
	 * etc. // Parse query // compare to restaurants hashmap //Assuming
	 * queryString is a valid argument
	 * 
	 * String sample = queryString; Set<Restaurant> set = new
	 * HashSet<Restaurant>(); String name;
	 * 
	 * //Check location
	 * 
	 * if(sample.contains("in")){ //Get where the entry was int n=
	 * sample.indexOf("in"); //Take off everything before it
	 * sample.substring(n+2); //Take off everything after the close parens...
	 * end of in query sample.split(")", 1);
	 * 
	 * for(int i=0;i<restaurants.size();i++){
	 * if(restaurants.get(i).contains(sample)){ String str = restaurants.get(i);
	 * JSONObject obj = new JSONObject(); //name = ob
	 * 
	 * 
	 * 
	 * int j=restaurants.get(i).indexOf("name");
	 * restaurants.get(i).substring(j); restaurants.get(i).split(",", 1);
	 * Restaurant temp = new Restaurant(restaurants.get(i).split(",", 1));
	 * set.add(temp); } } }
	 * 
	 * 
	 * 
	 * return null; }
	 */
	public static void addRestaurant(String request) {
		restaurants.put(restaurants.size(), request);
	}

	public static void addUser(String request) {
		users.put(restaurants.size(), request);

	}

	public static void addReview(String request) {
		reviews.put(restaurants.size(), request);

	}

	public static String getRestaurant(String ID) {
		for (int i = 0; i < restaurants.size(); i++) {
			if (restaurants.get(i).contains(ID)) {
				return restaurants.get(i);
			}
		}
		return ("Restaurant Not Found");
	}

	public static String getReview(String name) {

		String ID = null;
		int start;

		for (int i = 0; i < restaurants.size(); i++) {
			if (restaurants.get(i).contains(name)) {
				ID = restaurants.get(i);
				start = ID.indexOf(name);
				ID.substring(start, name.length());
			}
		}
		for (int j = 0; j < reviews.size(); j++) {
			int randomnum = randInt(1, 0);
			if (reviews.get(j).contains(ID) && randomnum > 0.66) {
				return reviews.get(j);
			}

			// Try twice for good measure
			for (int k = 0; j < reviews.size(); j++) {
				int randomnum1 = randInt(1, 0);
				if (reviews.get(k).contains(ID) && randomnum1 > 0.66) {
					return reviews.get(k);
				}

			}
		}
		return "Review not Found";
	}

	public static int randInt(int min, int max) {

		Random rand = null;
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
