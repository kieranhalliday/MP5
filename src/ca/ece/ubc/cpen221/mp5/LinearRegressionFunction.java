package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.statlearning.MP5Function;

public class LinearRegressionFunction implements MP5Function {
	double a;
	double b;
	double r2;
	MP5Function featureFunction;
	
	public LinearRegressionFunction (double a, double b, double r2, MP5Function featureFunction ){
		this.a = a;
		this.b = b;
		this.r2 = r2;
		this.featureFunction = featureFunction;
	}
	
	@Override
	public double f(Restaurant yelpRestaurant, RestaurantDB db) {
		double prediction;
		double x;
		
		x = featureFunction.f(yelpRestaurant, db);
		prediction = a+b*x;
		
		if (prediction>5){
			prediction = 5;
		}else if (prediction <0){
			prediction = 0;
		}
		
		return prediction;
	}
}


	