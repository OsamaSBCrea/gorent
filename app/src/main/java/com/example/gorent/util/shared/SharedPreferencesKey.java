package com.example.gorent.util.shared;

public enum SharedPreferencesKey {
    USER_ID("user_id"),
    USER_NAME("user_name"),
    USER_EMAIL("user_email"),
    USER_ROLE("user_role"),
    USER_COUNTRY_ID("user_country_id"),
    CURRENCY("currency"),
    AGENCY_ID("agency_id"),
    TENANT_ID("tenant_id"),
    AUTH_TOKEN("auth_token"),
    CURRENT_RENTED_PROPERTY_ID("current_property"),
    CURRENT_RENTING_STATUS("current_rent_status"),
    REMEMBER_ME("remember_me");

    private final String label;

    private SharedPreferencesKey(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
