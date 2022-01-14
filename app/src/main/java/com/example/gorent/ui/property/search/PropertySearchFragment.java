package com.example.gorent.ui.property.search;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gorent.MainActivity;
import com.example.gorent.R;
import com.example.gorent.data.enums.PropertySearchStatus;
import com.example.gorent.data.enums.PropertyStatus;
import com.example.gorent.data.models.PropertySearch;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.CityRepository;
import com.example.gorent.repository.api.PropertyRepository;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertySearchFragment extends Fragment {

    @BindView(R.id.spinner_search_property_city)
    Spinner citySpinner;

    @BindView(R.id.search_property_min_area)
    EditText minAreaEdit;

    @BindView(R.id.search_property_max_area)
    EditText maxAreaEdit;

    @BindView(R.id.search_property_min_bedrooms)
    EditText minBedroomsEdit;

    @BindView(R.id.search_property_max_bedrooms)
    EditText maxBedroomsEdit;

    @BindView(R.id.search_property_min_price)
    EditText minPriceEdit;

    @BindView(R.id.spinner_search_property_status)
    Spinner statusSpinner;

    @BindView(R.id.chk_search_property_balcony)
    CheckBox balconyCheckBox;

    @BindView(R.id.switch_search_property_balcony)
    SwitchCompat balconySwitch;

    @BindView(R.id.chk_search_property_garden)
    CheckBox gardenCheckBox;

    @BindView(R.id.switch_search_property_garden)
    SwitchCompat gardenSwitch;

    private List<Disposable> disposables;

    private CityRepository cityRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayAdapter<PropertySearchStatus> propertyStatusArrayAdapter;

    private PropertyRepository propertyRepository;

    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_property_search, container, false);
        ButterKnife.bind(this, root);
        disposables = new ArrayList<>();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity().getApplicationContext());
        cityRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getCityRepository();
        propertyRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getPropertyRepository();
        initViews();
        initAdapters();
        getCities();
        return root;
    }

    @Override
    public void onPause() {
        disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    private void initAdapters() {
        cityArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityArrayAdapter);

        propertyStatusArrayAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                PropertySearchStatus.values()
        );
        propertyStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(propertyStatusArrayAdapter);
    }

    private void initViews() {
        balconyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            balconySwitch.setEnabled(isChecked);
        });

        gardenCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gardenSwitch.setEnabled(isChecked);
        });
    }

    private void getCities() {
        disposables.add(cityRepository.getCities(0, 20, null,
                sharedPreferencesManager.getUser().getCountryId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(citiesPage -> {
                    cityArrayAdapter.addAll(citiesPage.getContent());
                }, error -> {
                    Log.e("GET cities", error.getMessage());
                    ToastMaker.showLongToast(getActivity(), R.string.alrt_get_cities_failed);
                }));
    }

    @OnClick(R.id.btn_search_property)
    void onSearch() {
        PropertySearch propertySearch = new PropertySearch();
        propertySearch.setCityId(((City) citySpinner.getSelectedItem()).getId());
        if (!minAreaEdit.getText().toString().isEmpty()) {
            propertySearch.setMinArea(Long.valueOf(minAreaEdit.getText().toString()));
        }

        if (!maxAreaEdit.getText().toString().isEmpty()) {
            propertySearch.setMaxArea(Long.valueOf(maxAreaEdit.getText().toString()));
        }

        if (!minBedroomsEdit.getText().toString().isEmpty()) {
            propertySearch.setMinBedrooms(Long.valueOf(minBedroomsEdit.getText().toString()));
        }

        if (!maxBedroomsEdit.getText().toString().isEmpty()) {
            propertySearch.setMaxBedrooms(Long.valueOf(maxBedroomsEdit.getText().toString()));
        }

        if (!minPriceEdit.getText().toString().isEmpty()) {
            propertySearch.setMinPrice(Long.valueOf(minPriceEdit.getText().toString()));
        }

        propertySearch.setStatus(((PropertySearchStatus) statusSpinner.getSelectedItem()));

        if (balconyCheckBox.isChecked()) {
            propertySearch.setHasBalcony(balconySwitch.isChecked());
        }

        if (gardenCheckBox.isChecked()) {
            propertySearch.setHasGarden(gardenSwitch.isChecked());
        }

        progressDialog = ProgressDialog.show(getActivity(), "", "Searching. Please wait...");
        disposables.add(propertyRepository.searchProperties(0, 20, propertySearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(propertyPage -> {
                    progressDialog.hide();
                    if (propertyPage.getContent().isEmpty()) {
                        ToastMaker.showLongToast(getActivity(), R.string.alrt_properties_not_found);
                    } else {
                        ((MainActivity) getActivity()).goToPropertyList(getView(), propertyPage, propertySearch);
                    }
                }, error -> {
                    progressDialog.hide();
                    Log.e("Search Properties", error.getMessage());
                    ToastMaker.showLongToast(getActivity(), R.string.alrt_search_properties);
                }));
    }

}