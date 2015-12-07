package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class PriceRange implements MP5Function{

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double price = 0.0;
		String priceS;
		
		priceS = Parser.getPrice(yelpRestaurant.getName());
		
		if (priceS.contains("1")) {
			price = 1.0;
		} else if (priceS.contains("2")) {
			price = 2.0;
		} else if (priceS.contains("3")) {
			price = 3.5;
		} else if (priceS.contains("4")) {
			price = 4.0;
		}
		
		
		return price;
	}

}

