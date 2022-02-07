package com.example.carpoolbuddy.models.vehicles;

public class Segway extends Vehicle{
    int range;
    int weightCapacity;

    public Segway(String owner, String ownerID, String model, int capacity, String vehicleType, double rideCost, int distance, int range, int weightCapacity) {
        super(owner, ownerID, model, capacity, vehicleType, rideCost, distance);
        vehicleType = "Segway";
        this.range = range;
        this.weightCapacity = weightCapacity;
    }
}
