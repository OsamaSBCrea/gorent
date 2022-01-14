package com.example.gorent.data.models;

import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;

import java.io.Serializable;

public class Agency implements Serializable {

    private Long id;

    private String name;

    private String phoneNumber;

    private Country country;

    private City city;

    public Agency() {
    }

    public Agency(Long id, String name, String phoneNumber, Country country, City city) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.city = city;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country=" + country +
                ", city=" + city +
                '}';
    }
}
