package com.example.carpoolbuddy.models.vehicles;

public class FossilFuelCar extends Vehicle{
    int range;

    public FossilFuelCar() {
    }

    public FossilFuelCar(String owner, String ownerID, String model, int capacity, String vehicleType, double rideCost, int distance, int range) {
        super(owner, ownerID, model, capacity, vehicleType, rideCost, distance);
        vehicleType = "FossilFuelCar ";
        this.range = range;
    }
}
