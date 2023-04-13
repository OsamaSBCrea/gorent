package com.example.gorent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.example.gorent.data.models.Property;
import com.example.gorent.data.models.PropertySearch;
import com.example.gorent.data.models.RentApplication;
import com.example.gorent.data.models.auth.UserModel;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.AuthenticationRepository;
import com.example.gorent.ui.auth.SignInActivity;
import com.example.gorent.ui.entities.property.PropertyDetailsListener;
import com.example.gorent.ui.entities.rent.RentApplicationDetailsListener;
import com.example.gorent.ui.property.PropertyDetailsActivity;
import com.example.gorent.ui.rent.RentApplicationDetailsActivity;
import com.example.gorent.ui.settings.SettingsActivity;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;
import com.example.gorent.util.shared.keys.SharedKeys;
import com.example.gorent.util.shared.pagination.Page;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// A commit test 2
public class MainActivity extends AppCompatActivity implements PropertyDetailsListener, RentApplicationDetailsListener {

    private AppBarConfiguration mAppBarConfiguration;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private NavController navController;

    private TextView navUserName;

    private TextView navUserEmail;

    private SharedPreferencesManager sharedPreferencesManager;

    private AuthenticationRepository authenticationRepository;

    private final List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_property_list, R.id.nav_property_search,
                R.id.nav_property_post, R.id.nav_rent_applications, R.id.nav_rental_history)
                .setDrawerLayout(drawer)
                .build();

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        authenticationRepository = RepositoryManager.getInstance(getApplicationContext()).getAuthenticationRepository();
        UserModel user = sharedPreferencesManager.getUser();
        switch (user.getRole()) {
            case TENANT:
                navigationView.inflateMenu(R.menu.tenant_main_drawer);
                fab.setVisibility(View.VISIBLE);
                break;
            case RENTING_AGENCY:
                navigationView.inflateMenu(R.menu.agency_main_drawer);
                fab.setVisibility(View.GONE);
                break;
            default:
                navigationView.inflateMenu(R.menu.guest_main_drawer);
                break;

        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        navUserEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FETCH TOKEN", task.getException());
                return;
            }

            disposables.add(authenticationRepository.refreshFCMToken(task.getResult())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        Log.d("TOKEN Refreshed", task.getResult());
                    }, error -> {
                        error.printStackTrace();
                        Log.e("TOKEN Refresh Error", error.getMessage());
                    }));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel user = sharedPreferencesManager.getUser();
        navUserName.setText(user.getName());
        navUserEmail.setText(user.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                sharedPreferencesManager.remove(SharedPreferencesKey.AUTH_TOKEN);
                sharedPreferencesManager.remove(SharedPreferencesKey.REMEMBER_ME);
                intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    void onFabClick(View view) {
        if (navController.getCurrentDestination().getId() != R.id.nav_property_search) {
            navController.navigate(R.id.nav_property_search);
        }
    }

    @Override
    public void showPropertyDetails(Property property) {
        Intent intent = new Intent(MainActivity.this, PropertyDetailsActivity.class);
        intent.putExtra(SharedKeys.PROPERTY_DETAILS_KEY, property);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!navController.popBackStack()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void goToPropertyList(View view) {
        Navigation.findNavController(view).navigate(R.id.nav_property_list);
    }

    public void goToPropertyList(View view, Page<Property> propertyPage, PropertySearch propertySearch) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SharedKeys.BUNDLE_PROPERTIES_PAGE, propertyPage);
        bundle.putSerializable(SharedKeys.BUNDLE_PROPERTY_SEARCH, propertySearch);
        Navigation.findNavController(view).navigate(R.id.nav_property_list, bundle);
    }

    @Override
    public void showRentApplicationDetails(RentApplication rentApplication) {
        Intent intent = new Intent(MainActivity.this, RentApplicationDetailsActivity.class);
        intent.putExtra(SharedKeys.RENT_APPLICATION_DETAILS, rentApplication);
        startActivity(intent);
    }
}
