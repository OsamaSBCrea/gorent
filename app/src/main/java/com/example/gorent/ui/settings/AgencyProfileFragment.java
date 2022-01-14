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
import android.widget.EditText;
import android.widget.TextView;

import com.example.gorent.R;
import com.example.gorent.data.models.Agency;
import com.example.gorent.data.models.auth.AgencyRegisterModel;
import com.example.gorent.data.models.auth.UserModel;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.AgencyRepository;
import com.example.gorent.ui.auth.RentingAgencySignUpFragment;
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
 * Use the {@link AgencyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgencyProfileFragment extends Fragment {

    @BindView(R.id.agency_profile_email)
    TextView agencyEmailText;

    @BindView(R.id.agency_profile_country)
    TextView agencyCountryText;

    @BindView(R.id.agency_profile_city)
    TextView agencyCityText;

    @BindView(R.id.edit_agency_profile_name)
    EditText agencyNameEdit;

    @BindView(R.id.edit_agency_profile_phone)
    EditText agencyPhoneEdit;

    @BindView(R.id.agency_profile_invalid_name)
    TextView invalidNameText;

    @BindView(R.id.agency_profile_invalid_phone)
    TextView invalidPhoneText;

    private ProgressDialog progressDialog;

    private SharedPreferencesManager sharedPreferencesManager;

    private AgencyRepository agencyRepository;

    private Agency agency;

    private UserModel userModel;

    private List<Disposable> disposables;

    public static final String TAG = "[AgencyProfileFragment]";

    public AgencyProfileFragment() {
        // Required empty public constructor
    }

    public static AgencyProfileFragment newInstance() {
        AgencyProfileFragment fragment = new AgencyProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity().getApplicationContext());
        userModel = sharedPreferencesManager.getUser();
        agencyRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getAgencyRepository();
        disposables = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agency_profile, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onPause() {
        disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getAgency();
    }

    private void getAgency() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...");
        disposables.add(agencyRepository.getAgencyById(userModel.getAgencyId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(agency -> {
                    this.agency = agency;
                    agencyEmailText.setText(userModel.getEmail());
                    agencyCountryText.setText(agency.getCountry().getName());
                    agencyCityText.setText(agency.getCity().getName());
                    agencyPhoneEdit.setText(agency.getPhoneNumber());
                    agencyNameEdit.setText(agency.getName());
                    progressDialog.hide();
                }, error -> {
                    Log.e(TAG, "GET Agency: " + error.getMessage());
                    progressDialog.hide();
                }));
    }

    @OnClick(R.id.btn_agency_edit)
    void onSubmitClick() {
        if (AgencyProfileFormManager.validateSignUpForm(this)) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Submitting. Please wait...");
            disposables.add(agencyRepository.updateAgency(agency.getId(), agency)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(agency -> {
                        sharedPreferencesManager.put(SharedPreferencesKey.USER_NAME, agency.getName());
                        progressDialog.hide();
                        if (getActivity() instanceof SettingsActivity) {
                            getActivity().finish();
                        }
                    }, error -> {
                        progressDialog.hide();
                        Log.e(TAG, "PUT agency: " + error.getMessage());
                    }));
        }
    }

    private void initViews() {
        this.agencyPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String countryCode = '+' + agency.getCountry().getCountryCode();
                if (!s.toString().startsWith(countryCode)) {
                    agencyPhoneEdit.setText(countryCode);
                    Selection.setSelection(agencyPhoneEdit.getText(), agencyPhoneEdit.getText().length());
                }
            }
        });
    }

    static class AgencyProfileFormManager {

        private static final int AGENCY_NAME_MAX_CHARS = 20;
        private static final int AGENCY_NAME_MIN_CHARS = 5;

        static boolean validateSignUpForm(AgencyProfileFragment agencyProfileFragment) {
            agencyProfileFragment.invalidNameText.setVisibility(View.GONE);
            agencyProfileFragment.invalidPhoneText.setVisibility(View.GONE);

            agencyProfileFragment.agencyNameEdit.setBackgroundResource(R.drawable.edit_text_bg);
            agencyProfileFragment.agencyPhoneEdit.setBackgroundResource(R.drawable.edit_text_bg);

            boolean isValidForm = true;
            if (agencyProfileFragment.agencyNameEdit.getText().toString().length() > AGENCY_NAME_MAX_CHARS ||
                    agencyProfileFragment.agencyNameEdit.getText().toString().length() < AGENCY_NAME_MIN_CHARS) {
                agencyProfileFragment.agencyNameEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                agencyProfileFragment.invalidNameText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }
            if (agencyProfileFragment.agencyPhoneEdit.getText().toString().length() < SharedKeys.PHONE_MIN_LENGTH) {
                agencyProfileFragment.agencyPhoneEdit.setBackgroundResource(R.drawable.edit_text_bg_invalid);
                agencyProfileFragment.invalidPhoneText.setVisibility(View.VISIBLE);
                isValidForm = false;
            }

            if (!isValidForm) {
                return false;
            }

            agencyProfileFragment.agency.setName(agencyProfileFragment.agencyNameEdit.getText().toString());
            agencyProfileFragment.agency.setPhoneNumber(agencyProfileFragment.agencyPhoneEdit.getText().toString());
            return isValidForm;
        }
    }
}