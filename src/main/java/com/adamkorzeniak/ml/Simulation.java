package com.adamkorzeniak.ml;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {

    private static final int GENERATION_LIMIT = 1000;
    public static final int POPULATION_SIZE = 10000;
    private static final int LOCATION_COUNT = 100;

    private int currentGeneration = 0;


    private boolean hasNextGeneration() {
        return currentGeneration <= GENERATION_LIMIT;
    }

    private List<Agent> generateRandomAgents() {
        return IntStream.range(0, POPULATION_SIZE).boxed()
                .map(i -> Agent.generateRandom(LOCATION_COUNT))
                .collect(Collectors.toList());
    }

    public void start() {
        World.initializeWorld(LOCATION_COUNT);
        List<Agent> agents = generateRandomAgents();
        long start = System.currentTimeMillis();
        while (hasNextGeneration()) {
            agents.forEach(Agent::calculateFitness);
            List<Agent> children = Agent.crossover(agents);
            children.forEach(Agent::mutate);
            currentGeneration++;
            long end = System.currentTimeMillis();
            System.out.printf("%d generation processed. Total: %.2fs%n", currentGeneration, (end - start) / 1000.0);
            System.out.printf("Best Fitness: %.2f%n%n", agents.stream().sorted(Comparator.reverseOrder()).findFirst().get().getTotalDistance());
            agents = children;
        }
        agents.forEach(Agent::calculateFitness);

        Agent champion = agents.stream().sorted(Comparator.reverseOrder()).findFirst().get();

        List<Location> route = champion.generateRoute();

        displayCanvas(route);
    }


    private void displayCanvas(List<Location> bestLocationOrder) {
        JFrame f = new JFrame("Canvas Example");
        f.add(new LocationCanvas(bestLocationOrder));
        f.setLayout(null);
        f.setSize(World.WIDTH + 10, World.HEIGHT + 35);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.repaint();
    }
}
