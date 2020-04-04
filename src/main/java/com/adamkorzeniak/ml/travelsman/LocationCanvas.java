package com.adamkorzeniak.ml;

import java.awt.*;
import java.util.List;

public class LocationCanvas extends Canvas {

    private List<Location> locationOrder;

    public LocationCanvas(List<Location> bestLocationOrder) {
        this.locationOrder = bestLocationOrder;
        setBackground(Color.GRAY);
        setSize(World.WIDTH, World.HEIGHT);

    }

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        int locationsSize = locationOrder.size();

        for (int i = 0; i < locationsSize - 1; i++) {
            Location location = locationOrder.get(i);
            Location nextLocation = locationOrder.get(i + 1);
            int x1 = (int) location.getX();
            int y1 = (int) location.getY();
            int x2 = (int) nextLocation.getX();
            int y2 = (int) nextLocation.getY();

            g.fillRect(x1 - 5, y1 - 5,  10, 10);
            g.drawLine(x1, y1, x2, y2);
        }

        Location end = locationOrder.get(locationsSize - 1);
        int xEnd = (int) end.getX();
        int yEnd = (int) end.getY();

        g.fillRect(xEnd - 5, yEnd - 5,  10, 10);
    }
}
