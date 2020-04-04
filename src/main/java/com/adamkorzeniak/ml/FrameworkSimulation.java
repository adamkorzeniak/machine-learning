package com.adamkorzeniak.ml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

public abstract class FrameworkSimulation {

    public static final int DEFAULT_GENERATION_LIMIT = 1000;

    private final FrameworkEnvironment environment;

    private final List<Double> overallFitnesses = new ArrayList<>();
    private final List<Double> bestFitnesses = new ArrayList<>();

    @Setter
    @Getter
    private boolean displayResultsAfterEachGeneration = false;

    @Getter
    private int currentGeneration = 1;

    public FrameworkSimulation(FrameworkEnvironment environment) {
        this.environment = environment;
    }

    public boolean hasNextGeneration() {
        return currentGeneration < DEFAULT_GENERATION_LIMIT;
    }

    public final Optional<Double> getOverallFitnessImprovement(int lastGenerations) {
        return getFitnessImprovement(lastGenerations, overallFitnesses);
    }

    public final Optional<Double> getBestFitnessImprovement(int lastGenerations) {
        return getFitnessImprovement(lastGenerations, bestFitnesses);
    }

    private Optional<Double> getFitnessImprovement(int lastGenerations, List<Double> overallFitnesses) {
        if (currentGeneration < lastGenerations) {
            return Optional.empty();
        }
        double currentFitness = overallFitnesses.get(currentGeneration);
        double previousFitness = overallFitnesses.get(currentGeneration - lastGenerations);
        return Optional.of(currentFitness / previousFitness);
    }

    public final void start() {
        while (hasNextGeneration()) {
            environment.generateNewGeneration();
            currentGeneration++;
            environment.runGeneration();
            storeGenerationSummary();
            if (displayResultsAfterEachGeneration) {
                environment.displayGeneration();
            }
        }
        environment.displayFinalResults();
    }

    private void storeGenerationSummary() {
        overallFitnesses.add(environment.getOverallFitness().orElse(null));
        bestFitnesses.add(environment.getBestFitness().orElse(null));
    }
}
