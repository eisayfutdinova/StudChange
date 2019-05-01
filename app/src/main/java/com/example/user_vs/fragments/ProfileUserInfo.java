package com.example.user_vs.fragments;

import java.util.List;

public class ProfileUserInfo {
    private String userId;
    private String fullName;
    private String gender;
    private String country;
    private String university;
    private String age;

    public ProfileUserInfo() {
    }

    public ProfileUserInfo(String userId, String fullname, String gender, String country, String university, String age) {
        this.userId = userId;
        this.fullName = fullname;
        this.gender = gender;
        this.country = country;
        this.university = university;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCountry() {
        return country;
    }

    public String getUniversity() {
        return university;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getUserId() {
        return userId;
    }

}
