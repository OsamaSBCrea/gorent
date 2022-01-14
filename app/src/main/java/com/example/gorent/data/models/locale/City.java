package com.example.gorent.data.models.locale;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class City implements Serializable {

    private Long id;

    private String name;

    private Long countryId;

    public City() {
    }

    public City(Long id, String name, Long countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof City) {
            return ((City) obj).id.equals(this.id);
        }
        return false;
    }
}
