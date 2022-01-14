package com.example.gorent.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.gorent.data.dto.PropertyDTO;
import com.example.gorent.data.enums.PropertyStatus;
import com.example.gorent.data.models.auth.AgencyRegisterModel;
import com.example.gorent.data.models.auth.LoginModel;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.sqlite.SQLiteCountryRepository;
import com.example.gorent.ui.property.post.PropertyPostFragment;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.keys.SharedKeys;
import com.example.gorent.util.shared.pagination.Page;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RentingAgencySignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RentingAgencySignUpFragment extends Fragment {

    @BindView(R.id.spinner_agency_country)
    Spinner countrySpinner;

    @BindView(R.id.spinner_agency_city)
    Spinner citySpinner;

    @BindView(R.id.edit_agency_phone)
    EditText phoneEdit;

    @BindView(R.id.edit_agency_email)
    EditText emailEdit;

    @BindView(R.id.edit_agency_name)
    EditText nameEdit;

    @BindView(R.id.edit_agency_password)
    EditText passwordEdit;

    @BindView(R.id.edit_agency_confirm_password)
    EditText confirmPasswordEdit;

    @BindView(R.id.text_agency_email_invalid)
    TextView invalidEmailText;

    @BindView(R.id.text_agency_name_invalid)
    TextView invalidNameText;

    @BindView(R.id.text_agency_password_invalid)
    TextView invalidPasswordText;

    @BindView(R.id.text_agency_confirm_pass_invalid)
    TextView invalidConfirmPassText;

    @BindView(R.id.text_agency_phone_invalid)
    TextView invalidPhoneText;

    private ArrayAdapter<Country> countryArrayAdapter;

    private ArrayAdapter<City> cityArrayAdapter;

    private RepositoryManager repositoryManager;

    private List<Disposable> disposables;

    private SQLiteCountryRepository sqLiteCountryRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private ProgressDialog progressDialog;

    private static final String TAG = "[RentingAgencySignUpFragment]";

    public RentingAgencySignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RentingAgencySignUpFragment.
     */
    public static RentingAgencySignUpFragment newInstance() {
        RentingAgencySignUpFragment fragment = new RentingAgencySignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositoryManager = RepositoryManager.getInstance(getActivity().getApplicationContext());
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity().getApplicationContext());
        sqLiteCountryRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getSqLiteCountryRepository();
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

        View view = inflater.inflate(R.layout.fragment_renting_agency_sign_up, container, false);
        ButterKnife.bind(this, view);
        this.initArrayAdapters();
        this.getCountries();
        this.initViews();
        return view;
    }

    private AgencyRegisterModel getAgencyInfo() {
        return new AgencyRegisterModel(
                emailEdit.getText().toString(),
                passwordEdit.getText().toString(),
                confirmPasswordEdit.getText().toString(),
                nameEdit.getText().toString(),
                ((Country) countrySpinner.getSelectedItem()).getId(),
                ((City) citySpinner.getSelectedItem()).getId(),
                phoneEdit.getText().toString()
        );
    }

    @OnClick(R.id.btn_agency_submit)
    void registerAgency() {
        if (RentingAgencySignUpFormManager.validateSignUpForm(this)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Registering your agency. Please wait...");
            disposables.add(repositoryManager.getAuthenticationRepository().registerAgency(RentingAgencySignUpFormManager.getAgencyData(this))
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
                        Log.e("POST Agency", error.getMessage());
                    }));
        }

    }

    private void getCountries() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...");
        disposables.add(repositoryManager.getCountryRepository().getCountries(0, 20, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((countryPage) -> {
                    progressDialog.hide();
                    countryArrayAdapter.clear();
                    countryArrayAdapter.addAll(countryPage.getContent());
                }, (error) -> {
                    progressDialog.hide();
                }));

        sqLiteCountryRepository.getCountries().forEach(country -> {
            Log.i(TAG, country.getId().toString() + " - " + country.getName() + ", " + country.getCountryCode());
        });
    }

    @OnItemSelected(R.id.spinner_agency_country)
    void getCities(Spinner spinner, int position) {
        phoneEdit.setText('+' + ((Country) countrySpinner.getSelectedItem()).getCountryCode());
        disposables.add(repositoryManager.getCityRepository().getCities(0, 20, null,
                countryArrayAdapter.getItem(position).getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((cityPage) -> {
                    cityArrayAdapter.clear();
                    cityArrayAdapter.addAll(cityPage.getContent());
                }, (error) -> {

                }));
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

    static class RentingAgencySignUpFormManager {

        private static final int AGENCY_NAME_MAX_CHARS = 20;
        private static final int AGENCY_NAME_MIN_CHARS = 5;

        static boolean validateSignUpForm(RentingAgencySignUpFragment rentingAgencySignUpFragment) {
            rentingAgencySignUpFragment.invalidNameText.setVisibility(View.GONE);
            rentingAgencySignUpFragment.invalidEmailText.setVisibility(View.GONE);
            rentingAgencySignUpFragment.invalidPasswordText.setVisibility(View.GONE);
            rentingAgencySignUpFragment.invalidConfirmPassText.setVisibility(View.GONE);
            rentingAgencySignUpFragment.invalidPhoneText.setVisibility(View.GONE);

            rentingAgencySignUpFragment.nameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            rentingAgencySignUpFragment.emailEdit.setBackgroundResource(R.drawable.edit_text_bg);
            rentingAgencySignUpFragment.passwordEdit.setBackgroundResource(R.drawable.edit_text_bg);
            rentingAgencySignUpFragment.confirmPasswordEdit.setBackgroundResource(R.drawable.edit_text_bg);
            rentingAgencySignUpFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg);

            boolean isValidForm = true;
            if (rentingAgencySignUpFragment.nameEdit.getText().toString().length() > AGENCY_NAME_MAX_CHARS ||
                rentingAgencySignUpFragment.nameEdit.getText().toString().length() < AGENCY_NAME_MIN_CHARS) {
                rentingAgencySignUpFragment.nameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                rentingAgencySignUpFragment.invalidNameText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (!rentingAgencySignUpFragment.emailEdit.getText().toString().matches(SharedKeys.EMAIL_REGEX)) {
                rentingAgencySignUpFragment.emailEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                rentingAgencySignUpFragment.invalidEmailText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (!rentingAgencySignUpFragment.passwordEdit.getText().toString().matches(SharedKeys.PASSWORD_REGEX)) {
                rentingAgencySignUpFragment.passwordEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                rentingAgencySignUpFragment.invalidPasswordText.setVisibility(View.VISIBLE);
                isValidForm = false;
            } else if (!rentingAgencySignUpFragment.confirmPasswordEdit.getText().toString().equals(rentingAgencySignUpFragment.passwordEdit.getText().toString())) {
                rentingAgencySignUpFragment.confirmPasswordEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                rentingAgencySignUpFragment.invalidConfirmPassText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (rentingAgencySignUpFragment.phoneEdit.getText().toString().length() < SharedKeys.PHONE_MIN_LENGTH) {
                rentingAgencySignUpFragment.phoneEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                rentingAgencySignUpFragment.invalidPhoneText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            return isValidForm;
        }

        static AgencyRegisterModel getAgencyData(RentingAgencySignUpFragment rentingAgencySignUpFragment) {
            return new AgencyRegisterModel(
                    rentingAgencySignUpFragment.emailEdit.getText().toString(),
                    rentingAgencySignUpFragment.passwordEdit.getText().toString(),
                    rentingAgencySignUpFragment.confirmPasswordEdit.getText().toString(),
                    rentingAgencySignUpFragment.nameEdit.getText().toString(),
                    ((Country) rentingAgencySignUpFragment.countrySpinner.getSelectedItem()).getId(),
                    ((City) rentingAgencySignUpFragment.citySpinner.getSelectedItem()).getId(),
                    rentingAgencySignUpFragment.phoneEdit.getText().toString()
            );
        }
    }
}