package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class Longitude implements MP5Function {

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double longitude;	
		
		longitude = Parser.getLongitude(yelpRestaurant.getName());
		
		
		return longitude;
	}

}
