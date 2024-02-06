package helper;

import java.awt.*;

public class Arithmetik {
	public static Point subtract(Point p1, Point p2) {
		int deltaX = p1.x - p2.x;
		int deltaY = p1.y - p2.y;

		return new Point(deltaX, deltaY);
	}

	public static Point add(Point p1, Point p2) {
		int deltaX = p1.x + p2.x;
		int deltaY = p1.y + p2.y;

		return new Point(deltaX, deltaY);
	}

	public static Point multiply(Point p1, Point p2) {
		int deltaX = p1.x * p2.x;
		int deltaY = p1.y * p2.y;

		return new Point(deltaX, deltaY);
	}

	public static Point divide(Point p1, Point p2) {
		int deltaX = p1.x;
		int deltaY = p1.y;

		if(p2.x != 0) {
			deltaX = p1.x / p2.x;
		}

		if(p2.y != 0) {
			deltaY = p1.y / p2.y;
		}

		return new Point(deltaX, deltaY);
	}

	public static double distance(Point p1, Point p2) {
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
}
