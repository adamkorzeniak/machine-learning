package com.adamkorzeniak.ml;

import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {

    private static final int GENERATION_LIMIT = 100;
    private static final int POPULATION_SIZE = 1000;

    private static final int currentGeneration = 0;


    private boolean hasNextGeneration() {
        return currentGeneration <= GENERATION_LIMIT;
    }

    private Set<Agent> generateRandomAgents() {
        return IntStream.range(0, POPULATION_SIZE).boxed()
                .map(i -> Agent.generateRandom(10))
                .collect(Collectors.toSet());
    }

    private Agent generateRandomAgent() {
        return Agent.generateRandom(20);
    }


    private void start() {

    }
}
