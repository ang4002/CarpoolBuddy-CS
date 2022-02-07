package com.example.carpoolbuddy.models.vehicles;

public class Bicycle extends Vehicle {
    int weight;
    int weightCapacity;

    public Bicycle() {
    }

    public Bicycle(String owner, String ownerID, String model, int capacity, String vehicleType, double rideCost, int distance, int weight, int weightCapacity) {
        super(owner, ownerID, model, capacity, vehicleType, rideCost, distance);
        vehicleType = "Bicycle";
        this.weight = weight;
        this.weightCapacity = weightCapacity;
    }
}
