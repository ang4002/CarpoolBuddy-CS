package com.example.carpoolbuddy.user;

import java.util.ArrayList;

public class Alumni extends User {
    String graduateYear;

    public Alumni(String name, String email, String userType, String id, String graduateYear) {
        super(name, email, userType, id);
        this.graduateYear = graduateYear;
    }
}