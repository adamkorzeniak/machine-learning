package com.adamkorzeniak.ml.travelsman;

import com.adamkorzeniak.ml.FrameworkEnvironment;
import com.adamkorzeniak.ml.FrameworkSimulation;

public class App {

    private static final int CITIES_AMOUNT = 50;

    public static void main(String[] args) {
        FrameworkEnvironment environment = new TravelsmanEnvironment(CITIES_AMOUNT);
        FrameworkSimulation simulation = new TravelsmanSimulation(environment);
        simulation.start();
    }
}
