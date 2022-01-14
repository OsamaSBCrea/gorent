package com.example.gorent.data.enums;

public enum Nationality {
    PALESTINIAN("Palestinian"),
    JORDANIAN("Jordanian"),
    SAUDI("Saudi"),
    EUROPEAN("European"),
    AMERICAN("American");

    private final String label;

    Nationality(String label) {
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
