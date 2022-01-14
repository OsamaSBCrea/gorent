package com.example.gorent.ui.settings;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gorent.R;
import com.example.gorent.data.enums.Gender;
import com.example.gorent.data.enums.Nationality;
import com.example.gorent.data.models.Tenant;
import com.example.gorent.data.models.auth.TenantRegisterModel;
import com.example.gorent.data.models.auth.UserModel;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.TenantRepository;
import com.example.gorent.ui.auth.TenantSignUpFragment;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.keys.SharedKeys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantProfileFragment extends Fragment {

    @BindView(R.id.edit_tenant_profile_first)
    EditText firstNameEdit;

    @BindView(R.id.edit_tenant_profile_last)
    EditText lastNameEdit;

    @BindView(R.id.edit_tenant_profile_family)
    EditText familySizeEdit;

    @BindView(R.id.edit_tenant_profile_phone)
    EditText phoneEdit;

    @BindView(R.id.edit_tenant_profile_gms)
    EditText salaryEdit;

    @BindView(R.id.edit_tenant_profile_occupation)
    EditText occupationEdit;

    @BindView(R.id.edit_tenant_profile_gender)
    Spinner genderSpinner;

    @BindView(R.id.edit_tenant_profile_nationality)
    Spinner nationalitySpinner;

    @BindView(R.id.tenant_profile_email)
    TextView emailText;

    @BindView(R.id.tenant_profile_country)
    TextView countryText;

    @BindView(R.id.tenant_profile_city)
    TextView cityText;

    @BindView(R.id.tenant_profile_invalid_first)
    TextView invalidFirstText;

    @BindView(R.id.tenant_profile_invalid_last)
    TextView invalidLastText;

    @BindView(R.id.tenant_profile_invalid_family)
    TextView invalidFamilyText;

    @BindView(R.id.tenant_profile_invalid_gms)
    TextView invalidGmsText;

    @BindView(R.id.tenant_profile_invalid_occupation)
    TextView invalidOccupationText;

    @BindView(R.id.tenant_profile_invalid_phone)
    TextView invalidPhoneText;

    private ProgressDialog progressDialog;

    private TenantRepository tenantRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private UserModel user;

    private Tenant tenant;

    private List<Disposable> disposables;

    public static final String TAG = "[TenantProfileFragment]";

    private ArrayAdapter<Gender> genderArrayAdapter;

    private ArrayAdapter<Nationality> nationalityArrayAdapter;

    public TenantProfileFragment() {
        // Required empty public constructor
    }

    public static TenantProfileFragment newInstance() {
        TenantProfileFragment fragment = new TenantProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext().getApplicationContext());
        tenantRepository = RepositoryManager.getInstance(getContext().getApplicationContext()).getTenantRepository();
        user = sharedPreferencesManager.getUser();
        disposables = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tenant_profile, container, false);
        ButterKnife.bind(this, view);
        initViews();
        initArrayAdapters();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTenant();
    }

    @Override
    public void onPause() {
        disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    private void getTenant() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...");
        disposables.add(tenantRepository.getTenant(user.getTenantId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tenant -> {
                    this.tenant = tenant;
                    this.emailText.setText(user.getEmail());
                    this.firstNameEdit.setText(tenant.getFirstName());
                    this.lastNameEdit.setText(tenant.getLastName());
                    this.phoneEdit.setText(tenant.getPhoneNumber());
                    this.occupationEdit.setText(tenant.getOccupation());
                    this.familySizeEdit.setText(String.valueOf(tenant.getFamilySize()));
                    this.salaryEdit.setText(String.valueOf(tenant.getGrossMonthlySalary()));
                    this.cityText.setText(tenant.getCity().getName());
                    this.countryText.setText(tenant.getCountry().getName());
                    this.nationalitySpinner.setSelection(nationalityArrayAdapter.getPosition(tenant.getNationality()));
                    this.genderSpinner.setSelection(genderArrayAdapter.getPosition(tenant.getGender()));
                    progressDialog.hide();
                }, error -> {
                    Log.e(TAG, "GET Tenant: " + error.getMessage());
                    progressDialog.hide();
                }));
    }

    private void initArrayAdapters() {
        genderArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Gender.values()
        );

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderArrayAdapter);

        nationalityArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Nationality.values()
        );

        nationalityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(nationalityArrayAdapter);
    }

    private void initViews() {
        this.phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String countryCode = '+' + tenant.getCountry().getCountryCode();
                if (!s.toString().startsWith(countryCode)) {
                    phoneEdit.setText(countryCode);
                    Selection.setSelection(phoneEdit.getText(), phoneEdit.getText().length());
                }
            }
        });
    }

    @OnClick(R.id.btn_tenant_edit)
    void onSubmitClick() {
        if (TenantProfileFormManager.validateSignUpForm(this)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Submitting. Please wait...");
            disposables.add(tenantRepository.updateTenant(tenant.getId(), tenant)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tenant -> {
                        sharedPreferencesManager.put(SharedPreferencesKey.USER_NAME, tenant.getFullName());
                        progressDialog.hide();
                        if (getActivity() instanceof SettingsActivity) {
                            getActivity().finish();
                        }
                    }, error -> {
                        progressDialog.hide();
                        Log.e(TAG, "PUT tenant: " + error.getMessage());
                    }));
        }
    }

    static class TenantProfileFormManager {

        private static final int TENANT_NAME_MAX_CHARS = 20;
        private static final int TENANT_NAME_MIN_CHARS = 3;
        private static final int TENANT_OCCUPATION_MAC_CHARS = 20;

        static boolean validateSignUpForm(TenantProfileFragment tenantProfileFragment) {
            tenantProfileFragment.invalidFirstText.setVisibility(View.GONE);
            tenantProfileFragment.invalidLastText.setVisibility(View.GONE);
            tenantProfileFragment.invalidPhoneText.setVisibility(View.GONE);
            tenantProfileFragment.invalidOccupationText.setVisibility(View.GONE);
            tenantProfileFragment.invalidGmsText.setVisibility(View.GONE);
            tenantProfileFragment.invalidFamilyText.setVisibility(View.GONE);


            tenantProfileFragment.firstNameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantProfileFragment.lastNameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantProfileFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantProfileFragment.occupationEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantProfileFragment.familySizeEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantProfileFragment.salaryEdit.setBackgroundResource(R.drawable.edit_text_bg);

            boolean isValidForm = true;
            if (tenantProfileFragment.firstNameEdit.getText().toString().length() > TENANT_NAME_MAX_CHARS ||
                    tenantProfileFragment.firstNameEdit.getText().toString().length() < TENANT_NAME_MIN_CHARS) {
                tenantProfileFragment.firstNameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidFirstText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantProfileFragment.lastNameEdit.getText().toString().length() > TENANT_NAME_MAX_CHARS ||
                    tenantProfileFragment.lastNameEdit.getText().toString().length() < TENANT_NAME_MIN_CHARS) {
                tenantProfileFragment.lastNameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidLastText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (tenantProfileFragment.phoneEdit.getText().toString().length() < SharedKeys.PHONE_MIN_LENGTH) {
                tenantProfileFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidPhoneText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantProfileFragment.occupationEdit.getText().toString().length() > TENANT_OCCUPATION_MAC_CHARS ||
                    tenantProfileFragment.occupationEdit.getText().toString().isEmpty()) {
                tenantProfileFragment.occupationEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidOccupationText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantProfileFragment.familySizeEdit.getText().toString().isEmpty()) {
                tenantProfileFragment.familySizeEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidFamilyText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantProfileFragment.salaryEdit.getText().toString().isEmpty()) {
                tenantProfileFragment.salaryEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantProfileFragment.invalidGmsText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (!isValidForm) {
                return false;
            }

            tenantProfileFragment.tenant.setFirstName(tenantProfileFragment.firstNameEdit.getText().toString());
            tenantProfileFragment.tenant.setLastName(tenantProfileFragment.lastNameEdit.getText().toString());
            tenantProfileFragment.tenant.setOccupation(tenantProfileFragment.occupationEdit.getText().toString());
            tenantProfileFragment.tenant.setGrossMonthlySalary(Double.valueOf(tenantProfileFragment.salaryEdit.getText().toString()));
            tenantProfileFragment.tenant.setFamilySize(Integer.valueOf(tenantProfileFragment.familySizeEdit.getText().toString()));
            tenantProfileFragment.tenant.setPhoneNumber(tenantProfileFragment.phoneEdit.getText().toString());
            tenantProfileFragment.tenant.setGender((Gender) tenantProfileFragment.genderSpinner.getSelectedItem());
            tenantProfileFragment.tenant.setNationality((Nationality) tenantProfileFragment.nationalitySpinner.getSelectedItem());

            return isValidForm;
        }
    }
}