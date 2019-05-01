package com.example.user_vs.fragments;

import java.util.List;

public class ProfileUserInfo {
    private String userId;
    private String fullName;
    private String gender;
    private String country;
    private String university;
    private String age;
    private String languages;

    public ProfileUserInfo() {
    }

    public ProfileUserInfo(String userId, String fullname, String gender, String country, String university, String age, String languages) {
        this.userId = userId;
        this.fullName = fullname;
        this.gender = gender;
        this.country = country;
        this.university = university;
        this.age = age;
        this.languages = languages;
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

    public String getLanguages() {
        return languages;
    }
}
