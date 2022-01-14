package com.example.gorent.data.sqlite.entities;

public enum SQLiteTableName {
    COUNTRY("country");

    private final String label;

    SQLiteTableName(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
