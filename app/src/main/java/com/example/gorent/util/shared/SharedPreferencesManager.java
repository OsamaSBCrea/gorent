package com.example.gorent.util.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gorent.data.enums.RentStatus;
import com.example.gorent.data.enums.UserRole;
import com.example.gorent.data.models.auth.UserModel;

public class SharedPreferencesManager {

    private static final String SHARED_PREF_NAME = "GoRent Shared Preferences";
    private static SharedPreferencesManager selfInstance = null;
    private static SharedPreferences sharedPreferences = null;
    private final SharedPreferences.Editor editor;

    public static SharedPreferencesManager getInstance(Context context) {
        if (selfInstance == null) {
            selfInstance = new SharedPreferencesManager(context);
        }
        return selfInstance;
    }

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean put(SharedPreferencesKey key, String value) {
        editor.putString(key.getLabel(), value);
        return editor.commit();
    }

    public String get(SharedPreferencesKey key, String defaultValue) {
        return sharedPreferences.getString(key.getLabel(), defaultValue);
    }

    public void remove(SharedPreferencesKey key) {
        editor.putString(key.getLabel(), "");
        editor.commit();
    }

    public void saveUser(UserModel result) {
        editor.putLong(SharedPreferencesKey.USER_ID.getLabel(), result.getId());
        editor.putString(SharedPreferencesKey.USER_NAME.getLabel(), result.getName());
        editor.putString(SharedPreferencesKey.USER_EMAIL.getLabel(), result.getEmail());
        editor.putString(SharedPreferencesKey.USER_ROLE.getLabel(), result.getRole().getLabel());
        editor.putLong(SharedPreferencesKey.USER_COUNTRY_ID.getLabel(), result.getCountryId());
        if (result.getRole() == UserRole.RENTING_AGENCY) {
            editor.putLong(SharedPreferencesKey.AGENCY_ID.getLabel(), result.getAgencyId());
        } else if (result.getRole() == UserRole.TENANT) {
            editor.putLong(SharedPreferencesKey.TENANT_ID.getLabel(), result.getTenantId());
            if (result.getCurrentRentedPropertyId() != null) {
                editor.putLong(SharedPreferencesKey.CURRENT_RENTED_PROPERTY_ID.getLabel(), result.getCurrentRentedPropertyId());
                editor.putString(SharedPreferencesKey.CURRENT_RENTING_STATUS.getLabel(), result.getCurrentRentStatus().toString());
            } else {
                editor.putLong(SharedPreferencesKey.CURRENT_RENTED_PROPERTY_ID.getLabel(), -1);
                editor.putString(SharedPreferencesKey.CURRENT_RENTING_STATUS.getLabel(), "");
            }
        }
        editor.commit();
    }

    public UserModel getUser() {
        if (sharedPreferences.getLong(SharedPreferencesKey.USER_ID.getLabel(), -1) == -1) {
            return null;
        }

        UserModel userModel = new UserModel(
                sharedPreferences.getLong(SharedPreferencesKey.USER_ID.getLabel(), -1),
                sharedPreferences.getString(SharedPreferencesKey.USER_EMAIL.getLabel(), ""),
                sharedPreferences.getString(SharedPreferencesKey.USER_NAME.getLabel(), ""),
                UserRole.valueOf(sharedPreferences.getString(SharedPreferencesKey.USER_ROLE.getLabel(), "")),
                sharedPreferences.getLong(SharedPreferencesKey.AGENCY_ID.getLabel(), -1),
                sharedPreferences.getLong(SharedPreferencesKey.TENANT_ID.getLabel(), -1),
                sharedPreferences.getLong(SharedPreferencesKey.USER_COUNTRY_ID.getLabel(), -1),
                sharedPreferences.getLong(SharedPreferencesKey.CURRENT_RENTED_PROPERTY_ID.getLabel(), -1),
                RentStatus.valueOf(sharedPreferences.getString(SharedPreferencesKey.CURRENT_RENTING_STATUS.getLabel(), ""))
        );

        return userModel;
    }
}

