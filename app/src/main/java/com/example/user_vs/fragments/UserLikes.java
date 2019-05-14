package com.example.user_vs.fragments;

public class UserLikes {
    private String userId;
    private String exchangeId;

    public UserLikes(){}

    public UserLikes(String userId, String exchangeId) {
        this.userId = userId;
        this.exchangeId = exchangeId;
    }

    public String getUserId() {
        return userId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

}
