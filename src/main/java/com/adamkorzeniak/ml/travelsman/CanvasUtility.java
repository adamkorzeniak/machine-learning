package com.adamkorzeniak.ml.travelsman;

import javax.swing.*;
import java.util.List;

public class CanvasUtility {

    public static void displayCanvas(List<Location> bestLocationOrder) {
        JFrame f = new JFrame("Canvas Example");
        f.add(new LocationCanvas(bestLocationOrder));
        f.setLayout(null);
        f.setSize(TravelsmanEnvironment.WIDTH + 10, TravelsmanEnvironment.HEIGHT + 35);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.repaint();
    }

}
