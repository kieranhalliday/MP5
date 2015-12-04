package ca.ece.ubc.cpen221.mp5;

import java.util.*;

public class Cluster {

	
	public List<Point> conPoints;
	public Centroid centroid;
	public int id;
	
	public Cluster (int id){
		
		this.id = id;
		this.conPoints = new ArrayList<Point>();
		setCentroid();
	}
	
	public void setCentroid(){
		Random r = new Random();
		double x = -122-r.nextDouble();
		double y = 37+r.nextDouble();
		this.centroid = new Centroid(x,y);
	}
	
	public List<Point> getPoints (){
		return this.conPoints;
	}
	
	public void resetCentroid(){
		double totalX = 0;
		double totalY = 0;
		double x;
		double y;
		
		
		for (int i = 0; i<conPoints.size(); i++){
			totalX += conPoints.get(i).getX();
			totalY += conPoints.get(i).getY();
		}
		
		x = totalX/(conPoints.size());
		y = totalY/(conPoints.size());
		
		this.centroid = new Centroid(x,y);
	
	}
	
	public void addPoint (Point point){
		conPoints.add(point);
	}
	
	public void setPoints (List<Point> conPoints){
		this.conPoints = conPoints;
	}
	
	public Centroid getCentroid(){
		return this.centroid;
	}
	
	public int getID(){
		return id;
	}
	
	public void clear(){
		conPoints.clear();
	}
	
	public double distanceClusterToCluster ( Centroid centroid){
		return Math.sqrt((Math.pow(this.centroid.getX()-centroid.getX(), 2))+(Math.pow(this.centroid.getY()-centroid.getY(), 2)));
	}
	
	
}
