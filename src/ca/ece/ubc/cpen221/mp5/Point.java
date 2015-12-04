package ca.ece.ubc.cpen221.mp5;




public class Point {

	private double x = 0;
	private double y = 0;
	private String ID;
	private int cluster = 0;
	
	public Point(Restaurant restaurant){
		
		setX(restaurant);
		setY(restaurant);
		setID(restaurant);
	}
	
	public void setX(Restaurant restaurant){
		this.x=Parser.getLongitude(restaurant.getName());
	}
	
	public void setY(Restaurant restaurant){
		this.y = Parser.getLatitude(restaurant.getName());;
	}
	
	public void setID(Restaurant restaurant){
		this.ID = Parser.getID(restaurant.getName());
	}
	
	public double getX (){
		return this.x;
	}
	
	public double getY (){
		return this.y;
	}
	
	public String getID(){
		return this.ID;
	}
	
	public void attachToCluster (int clusterID){
		this.cluster = clusterID;
	}
	
	public int getCLuster(){
		return this.cluster;
	}
	
	public double distanceToCluster ( Centroid centroid){
		return Math.sqrt((Math.pow(this.getX()-centroid.getX(), 2))+(Math.pow(this.getY()-centroid.getY(), 2)));
	}
	
	
	
}
