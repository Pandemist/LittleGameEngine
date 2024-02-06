package helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class DoublePoint {
	private double x;
	private double y;

	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}

public class PairwiseLinearFunction {
	private List<DoublePoint> points = new ArrayList<>();
	private double cachedX = Double.NaN;
	private double cachedY = Double.NaN;

	public void addSegment(double x, double y) {
		points.add(new DoublePoint(x, y));
	}

	public double evaluate(double x) {
		if(points.isEmpty()) {
			return 0;
		}

		if(x == cachedX) {
			return cachedY;
		}

		if(x <= points.get(0).getX()) {
			cacheResult(points.get(0).getY(), x);
			return cachedY;
		}

		if(x <= points.get(0).getX()) {
			return points.get(0).getY();
		}

		for(int i = 1; i < points.size(); i++) {
			DoublePoint prevPoint = points.get(i-1);
			DoublePoint currPoint = points.get(i);

			if(x <= currPoint.getX()) {
				double slope = (currPoint.getY() - prevPoint.getY()) / (currPoint.getX() - prevPoint.getX());
				double yIntercept = prevPoint.getY() - slope * prevPoint.getX();
				double result = slope * x + yIntercept;
				cacheResult(result, x);
				return cachedY;
			}
		}

		cacheResult(points.get(points.size() - 1).getY(), x);
		return cachedY;
	}

	private void cacheResult(double result, double x) {
		cachedY = result;
		cachedX = x;
	}
}
