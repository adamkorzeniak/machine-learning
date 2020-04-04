package com.adamkorzeniak.ml;

import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class FrameworkEnvironment {

    private static final int POPULATION_LIMIT = 1000;

    private final Supplier<FrameworkAgent> randomAgentSupplier;
    private final Function<Set<FrameworkAgent>, FrameworkAgent> agentsChildrenFunction;

    public FrameworkEnvironment(
                Supplier<FrameworkAgent> randomAgentSupplier,
                Function<Set<FrameworkAgent>, FrameworkAgent> agentsChildrenFunction) {
        this.randomAgentSupplier = randomAgentSupplier;
        this.agentsChildrenFunction = agentsChildrenFunction;
    }

    @Getter
    private Set<FrameworkAgent> agents = new HashSet<>();

    public final void runGeneration() {
        agents.forEach(FrameworkAgent::act);
        agents.forEach(FrameworkAgent::calculateFitness);
    }

    public void displayGeneration() {
        Optional<Double> maxFitness = getBestFitness();
        Optional<Double> overallFitness = getOverallFitness();
        if (maxFitness.isPresent() && overallFitness.isPresent()) {
            String result = String.format("Max fitness: %f.2, Overall fitness: %f.2",
                    maxFitness.get(),
                    overallFitness.get());
            System.out.println(result);
        }
    }

    public void generateNewGeneration() {
        if (agents.isEmpty()) {
            agents = IntStream.range(0, getPopulationLimit())
                    .mapToObj(i -> randomAgentSupplier.get())
                    .collect(Collectors.toSet());
        } else {
            Set<FrameworkAgent> parents = new HashSet<>();
            FrameworkAgent children = agentsChildrenFunction.apply(parents);
            children.mutate();
        }
        agents.forEach(agent -> agent.setEnvironment(this));
    }

    public int getPopulationLimit() {
        return POPULATION_LIMIT;
    }

    public void displayFinalResults() {
        displayGeneration();
    }

    public Optional<Double> getOverallFitness() {
        OptionalDouble average = agents.stream()
                .mapToDouble(FrameworkAgent::getFitness)
                .average();
        return average.isPresent()
                ? Optional.of(average.getAsDouble())
                : Optional.empty();
    }

    public Optional<Double> getBestFitness() {
        return agents.stream()
                .map(FrameworkAgent::getFitness)
                .max(Comparator.naturalOrder());
    }
}
