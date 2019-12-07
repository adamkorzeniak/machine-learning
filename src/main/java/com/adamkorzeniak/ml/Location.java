package com.adamkorzeniak.ml;

public class Location {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(X: %2f, Y: %2f)", x, y);
    }

    public double calculateDistanceTo(Location other) {
        double diffXSquared = Math.pow(other.x - this.x, 2);
        double diffYSquared = Math.pow(other.y - this.y, 2);
        return Math.sqrt(diffXSquared + diffYSquared);
    }
	
	public static Location generateRandom() {
        double x = Math.random() * World.WIDTH;
        double y = Math.random() * World.HEIGHT;
        return new Location(x, y);
	}
}
