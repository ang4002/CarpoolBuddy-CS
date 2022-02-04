package com.example.carpoolbuddy.models;

import java.util.ArrayList;

public class Parent extends User {
    ArrayList<String> childrenUIDs;

    public Parent() {

    }

    public Parent(String name, String email, String userType, String id) {
        super(name, email, userType, id);
    }
}
