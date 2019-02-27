package com.example.user_vs.fragments;

public class Exchange {
    private String univercityName;
    private String educationalProgram;
    private String country;
    private String language;

    public Exchange(){}

    public Exchange(String univercity,String educationalProgram,String country, String language) {
        this.univercityName = univercity;
        this.educationalProgram = educationalProgram;
        this.country = country;
        this.language = language;
    }

    public String getUnivercityName() {
        return univercityName;
    }

    public String getEducationalProgram()
    {
        return educationalProgram;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }
}
