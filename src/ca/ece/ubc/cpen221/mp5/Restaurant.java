package ca.ece.ubc.cpen221.mp5;

// TODO: Use this class to represent a restaurant.
// State the rep invariant and abs

/**
 * @Abstraction_Function: Each instance of this class represents a restaurant
 * @Rep_invariant: Each restaurant must have a name of type string
 */

public class Restaurant {

	@SuppressWarnings("unused")
	private String name;

	public Restaurant(String strings) {
		this.name = strings;
	}
}
