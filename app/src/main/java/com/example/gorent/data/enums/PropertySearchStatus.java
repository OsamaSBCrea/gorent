package com.example.gorent.data.enums;

public enum PropertySearchStatus {
    FURNISHED("Furnished"),
    UNFURNISHED("Unfurnished"),
    ALL("All");

    private final String label;

    PropertySearchStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
