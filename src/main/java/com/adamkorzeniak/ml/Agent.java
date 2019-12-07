package com.adamkorzeniak.ml;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Agent implements Comparable<Agent> {

    private static final double MUTABILITY_FACTOR = 0.01;

    private List<Integer> order;
    private double matingProbability = 0;
    private double fitness = 0;
    private double totalDistance = 0;

    private Agent(int elementsSize) {
        order = new ArrayList<>(elementsSize);
        for (int i = 0; i < elementsSize; i++) {
            order.add(0);
        }
    }

    private Agent(List<Integer> order) {
        this.order = order;
    }

    public static Agent generateRandom(int elementsSize) {
        List<Integer> order = IntStream.range(0, elementsSize).boxed().collect(Collectors.toList());
        Collections.shuffle(order);
        return new Agent(order);
    }

    public static Set<Agent> generateFromParents(Agent parent1, Agent parent2) {

        List<Integer> parentOrder1 = parent1.order;
        List<Integer> parentOrder2 = parent2.order;

        int size = parentOrder1.size();
        Set<Integer> chunkLeft1 = new HashSet<>(parentOrder1);
        Set<Integer> chunkLeft2 = new HashSet<>(parentOrder2);

        Agent child1 = new Agent(size);
        Agent child2 = new Agent(size);

        int break1 = (int)(Math.random() * size);
        int break2 = (int)(Math.random() * size);

        int leftBreak = Math.min(break1, break2);
        int rightBreak = Math.max(break1, break2);

        List<Integer> parentChunk1 = parentOrder1.subList(leftBreak, rightBreak);
        List<Integer> parentChunk2 = parentOrder2.subList(leftBreak, rightBreak);

        chunkLeft1.removeAll(parentChunk2);
        chunkLeft2.removeAll(parentChunk1);

        int childIndex1 = rightBreak;
        int childIndex2 = rightBreak;

        for (int i = leftBreak; i < rightBreak ; i++) {
            child1.order.set(i, parentOrder1.get(i));
            child2.order.set(i, parentOrder2.get(i));
        }

        for (int rotationsLeft = size; rotationsLeft > 0; rotationsLeft--) {
            rightBreak %= size;

            Integer parentElement1 = parentOrder1.get(rightBreak);
            Integer parentElement2 = parentOrder2.get(rightBreak);

            if (chunkLeft1.contains(parentElement1)) {
                childIndex2 %= size;
                child2.order.set(childIndex2++, parentElement1);
            }

            if (chunkLeft2.contains(parentElement2)) {
                childIndex1 %= size;
                child1.order.set(childIndex1++, parentElement2);
            }
            rightBreak++;
        }

        Set<Agent> children = new HashSet<>();
        children.add(child1);
        children.add(child2);
        return children;
    }

    public static List<Agent> crossover(List<Agent> agents) {
		List<Agent> parents = new ArrayList<>(agents);
		int size = parents.size();
		
//		double fitnessTotalSum = parents.stream().mapToDouble(agent -> agent.fitness).sum();
//		double currentTotalSum = 0;
//		for (int i = 0; i < size; i++) {
//			Agent agent = parents.get(i);
//			currentTotalSum += agent.fitness;
//			agent.matingProbability = currentTotalSum / fitnessTotalSum;
//		}
//
//		Set<Agent> children = parents.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toSet());
//
//		for (int i = 0; i < (Simulation.POPULATION_SIZE - 2) / 2; i++) {
//			double percentile1 = Math.random();
//			double percentile2 = Math.random();
//			Agent parent1 = getByPercentile(parents, percentile1);
//			Agent parent2 = getByPercentile(parents, percentile2);
//			children.addAll(generateFromParents(parent1, parent2));
//		}

        List<Agent> collect = parents.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        List<Agent> children = parents.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());

        int bestAgentsSize = World.getLocationAmount() / 10;
        List<Agent> bestAgents = parents.stream().sorted(Comparator.reverseOrder()).limit(bestAgentsSize).collect(Collectors.toList());

        for (int i = 0; i < (Simulation.POPULATION_SIZE - 2) / 2; i++) {
			int index1 = (int) (Math.random() * bestAgentsSize);
            int index2 = (int) (Math.random() * bestAgentsSize);
			children.addAll(generateFromParents(bestAgents.get(index1), bestAgents.get(index2)));
		}
        return children;
    }
	
    public void mutate() {
        int size = this.order.size();
        for (int i = 0; i < size; i++) {
            double mutationRoll = Math.random();
            if (mutationRoll <= MUTABILITY_FACTOR) {
                int otherElementIndex = (int)(Math.random() * size);
                int tempValue = this.order.get(i);
                this.order.set(i, this.order.get(otherElementIndex));
                this.order.set(otherElementIndex, tempValue);
            }
        }
    }

    public double getTotalDistance() {
        return  totalDistance;
    }

    @Override
    public String toString() {
        return String.format("Distance: %.2f", totalDistance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return order.equals(agent.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }

    public void calculateFitness() {
        this.totalDistance  = calculateTotalDistance();
        double temp = World.getLocationAmount() * (World.HEIGHT + World.WIDTH) / this.totalDistance / 2;
        this.fitness = Math.pow(temp, 3);
    }
	
	private double calculateTotalDistance()  {
        int size = order.size();
        double totalDistance = 0;
		List<Location> locations = World.getLocations();

        for (int i = 0; i < size - 1; i++) {
            Location start = locations.get(order.get(i));
            Location end = locations.get(order.get(i+ 1));
            double distance = start.calculateDistanceTo(end);
            totalDistance += distance;
        }
        return totalDistance;
    }
	
	public int compareTo(Agent other) {
		return Double.compare(this.fitness, other.fitness);
	}

    public List<Location> generateRoute() {
        int locationAmount = order.size();
//        int locationAmount = World.getLocationAmount();
        List<Location> locations = World.getLocations();
        List<Location> route = new ArrayList<>(locationAmount);
        for (int i = 0; i < locationAmount; i++) {
            route.add(locations.get(order.get(i)));
        }
        return route;
    }
}
