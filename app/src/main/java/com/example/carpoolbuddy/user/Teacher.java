package com.example.carpoolbuddy.user;

public class Teacher extends User {
    String inSchoolTitle;

    public Teacher(String name, String email, String userType, String id, String inSchoolTitle) {
        super(name, email, userType, id);
        this.inSchoolTitle = inSchoolTitle;
    }
}
