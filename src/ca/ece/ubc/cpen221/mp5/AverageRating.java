package ca.ece.ubc.cpen221.mp5;

import java.util.*;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class AverageRating implements MP5Function{

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double rating = 0.0;
		String ratingS;
		
		ratingS = Parser.getRating(yelpRestaurant.getName());
		
		if (ratingS.contains("1.0")) {
			rating = 1.0;
		} else if (ratingS.contains("1.5")) {
			rating = 1.5;
		} else if (ratingS.contains("2.0")) {
			rating = 2.0;
		} else if (ratingS.contains("2.5")) {
			rating = 2.5;
		} else if (ratingS.contains("3.0")) {
			rating = 3.5;
		} else if (ratingS.contains("4.0")) {
			rating = 4.0;
		} else if (ratingS.contains("4.5")) {
			rating = 4.5;
		} else if (ratingS.contains("5.0")) {
			rating = 5.0;
		} else if (ratingS.contains("0.5")) {
			rating = 0.5;
		} else if (ratingS.contains("0.0")) {
			rating = 0.0;
		}
		
		
		
		return rating;
	}

}
