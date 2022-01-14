package com.example.gorent.data.enums;

public enum PropertyStatus {
    FURNISHED("Furnished"),
    UNFURNISHED("Unfurnished");

    private final String label;

    PropertyStatus(String label) {
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
