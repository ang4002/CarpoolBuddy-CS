package com.example.carpoolbuddy.models.vehicles;

public class Helicopter extends Vehicle {
    int maxAltitude;
    int maxAirSpeed;

    public Helicopter(String owner, String ownerID, String model, int capacity, String vehicleType, double rideCost, int distance, int maxAltitude, int maxAirSpeed) {
        super(owner, ownerID, model, capacity, vehicleType, rideCost, distance);
        vehicleType = "Helicopter";
        this.maxAltitude = maxAltitude;
        this.maxAirSpeed = maxAirSpeed;
    }
}
