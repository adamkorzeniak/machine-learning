package com.adamkorzeniak.ml;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Agent {

    private List<Integer> order;

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
        int[] temp = IntStream.range(0, elementsSize).toArray();
        shuffleArray(temp);
        List<Integer> order = new ArrayList<>(elementsSize);
        for (int value: temp) {
            order.add(value);
        }
        return new Agent(order);
    }

    public static List<Agent> generateFromParents(Agent parent1, Agent parent2) {

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

        leftBreak = 2;
        rightBreak = 8;

        System.out.printf("Left break: %d%n", leftBreak);
        System.out.printf("Right break: %d%n", rightBreak);

        List<Integer> parentChunk1 = parentOrder1.subList(leftBreak, rightBreak);
        List<Integer> parentChunk2 = parentOrder2.subList(leftBreak, rightBreak);

        chunkLeft1.removeAll(parentChunk2);
        chunkLeft2.removeAll(parentChunk1);

        int childIndex1 = rightBreak;
        int childIndex2 = rightBreak;

        int rotationsLeft = size - rightBreak + leftBreak;

        child1.order.addAll(leftBreak, parentChunk2);
        child2.order.addAll(leftBreak, parentChunk1);

        while (rotationsLeft > 0) {
            rightBreak %= size;

            Integer parentElement1 = parentOrder1.get(rightBreak);
            if (chunkLeft2.contains(parentElement1)) {
                childIndex1 %= size;
                child1.order.set(childIndex1++, parentElement1);
            }

            Integer parentElement2 = parentOrder2.get(rightBreak);
            if (chunkLeft1.contains(parentElement2)) {
                childIndex2 %= size;
                child2.order.set(childIndex2++, parentElement2);
            }

            rightBreak++;
            rotationsLeft--;
        }

        return Arrays.asList(child1, child2);
    }

    private static void shuffleArray(int[] ar)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    public String toString() {
        return order.toString();
    }

    public static void main(String[] args) {
        Agent parent1 = generateRandom(10);
        Agent parent2 = generateRandom(10);

        System.out.printf("Agent 1: %s%n", parent1);
        System.out.printf("Agent 2: %s%n", parent2);

        List<Agent> children = generateFromParents(parent1, parent2);


        System.out.printf("Child 1: %s%n", children.get(0));
        System.out.printf("Child 2: %s%n", children.get(1));

    }
}
