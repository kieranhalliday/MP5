package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class Latitude implements MP5Function {

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double latitude;	
		
		latitude = Parser.getLongitude(yelpRestaurant.getName());
		
		
		return latitude;
	}

}
