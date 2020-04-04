package com.adamkorzeniak.ml;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class FrameworkAgent implements Comparable<FrameworkAgent>{

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Double fitness;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private FrameworkEnvironment environment;

    public abstract void act();

    public abstract void calculateFitness();

    @Override
    public int compareTo(FrameworkAgent other) {
        return Double.compare(this.fitness, other.fitness);
    }

    public abstract void mutate();
}
