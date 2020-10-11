package com.adamkorzeniak.ml.travelsman;

import com.adamkorzeniak.ml.FrameworkEnvironment;
import com.adamkorzeniak.ml.FrameworkSimulation;

import java.util.function.Function;

public class TravelsmanSimulation extends FrameworkSimulation {
    public TravelsmanSimulation(FrameworkEnvironment environment) {
        super(environment);
    }

    private static final int MIN_GENERATION = 1000;
    private static final int MAX_GENERATION_STAGNATION = 200;

    @Override
    public boolean hasNextGeneration() {
        if (getCurrentGeneration() <= MIN_GENERATION ) {
            return true;
        }
        Function<Double, Boolean> isStagnant = i -> i.compareTo(1.0) <= 0;
        boolean isOverallFitnessStagnation =
                getOverallFitnessImprovement(MAX_GENERATION_STAGNATION)
                        .map(isStagnant)
                        .orElse(false);
        boolean isBestFitnessStagnation =
                getBestFitnessImprovement(MAX_GENERATION_STAGNATION)
                        .map(isStagnant)
                        .orElse(false);
        return isOverallFitnessStagnation && isBestFitnessStagnation;
    }
}
