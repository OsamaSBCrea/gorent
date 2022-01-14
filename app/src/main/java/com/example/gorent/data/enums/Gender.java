package com.example.gorent.data.enums;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label.charAt(0) + label.substring(1).toLowerCase();
    }
}
