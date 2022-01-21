package com.example.carpoolbuddy.Models;

import java.io.Serializable;
import java.util.*;

public class Vehicle implements Serializable {
    private String owner;
    private String model;
    private int capacity;
    private String vehicleID;
    private ArrayList<String> ridersUIDs;
    private ArrayList<String> ridersNames;
    private boolean open;
    private String vehicleType;
    private double basePrice;

    public Vehicle() {
    }

    public Vehicle(String owner, String model, int capacity, String vehicleType, int basePrice) {
        this.owner = owner;
        this.model = model;
        this.capacity = capacity;
        this.vehicleID = UUID.randomUUID().toString();
        this.ridersUIDs = new ArrayList<String>();
        this.ridersNames = new ArrayList<String>();
        this.open = true;
        this.vehicleType = vehicleType;
        this.basePrice = basePrice;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public ArrayList<String> getRidersUIDs() {
        return ridersUIDs;
    }

    public void addRiderUID(String riderUID) {
        ridersUIDs.add(riderUID);
    }

    public void setRidersUIDs(ArrayList<String> ridersUIDs) {
        this.ridersUIDs = ridersUIDs;
    }

    public ArrayList<String> getRidersNames() {
        return ridersNames;
    }

    public void addRiderName(String riderName) {
        ridersNames.add(riderName);
    }

    public void setRidersNames(ArrayList<String> ridersNames) {
        this.ridersNames = ridersNames;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
