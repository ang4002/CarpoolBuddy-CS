package com.example.carpoolbuddy.user;

import java.util.ArrayList;

public class Student extends User {
    String graduatingYear;

    public Student(String name, String email, String userType, String id, String graduatingYear) {
        super(name, email, userType, id);
        this.graduatingYear = graduatingYear;
    }
}
