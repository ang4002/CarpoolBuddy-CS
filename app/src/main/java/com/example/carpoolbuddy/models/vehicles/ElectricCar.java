package com.example.carpoolbuddy.models.vehicles;

public class ElectricCar extends Vehicle{
    int range;

    public ElectricCar() {
    }

    public ElectricCar(String owner, String ownerID, String model, int capacity, String vehicleType, double rideCost, int distance, int range) {
        super(owner, ownerID, model, capacity, vehicleType, rideCost, distance);
        vehicleType = "ElectricCar";
        this.range = range;
    }
}
