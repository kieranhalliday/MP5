package ca.ece.ubc.cpen221.mp5;

/**
 * @Abstraction_Function: Each instance of this class represents a restaurant
 * @Rep_invariant: Each restaurant must have a name of type string
 */

public class Restaurant {

	@SuppressWarnings("unused")
	private String name;

	public Restaurant(String string) {
		this.name = string;
	}
}
