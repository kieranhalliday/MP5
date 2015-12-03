package ca.ece.ubc.cpen221.mp5;

// TODO: Use this class to represent a restaurant.
// State the rep invariant and abs

public class Restaurant {

	@SuppressWarnings("unused")
	private String name;

	// Abstraction Function: Each instance of this class represents a restaurant
	// Rep invariant: Each restaurant must have a name of type string
	
	public Restaurant(String strings){
		this.name=strings;
	}
	
	public String getName(){
		return this.name;
	}
}
