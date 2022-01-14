package com.example.gorent.data.sqlite.entities;

public enum SQLiteCountryField {
    ID("id"),
    NAME("name"),
    COUNTRY_CODE("country_code");

    private final String label;

    SQLiteCountryField(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
