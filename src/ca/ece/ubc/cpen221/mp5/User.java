package ca.ece.ubc.cpen221.mp5;

/**
 * @Abstraction_Function: Each instance of this class represents a Yelp user
 * @Rep_invariant: A string as a username
 */

public class User {
	
	@SuppressWarnings("unused")
	private String username;

	public User(String username){
		this.username=username;
	}

}
