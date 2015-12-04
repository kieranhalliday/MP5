package ca.ece.ubc.cpen221.mp5.statlearning;

import java.util.*;
import java.util.List;
import ca.ece.ubc.cpen221.mp5.*;

public class Algorithms {

	/**
	 * Use k-means clustering to compute k clusters for the restaurants in the
	 * database.
	 * 
	 * @param db
	 * @return
	 */
	public static List<Set<Restaurant>> kMeansClustering(int k, RestaurantDB db) {
		//TODO
		List<Point> points = new ArrayList<Point>();
		List<Cluster> cluster = new ArrayList<Cluster>();
		boolean finished = false;
		
		List<Set<Restaurant>> allClusters = new ArrayList<Set<Restaurant>>();
		
		for (int i = 1; i<=k; i++){
			cluster.add(new Cluster(i));
			
		}
		
		for (int j = 0; j<RestaurantDB.restaurants.size();j++){
			points.add(new Point(new Restaurant(RestaurantDB.getRes().get(j))));
		}
		
		while (!finished){
			List<Centroid> lastCentroids = new ArrayList <Centroid>();
			double distance = 0;
			
			for (int n = 0; n<cluster.size();n++){
				cluster.get(n).clear();
			}
			
			for (int h = 0; h< points.size();k++){
				int closestCluster = 0;
				for (int g = 0; g< cluster.size(); g++){
					if(closestCluster == 0 || (points.get(h).distanceToCluster(cluster.get(g).getCentroid())
							<(points.get(h).distanceToCluster(cluster.get(closestCluster).getCentroid())))){
						closestCluster = g;
					}
				}
				points.get(h).attachToCluster(closestCluster);
				cluster.get(closestCluster).addPoint(points.get(h));
			}
			
			for (int m = 0; m<cluster.size(); m++){
				lastCentroids.add(cluster.get(m).getCentroid());
				cluster.get(m).resetCentroid();
				distance += cluster.get(m).distanceClusterToCluster(lastCentroids.get(m));
			}
			if (distance == 0){
				finished = true;
			}
		}
		Set<Restaurant> restaurantCluster = new HashSet <Restaurant>();
		for (int p = 0; p<cluster.size();p++ ){
			restaurantCluster.clear();
			for (int d = 0; d<cluster.get(p).getPoints().size();d++){
				restaurantCluster.add(new Restaurant(Parser.getNamefromID(cluster.get(p).getPoints().get(d).getID())));
			}
			allClusters.add(restaurantCluster);
		}
		
		
		return allClusters;
	}

	public static String convertClustersToJSON(List<Set<Restaurant>> clusters) {
		// TODO: Implement this method
		return null;
	}

	public static MP5Function getPredictor(User u, RestaurantDB db, MP5Function featureFunction) {
		// TODO: Implement this method
		return null;
	}

	public static MP5Function getBestPredictor(User u, RestaurantDB db, List<MP5Function> featureFunctionList) {
		// TODO: Implement this method
		return null;
	}
}