package com.example.gorent.data.models.locale;

import com.example.gorent.data.enums.Currency;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("currency")
    private Currency currency;

    @SerializedName("cities")
    private List<City> cities;

    public Country() {
    }

    public Country(Long id, String name, String countryCode, Currency currency) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return name;
    }
}
