package com.adamkorzeniak.ml.travelsman;

import com.adamkorzeniak.ml.FrameworkAgent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TravelsmanAgent extends FrameworkAgent {

    private final List<Integer> order;
    private double matingProbability = 0;
    private double fitness = 0;
    private double totalDistance = 0;

    @Override
    public void act() {

    }

    @Override
    public TravelsmanEnvironment getEnvironment() {
        return (TravelsmanEnvironment) super.getEnvironment();
    }

    private TravelsmanAgent(int elementsSize) {
        order = new ArrayList<>(elementsSize);
        for (int i = 0; i < elementsSize; i++) {
            order.add(0);
        }
    }

    private TravelsmanAgent(List<Integer> order) {
        this.order = order;
    }

    public static TravelsmanAgent createRandom(int amountOfCities) {
        List<Integer> order = IntStream.range(0, amountOfCities).boxed().collect(Collectors.toList());
        Collections.shuffle(order);
        return new TravelsmanAgent(order);
    }

    public static TravelsmanAgent createChild(Set<FrameworkAgent> parentAgents, int amountOfCities) {
        List<Integer> parentOrder1 = ((TravelsmanAgent)parentAgents.stream().findFirst().get()).order;
        List<Integer> parentOrder2 = ((TravelsmanAgent)parentAgents.stream().findFirst().get()).order;

        int size = amountOfCities;
        Set<Integer> chunkLeft1 = new HashSet<>(parentOrder1);
        Set<Integer> chunkLeft2 = new HashSet<>(parentOrder2);

        TravelsmanAgent child1 = new TravelsmanAgent(size);
        TravelsmanAgent child2 = new TravelsmanAgent(size);

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

        Set<TravelsmanAgent> children = new HashSet<>();
        children.add(child1);
        children.add(child2);
        return child1;
    }

    public void mutate() {
        int size = this.order.size();
        for (int i = 0; i < size; i++) {
            double mutationRoll = Math.random();
            if (mutationRoll <= getEnvironment().getMutabilityFactor()) {
                int otherElementIndex = (int)(Math.random() * size);
                int tempValue = this.order.get(i);
                this.order.set(i, this.order.get(otherElementIndex));
                this.order.set(otherElementIndex, tempValue);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Distance: %.2f", totalDistance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelsmanAgent agent = (TravelsmanAgent) o;
        return order.equals(agent.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }

    @Override
    public void calculateFitness() {
        this.totalDistance  = calculateTotalDistance();
        double temp = getEnvironment().getAmountOfCities() * (TravelsmanEnvironment.HEIGHT + TravelsmanEnvironment.WIDTH) / this.totalDistance / 2;
        this.fitness = Math.pow(temp, 3);
    }

    private double calculateTotalDistance()  {
        int size = order.size();
        double totalDistance = 0;
        List<Location> locations = getEnvironment().getLocations();

        for (int i = 0; i < size - 1; i++) {
            Location start = locations.get(order.get(i));
            Location end = locations.get(order.get(i+ 1));
            double distance = start.calculateDistanceTo(end);
            totalDistance += distance;
        }
        return totalDistance;
    }

    public List<Location> generateRoute() {
        int locationAmount = order.size();
        List<Location> locations = getEnvironment().getLocations();
        List<Location> route = new ArrayList<>(locationAmount);
        for (int i = 0; i < locationAmount; i++) {
            route.add(locations.get(order.get(i)));
        }
        return route;
    }

    public List<TravelsmanAgent> crossover(List<TravelsmanAgent> agents) {
        List<TravelsmanAgent> parents = new ArrayList<>(agents);
        int size = parents.size();

		double fitnessTotalSum = parents.stream().mapToDouble(agent -> agent.fitness).sum();
		double currentTotalSum = 0;
		for (int i = 0; i < size; i++) {
            TravelsmanAgent agent = parents.get(i);
			currentTotalSum += agent.fitness;
			agent.matingProbability = currentTotalSum / fitnessTotalSum;
		}

		Set<TravelsmanAgent> children = parents.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toSet());

		for (int i = 0; i < (getEnvironment().getPopulationLimit() - 2) / 2; i++) {
			double percentile1 = Math.random();
			double percentile2 = Math.random();
            TravelsmanAgent parent1 = getByPercentile(parents, percentile1);
            TravelsmanAgent parent2 = getByPercentile(parents, percentile2);
            Set<FrameworkAgent> fagents = new HashSet<>(Arrays.asList(parent2, parent1));
			children.add(createChild(fagents, getEnvironment().getAmountOfCities()));
		}

        List<TravelsmanAgent> collect = parents.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        List<TravelsmanAgent> children2 = parents.stream().sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());

        int bestAgentsSize = getEnvironment().getAmountOfCities() / 10;
        List<TravelsmanAgent> bestAgents = parents.stream().sorted(Comparator.reverseOrder()).limit(bestAgentsSize).collect(Collectors.toList());

        for (int i = 0; i < (getEnvironment().getPopulationLimit() - 2) / 2; i++) {
            int index1 = (int) (Math.random() * bestAgentsSize);
            int index2 = (int) (Math.random() * bestAgentsSize);
            Set<FrameworkAgent> travelsmanAgents = new HashSet<>(Arrays.asList(bestAgents.get(index1), bestAgents.get(index2)));
            children.add(createChild(travelsmanAgents, getEnvironment().getAmountOfCities()));
        }
        return children2;
    }

    private TravelsmanAgent getByPercentile(List<TravelsmanAgent> parents, double percentile1) {
        return null;
    }
}
