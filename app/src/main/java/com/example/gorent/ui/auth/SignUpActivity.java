package com.example.gorent.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gorent.MainActivity;
import com.example.gorent.R;
import com.example.gorent.data.models.auth.LoginModel;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.AuthenticationRepository;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {

    private LinearLayout mainLayout;

    private LinearLayout buttonsLayout;

    private FragmentContainerView fragmentContainerView;

    private TextView titleText;

    private TextView subtitleText;

    private Button tenantButton;

    private Button rentingAgencyButton;

    private ProgressDialog progressDialog;

    private final TenantSignUpFragment tenantSignUpFragment = TenantSignUpFragment.newInstance();

    private final String TENANT_FRAG_TAG = "TenantFrag";

    private final RentingAgencySignUpFragment rentingAgencySignUpFragment = RentingAgencySignUpFragment.newInstance();

    private final String RENTING_AGENCY_FRAG_TAG = "RentingAgencyFrag";

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private SharedPreferencesManager sharedPreferencesManager;

    private List<Disposable> disposables;

    private AuthenticationRepository authenticationRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(R.style.Theme_GoRent_NoActionBar);
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        disposables = new ArrayList<>();
        authenticationRepository = RepositoryManager.getInstance(getApplicationContext()).getAuthenticationRepository();
        initLayout();
        initViewsProperties();
        initEventListeners();
        setContentView(mainLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initLayout() {
        // Init Linear Layouts
        mainLayout = new LinearLayout(this);
        buttonsLayout = new LinearLayout(this);

        // Init Fragment Container View
        fragmentContainerView = new FragmentContainerView(this);

        // Init Text Views
        titleText = new TextView(this);
        subtitleText = new TextView(this);

        // Init Buttons
        tenantButton = new Button(this);
        rentingAgencyButton = new Button(this);

        // Buttons Layout Children
        buttonsLayout.addView(tenantButton);
        buttonsLayout.addView(rentingAgencyButton);

        // Main Layout Children
        mainLayout.addView(titleText);
        mainLayout.addView(subtitleText);
        mainLayout.addView(buttonsLayout);
        mainLayout.addView(fragmentContainerView);
    }

    private void initViewsProperties() {
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        mainLayout.setBackgroundColor(getResources().getColor(R.color.bg_light, getTheme()));

        int marginSmall = getResources().getDimensionPixelSize(R.dimen.margin_small);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, marginSmall, 0, 0);

        titleText.setText(R.string.title_sign_up);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.title_size));
        titleText.setLayoutParams(titleParams);

        subtitleText.setText(R.string.subtitle_sign_up);
        subtitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.subtitle_size));
        subtitleText.setLayoutParams(titleParams);

        LinearLayout.LayoutParams buttonsParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonsParams.setMargins(0, marginSmall, 0, marginSmall);
        buttonsLayout.setGravity(Gravity.CENTER);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(buttonsParams);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(marginSmall, 0, marginSmall, 0);
        tenantButton.setLayoutParams(buttonParams);
        tenantButton.setMinWidth(getResources().getDimensionPixelSize(R.dimen.button_min_width_small));
        tenantButton.setText(R.string.tenant);
        tenantButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_light, getTheme())));
        tenantButton.setTextColor(getResources().getColor(R.color.white, getTheme()));

        rentingAgencyButton.setLayoutParams(buttonParams);
        rentingAgencyButton.setMinWidth(getResources().getDimensionPixelSize(R.dimen.button_min_width_small));
        rentingAgencyButton.setText(R.string.renting_agency);
        rentingAgencyButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_light, getTheme())));
        rentingAgencyButton.setTextColor(getResources().getColor(R.color.white, getTheme()));

        fragmentContainerView.setId(R.id.fragment_container_sign_up);
        fragmentContainerView.setLayoutParams(buttonsParams);
    }

    private void addFragment(Fragment fragment, String fragmentTag) {
        if (!fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentContainerView.getId(), fragment, fragmentTag);
            fragmentTransaction.commit();
        }
    }

    private void initEventListeners() {
        tenantButton.setOnClickListener(view -> {
            addFragment(tenantSignUpFragment, TENANT_FRAG_TAG);
        });

        rentingAgencyButton.setOnClickListener(view -> {
            addFragment(rentingAgencySignUpFragment, RENTING_AGENCY_FRAG_TAG);
        });
    }

    void loginNewAccount(LoginModel loginModel) {
        progressDialog = ProgressDialog.show(SignUpActivity.this, "", "Signing in to your new account. Please wait...", true);
        disposables.add(authenticationRepository.authenticate(loginModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jwtToken -> {
                    sharedPreferencesManager.put(SharedPreferencesKey.AUTH_TOKEN, jwtToken.getId_token());
                    proceedToMainActivity();
                }, error -> {
                    progressDialog.hide();
                    Log.e("AUTH", error.getMessage());
                    ToastMaker.showLongToast(SignUpActivity.this, R.string.alrt_login_after_register);
                }));
    }

    private void proceedToMainActivity() {
        sharedPreferencesManager.put(SharedPreferencesKey.REMEMBER_ME, "False");
        disposables.add(authenticationRepository.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    sharedPreferencesManager.saveUser(user);
                    progressDialog.hide();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }, error -> {
                    progressDialog.hide();
                    Log.e("GET User", error.getMessage());
                }));
    }
}
