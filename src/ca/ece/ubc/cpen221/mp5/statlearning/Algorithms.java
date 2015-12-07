package ca.ece.ubc.cpen221.mp5.statlearning;

import java.util.*;

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
		String allClusters;
		String cluster;
		String Point;
		
		for (int i =0; i<clusters.size();i++){
			for (int j =0; j< clusters.get(i).size();j++){
				Point = "{'x': " + parseString(Parser.getLongitude(( clusters.get(i)).get(j)/*WHY DID WE HAVE TO USE A SET AND NOT A LIST >:(*/.getName()));
			}
		}
		
		return null;
		
		
	}

	public static MP5Function getPredictor(User u, RestaurantDB db, MP5Function featureFunction) {
		
		MP5Function LinearRegressionFunction;
		
		
		
		List<Double> dep = new ArrayList<Double>();
		List<Double> indep = new ArrayList <Double>();
		
		for(int i =0; i<RestaurantDB.Reviewsize(); i++){
			if(RestaurantDB.getReviews().get(i).contains(u.getName())){
				dep.add(Parser.getStars())
			}
		}
		
		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumXY = 0;
		double b;
		double a;
		double r2;
		
	
		for (int i = 0; i< dep.size(); i++){
			sumX += dep.get(i);
			sumY += indep.get(i);
			sumXX += (dep.get(i)*dep.get(i));
			sumXY += (dep.get(i)*indep.get(i));
		}
		
		b = (((dep.size()*sumXY)-(sumX*sumY))/((dep.size()*sumXX)-(sumX*sumX)));
		
	
		
		
			
		
		
		return LinearRegressionFunction.LinearRegressionFunction(a,b,r2,featureFunction);
	}

	public static MP5Function getBestPredictor(User u, RestaurantDB db, List<MP5Function> featureFunctionList) {
		// TODO: Implement this method
		return null;
	}

	public double LeastSquareSlope (List<double> dep, List<double> indep){
		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumXY = 0;
		double slope;
		
	
		for (int i = 0; i< dep.size(); i++){
			sumX += dep.get(i);
			sumY += indep.get(i);
			sumXX += (dep.get(i)*dep.get(i));
			sumXY += (dep.get(i)*indep.get(i));
		}
		
		slope = (((dep.size()*sumXY)-(sumX*sumY))/((dep.size()*sumXX)-(sumX*sumX)));
		return slope;
	
	}
	public double LeastSquareIntercept (List<double> dep, List<double> indep, double slope){
		double sumX = 0;
		double sumY = 0;
		double intercept;
	
		for (int i = 0; i< dep.size(); i++){
			sumX += dep.get(i);
			sumY += indep.get(i);
		}
		
		intercept = (sumY-(slope*sumX))/(dep.size())
		return intercept;		
				
	}
	
	public double plugIn (double slope, double intercept, double x){
		double y;
		
		y = intercept + (slope*x);
		
		return y;
	}
}


	