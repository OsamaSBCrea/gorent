package com.example.gorent.ui.auth;

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
import com.example.gorent.data.models.auth.AgencyRegisterModel;
import com.example.gorent.data.models.auth.LoginModel;
import com.example.gorent.data.models.auth.TenantRegisterModel;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.util.shared.keys.SharedKeys;
import com.example.gorent.util.shared.pagination.Page;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TenantSignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TenantSignUpFragment extends Fragment {

    @BindView(R.id.spinner_tenant_gender)
    Spinner genderSpinner;

    @BindView(R.id.spinner_tenant_nationality)
    Spinner nationalitySpinner;

    @BindView(R.id.spinner_tenant_country)
    Spinner countrySpinner;

    @BindView(R.id.spinner_tenant_city)
    Spinner citySpinner;

    @BindView(R.id.edit_tenant_phone)
    EditText phoneEdit;

    @BindView(R.id.edit_tenant_first)
    EditText firstNameEdit;

    @BindView(R.id.edit_tenant_last)
    EditText lastNameEdit;

    @BindView(R.id.edit_tenant_email)
    EditText emailEdit;

    @BindView(R.id.edit_tenant_password)
    EditText passwordEdit;

    @BindView(R.id.edit_tenant_confirm_password)
    EditText confirmPasswordEdit;

    @BindView(R.id.edit_tenant_family)
    EditText familySizeEdit;

    @BindView(R.id.edit_tenant_salary)
    EditText salaryEdit;

    @BindView(R.id.edit_tenant_occupation)
    EditText occupationEdit;

    @BindView(R.id.text_tenant_email_invalid)
    TextView invalidEmailText;

    @BindView(R.id.text_tenant_first_invalid)
    TextView invalidFirstText;

    @BindView(R.id.text_tenant_last_invalid)
    TextView invalidLastText;

    @BindView(R.id.text_tenant_password_invalid)
    TextView invalidPasswordText;

    @BindView(R.id.text_tenant_confirm_pass_invalid)
    TextView invalidConfirmPassText;

    @BindView(R.id.text_tenant_phone_invalid)
    TextView invalidPhoneText;

    @BindView(R.id.text_tenant_occupation_invalid)
    TextView invalidOccupationText;

    @BindView(R.id.text_tenant_family_invalid)
    TextView invalidFamilyText;

    @BindView(R.id.text_tenant_gms_invalid)
    TextView invalidGmsText;

    private ArrayAdapter<Country> countryArrayAdapter;

    private ArrayAdapter<City> cityArrayAdapter;

    private RepositoryManager repositoryManager;

    private List<Disposable> disposables;

    private ProgressDialog progressDialog;

    public TenantSignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TenantSignUpFragment.
     */
    public static TenantSignUpFragment newInstance() {
        TenantSignUpFragment fragment = new TenantSignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositoryManager = RepositoryManager.getInstance(getContext().getApplicationContext());
        disposables = new ArrayList<>();
    }

    @Override
    public void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tenant_sign_up, container, false);
        ButterKnife.bind(this, view);
        this.initArrayAdapters();
        this.getCountries();
        this.initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCountries();
    }

    private TenantRegisterModel getTenantInfo() {
        return new TenantRegisterModel(
                emailEdit.getText().toString(),
                passwordEdit.getText().toString(),
                confirmPasswordEdit.getText().toString(),
                firstNameEdit.getText().toString(),
                lastNameEdit.getText().toString(),
                ((Gender) genderSpinner.getSelectedItem()).getLabel(),
                ((Nationality) nationalitySpinner.getSelectedItem()),
                Double.valueOf(salaryEdit.getText().toString()),
                occupationEdit.getText().toString(),
                Integer.valueOf(familySizeEdit.getText().toString()),
                ((Country) countrySpinner.getSelectedItem()).getId(),
                ((City) citySpinner.getSelectedItem()).getId(),
                phoneEdit.getText().toString()
        );
    }

    @OnClick(R.id.btn_tenant_submit)
    void registerTenant() {
        if (TenantSignUpFormManager.validateSignUpForm(this)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Registering your account. Please wait...");
            disposables.add(repositoryManager.getAuthenticationRepository().registerTenant(TenantSignUpFormManager.getTenantData(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        progressDialog.hide();
                        ((SignUpActivity) getActivity()).loginNewAccount(new LoginModel(
                                emailEdit.getText().toString(),
                                passwordEdit.getText().toString(),
                                false
                        ));
                    }, error -> {
                        progressDialog.hide();
                        Log.e("POST Tenant", error.getMessage());
                    }));
        }

    }

    @OnItemSelected(R.id.spinner_tenant_country)
    void getCities(Spinner spinner, int countryPosition) {
        if (countrySpinner.getSelectedItem() != null) {
            phoneEdit.setText('+' + ((Country) countrySpinner.getSelectedItem()).getCountryCode());
            disposables.add(repositoryManager.getCityRepository().getCities(0, 20, null,
                    countryArrayAdapter.getItem(countryPosition).getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((cityPage) -> {
                        cityArrayAdapter.clear();
                        cityArrayAdapter.addAll(cityPage.getContent());
                    }, (error) -> {

                    }));
        }
    }

    private void initViews() {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (countrySpinner.getSelectedItem() != null && countrySpinner.getSelectedItem() instanceof Country) {
                    String countryCode = '+' + ((Country) countrySpinner.getSelectedItem()).getCountryCode();
                    if (!s.toString().startsWith(countryCode)) {
                        phoneEdit.setText(countryCode);
                        Selection.setSelection(phoneEdit.getText(), phoneEdit.getText().length());
                    }
                }

            }
        });
    }

    private void initArrayAdapters() {
        ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Gender.values()
        );

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<Nationality> nationalityArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                Nationality.values()
        );

        nationalityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationalitySpinner.setAdapter(nationalityArrayAdapter);

        countryArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityArrayAdapter);
    }

    private void getCountries() {
        disposables.add(repositoryManager.getCountryRepository().getCountries(0, 20, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((countryPage) -> {
                    countryArrayAdapter.clear();
                    countryArrayAdapter.addAll(countryPage.getContent());
                }, (error) -> {
                }));
    }

    static class TenantSignUpFormManager {

        private static final int TENANT_NAME_MAX_CHARS = 20;
        private static final int TENANT_NAME_MIN_CHARS = 3;
        private static final int TENANT_OCCUPATION_MAC_CHARS = 20;

        static boolean validateSignUpForm(TenantSignUpFragment tenantSignUpFragment) {
            tenantSignUpFragment.invalidFirstText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidLastText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidEmailText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidPasswordText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidConfirmPassText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidPhoneText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidOccupationText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidGmsText.setVisibility(View.GONE);
            tenantSignUpFragment.invalidFamilyText.setVisibility(View.GONE);


            tenantSignUpFragment.firstNameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.lastNameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.emailEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.passwordEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.confirmPasswordEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.occupationEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.familySizeEdit.setBackgroundResource(R.drawable.edit_text_bg);
            tenantSignUpFragment.salaryEdit.setBackgroundResource(R.drawable.edit_text_bg);

            boolean isValidForm = true;
            if (tenantSignUpFragment.firstNameEdit.getText().toString().length() > TENANT_NAME_MAX_CHARS ||
                    tenantSignUpFragment.firstNameEdit.getText().toString().length() < TENANT_NAME_MIN_CHARS) {
                tenantSignUpFragment.firstNameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidFirstText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantSignUpFragment.lastNameEdit.getText().toString().length() > TENANT_NAME_MAX_CHARS ||
                    tenantSignUpFragment.lastNameEdit.getText().toString().length() < TENANT_NAME_MIN_CHARS) {
                tenantSignUpFragment.lastNameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidLastText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (!tenantSignUpFragment.emailEdit.getText().toString().matches(SharedKeys.EMAIL_REGEX)) {
                tenantSignUpFragment.emailEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidEmailText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (!tenantSignUpFragment.passwordEdit.getText().toString().matches(SharedKeys.PASSWORD_REGEX)) {
                tenantSignUpFragment.passwordEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidPasswordText.setVisibility(View.VISIBLE);
                isValidForm = false;
            } else if (!tenantSignUpFragment.confirmPasswordEdit.getText().toString().equals(tenantSignUpFragment.passwordEdit.getText().toString())) {
                tenantSignUpFragment.confirmPasswordEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidConfirmPassText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (tenantSignUpFragment.phoneEdit.getText().toString().length() < SharedKeys.PHONE_MIN_LENGTH) {
                tenantSignUpFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidPhoneText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantSignUpFragment.occupationEdit.getText().toString().length() > TENANT_OCCUPATION_MAC_CHARS ||
                tenantSignUpFragment.occupationEdit.getText().toString().isEmpty()) {
                tenantSignUpFragment.occupationEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidOccupationText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantSignUpFragment.familySizeEdit.getText().toString().isEmpty()) {
                tenantSignUpFragment.familySizeEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidFamilyText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (tenantSignUpFragment.salaryEdit.getText().toString().isEmpty()) {
                tenantSignUpFragment.salaryEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                tenantSignUpFragment.invalidGmsText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            return isValidForm;
        }

        static TenantRegisterModel getTenantData(TenantSignUpFragment tenantSignUpFragment) {
            return new TenantRegisterModel(
                    tenantSignUpFragment.emailEdit.getText().toString(),
                    tenantSignUpFragment.passwordEdit.getText().toString(),
                    tenantSignUpFragment.confirmPasswordEdit.getText().toString(),
                    tenantSignUpFragment.firstNameEdit.getText().toString(),
                    tenantSignUpFragment.lastNameEdit.getText().toString(),
                    ((Gender) tenantSignUpFragment.genderSpinner.getSelectedItem()).getLabel(),
                    ((Nationality) tenantSignUpFragment.nationalitySpinner.getSelectedItem()),
                    Double.valueOf(tenantSignUpFragment.salaryEdit.getText().toString()),
                    tenantSignUpFragment.occupationEdit.getText().toString(),
                    Integer.valueOf(tenantSignUpFragment.familySizeEdit.getText().toString()),
                    ((Country) tenantSignUpFragment.countrySpinner.getSelectedItem()).getId(),
                    ((City) tenantSignUpFragment.citySpinner.getSelectedItem()).getId(),
                    tenantSignUpFragment.phoneEdit.getText().toString()
            );
        }
    }
}