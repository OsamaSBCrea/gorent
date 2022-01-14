package com.example.gorent.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gorent.R;
import com.example.gorent.data.enums.UserRole;
import com.example.gorent.data.models.auth.UserModel;
import com.example.gorent.util.shared.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        UserModel user = sharedPreferencesManager.getUser();
        if (savedInstanceState == null) {
            if (user.getRole() == UserRole.RENTING_AGENCY) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile, new AgencyProfileFragment())
                        .commit();
            } else if (user.getRole() == UserRole.TENANT) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile, new TenantProfileFragment())
                        .commit();
            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}