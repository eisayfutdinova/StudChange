package com.example.user_vs.fragments;

public class Exchange{
    private String name;
    private String type;
    private String language;
    private String cost;
    private String costDetails;
    private String country;
    private String description;
    private String fulldescription;
    private String link;

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    private String exchangeId;

    public Exchange() {
    }

    public Exchange(String name,String type,String language,
                    String cost,String costDetails,String country,
                    String description,String fulldescription,String link, String sxchangeId) {
        this.name = name;
        this.type = type;
        this.language = language;
        this.cost = cost;
        this.costDetails = costDetails;
        this.country = country;
        this.description = description;
        this.fulldescription = fulldescription;
        this.link = link;
        this.exchangeId = sxchangeId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getCost() {
        return cost;
    }

    public String getCostDetails() {
        return costDetails;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public String getFulldescription() {
        return fulldescription;
    }

    public String getLink() {
        return link;
    }
}
