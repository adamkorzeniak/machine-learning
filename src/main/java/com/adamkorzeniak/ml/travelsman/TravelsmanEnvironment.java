package com.adamkorzeniak.ml.travelsman;

import com.adamkorzeniak.ml.FrameworkEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TravelsmanEnvironment extends FrameworkEnvironment {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 800;

    private final int amountOfCities;
    private static final double MUTABILITY_FACTOR = 0.01;

    private static List<Location> locations;

    public double getMutabilityFactor() {
        return MUTABILITY_FACTOR;
    }

    public TravelsmanEnvironment(int amountOfCities) {
        super(  () -> TravelsmanAgent.createRandom(amountOfCities),
                (parents) -> TravelsmanAgent.createChild(parents, amountOfCities));
        this.amountOfCities = amountOfCities;
        locations = IntStream.range(0, amountOfCities).boxed()
                        .map(i -> Location.generateRandom()).
                        collect(Collectors.toList());
    }

    public int getAmountOfCities() {
        return amountOfCities;
    }

    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }
}
