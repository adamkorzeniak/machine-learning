package com.adamkorzeniak.ml;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class World {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 800;

	private static int locationAmount;
    private static List<Location> locations;

    private World() {}

    public static void initializeWorld(int locationAmount) {
		World.locationAmount = locationAmount;
        locations = IntStream.range(0, locationAmount).boxed().map(i -> Location.generateRandom()).collect(Collectors.toList());
    }
	
	public static int getLocationAmount() {
		return locationAmount;
	}
	
	public static List<Location> getLocations() {
		return locations;
	}
}
