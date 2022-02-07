package com.example.carpoolbuddy.models.users;

public class Teacher extends User {
    String inSchoolTitle;

    public Teacher() {
    }

    public Teacher(String name, String email, String userType, String id, String inSchoolTitle) {
        super(name, email, userType, id);
        this.inSchoolTitle = inSchoolTitle;
    }

    public String getInSchoolTitle() {
        return inSchoolTitle;
    }
}
