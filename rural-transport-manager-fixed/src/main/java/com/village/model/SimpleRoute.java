package com.village.model;

import java.util.UUID;

public class SimpleRoute implements Route, java.io.Serializable {
    private final String id;
    private final String name;
    private final double distanceKm;

    public SimpleRoute(String name, double distanceKm) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.distanceKm = distanceKm;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getDistanceKm() { return distanceKm; }

    @Override
    public String toString() { return "Route[" + name + "," + distanceKm + "km]"; }
}
