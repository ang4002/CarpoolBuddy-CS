package com.example.carpoolbuddy.models;

import java.util.*;

public class User {
    private String uuid;
    private String name;
    private String email;
    private String userType;
    private double priceMultiplier;
    private ArrayList<String> ownedVehicles;
    private double balance;
    private double lagosBalance;

    public User() {
    }

    public User(String name, String email, String userType, String id) {
        this.uuid = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.balance = 1000;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public ArrayList<String> getOwnedVehicles() {
        return ownedVehicles;
    }

    public void addOwnedVehicle(String vehicle) {
        ownedVehicles.add(vehicle);
    }

    public void reduceBalance(int amount) {
        balance -= amount;
    }

    public void addBalance(int amount) {
        balance += amount;
    }

    public void setOwnedVehicles(ArrayList<String> ownedVehicles) {
        this.ownedVehicles = ownedVehicles;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void increaseBalance(double amount) {
        this.balance += amount;
    }

    public void decreaseBalance(double amount) {
        this.balance -= amount;
    }

    public double getLagosBalance() {
        return lagosBalance;
    }

    public void setLagosBalance(double lagosBalance) {
        this.lagosBalance = lagosBalance;
    }

    public void increaseLagosBalance(double amount) {
        this.balance += amount;
    }

    public void decreaseLagosBalance(double amount) {
        this.balance -= amount;
    }
}
