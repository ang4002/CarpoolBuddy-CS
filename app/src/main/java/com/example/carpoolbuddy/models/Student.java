package com.example.carpoolbuddy.models;

import java.util.ArrayList;

public class Student extends User {
    String graduatingYear;
    ArrayList<String> parentUIDs;

    public Student() {
    }

    public Student(String name, String email, String userType, String id, String graduatingYear) {
        super(name, email, userType, id);
        this.graduatingYear = graduatingYear;
    }

    public String getGraduatingYear() {
        return graduatingYear;
    }
}



