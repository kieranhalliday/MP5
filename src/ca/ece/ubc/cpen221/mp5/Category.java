package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class Category implements MP5Function{

	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double catVal = 0.0;
		String category;
		
		category = Parser.getCategory(yelpRestaurant.getName());
		
		if (category.contains("Thai")) {
			catVal = 1;
		} else if (category.contains("Chinese")) {
			catVal = 2;
		} else if (category.contains("Mexican")) {
			catVal = 3;
		} else if (category.contains("Vietnamese")) {
			catVal = 4;
		} else if (category.contains("Indian")) {
			catVal = 5;
		} else if (category.contains("Greek")) {
			catVal = 6;
		} else if (category.contains("Italian")) {
			catVal = 7;
		} else if (category.contains("Korean")) {
			catVal = 8;
		} else if (category.contains("Persian/Iranian")) {
			catVal = 9;
		} else if (category.contains("Japanese")) {
			catVal = 10;
		} else if (category.contains("Brazillian")) {
			catVal = 11;
		} else if (category.contains("Mediteranean")) {
			catVal = 12;
		} else if (category.contains("Turkish")) {
			catVal = 13;
		} else if (category.contains("Vegan")) {
			catVal = 14;
		} else if (category.contains("Middle Eastern")) {
			catVal = 15;
		} else if (category.contains("Pakistani")) {
			catVal = 16;
		} else if (category.contains("Halal")) {
			catVal = 17;
		} else if (category.contains("African")) {
			catVal = 18;
		} else if (category.contains("Carribean")) {
			catVal = 19;
		} else if (category.contains("Ethnic Food")) {
			catVal = 20;
		} else if (category.contains("American (Traditional)")) {
			catVal = 21;
		} else if (category.contains("American (New)")) {
			catVal = 22;
		} else if (category.contains("Desserts")) {
			catVal = 23;
		} else if (category.contains("Fast Food")) {
			catVal = 24;
		} else if (category.contains("Breakfast & Brunch")) {
			catVal = 25;
		} else if (category.contains("Asian Fusion")) {
			catVal = 26;
		} else if (category.contains("Coffee & Tea")) {
			catVal = 27;
		} else if (category.contains("Sandwiches")) {
			catVal = 28;
		} else if (category.contains("Hot Dogs")) {
			catVal = 29;
		} else if (category.contains("Fish & Chips")) {
			catVal = 30;
		} else if (category.contains("Sushi Bars")) {
			catVal = 31;
		} else if (category.contains("Juice Bars & Smoothies")) {
			catVal = 32;
		} else if (category.contains("Burgers")) {
			catVal = 33;
		} else if (category.contains("Beer, Wine & Spirits")) {
			catVal = 34;
		} else if (category.contains("Chicken Wings")) {
			catVal = 35;
		} else if (category.contains("Pizza")) {
			catVal = 36;
		} else if (category.contains("Dim Sum")) {
			catVal = 37;
		} else if (category.contains("Soup")) {
			catVal = 38;
		} else if (category.contains("Music & DVDs")) {
			catVal = 39;
		} else if (category.contains("Books, Mags, Music and Video")) {
			catVal = 40;
		} else if (category.contains("Cafes")) {
			catVal = 41;
		} else if (category.contains("Bars")) {
			catVal = 42;
		} else if (category.contains("NightLife")) {
			catVal = 43;
		} else if (category.contains("Ceperies")) {
			catVal = 44;
		} else if (category.contains("Delis")) {
			catVal = 45;
		} else if (category.contains("Caterers")) {
			catVal = 46;
		} else if (category.contains("Street Vendors")) {
			catVal = 47;
		} else if (category.contains("Food Stands")) {
			catVal = 48;
		} else if (category.contains("Bakeries")) {
			catVal = 49;
		} else if (category.contains("Convenience Stores")) {
			catVal = 50;
		} else if (category.contains("Grocery")) {
			catVal = 51;
		} else if (category.contains("Restaurant")) {
			catVal = 52;
		} else if (category.contains("Food")) {
			catVal = 53;
		}

		
		
		
		return catVal;
	}

}

