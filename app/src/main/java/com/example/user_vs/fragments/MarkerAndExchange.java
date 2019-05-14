package com.example.user_vs.fragments;

public class MarkerAndExchange {
    private String markerId;
    private String exchangeId;

    public MarkerAndExchange(){}

    public MarkerAndExchange(String userId, String exchangeId) {
        this.markerId = userId;
        this.exchangeId = exchangeId;
    }

    public String getMarkerId() {
        return markerId;
    }

    public String getExchangeId() {
        return exchangeId;
    }
}
