package com.example.carpoolbuddy.models.users;

public class Alumni extends User {
    String graduateYear;

    public Alumni() {
    }

    public Alumni(String name, String email, String userType, String id, String graduateYear) {
        super(name, email, userType, id);
        this.graduateYear = graduateYear;
    }

    public String getGraduateYear() {
        return graduateYear;
    }
}