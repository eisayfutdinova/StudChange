package com.example.user_vs.fragments;

public class ModelOfApply {
    private String apply_nameStr;
    private String apply_costStr ;
    private String apply_countryStr;
    private String apply_typeStr;
    private String apply_linkStr;
    private String apply_descStr;
    private String apply_emailStr;

    public ModelOfApply(String apply_nameStr, String apply_costStr, String apply_countryStr, String apply_typeStr, String apply_linkStr, String apply_descStr, String apply_emailStr) {
        this.apply_nameStr = apply_nameStr;
        this.apply_costStr = apply_costStr;
        this.apply_countryStr = apply_countryStr;
        this.apply_typeStr = apply_typeStr;
        this.apply_linkStr = apply_linkStr;
        this.apply_descStr = apply_descStr;
        this.apply_emailStr = apply_emailStr;
    }

    public String getApply_nameStr() {
        return apply_nameStr;
    }

    public String getApply_costStr() {
        return apply_costStr;
    }

    public String getApply_countryStr() {
        return apply_countryStr;
    }

    public String getApply_typeStr() {
        return apply_typeStr;
    }

    public String getApply_linkStr() {
        return apply_linkStr;
    }

    public String getApply_descStr() {
        return apply_descStr;
    }

    public String getApply_emailStr() {
        return apply_emailStr;
    }
}
