package com.example.user_vs.fragments;

public class Exchange{
    private String univercityName;
    private String educationalProgram;
    private String country;
    private String language;

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    private String exchangeId;

    public Exchange() {
    }

    public Exchange(String univercity, String educationalProgram, String country, String language, String sxchangeId) {
        this.univercityName = univercity;
        this.exchangeId = sxchangeId;
        this.educationalProgram = educationalProgram;
        this.country = country;
        this.language = language;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public String getUnivercityName() {
        return univercityName;
    }

    public String getEducationalProgram() {
        return educationalProgram;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }
}
