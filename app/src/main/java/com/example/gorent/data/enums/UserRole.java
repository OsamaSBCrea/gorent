package com.example.gorent.data.enums;

import com.google.gson.annotations.SerializedName;

public enum UserRole {
    @SerializedName("GUEST")
    GUEST("GUEST"),

    @SerializedName("TENANT")
    TENANT("TENANT"),

    @SerializedName("AGENCY")
    RENTING_AGENCY("RENTING_AGENCY");

    private String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
