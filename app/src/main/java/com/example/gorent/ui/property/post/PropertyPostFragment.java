package com.example.gorent.ui.property.post;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
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
import com.example.gorent.data.dto.PropertyDTO;
import com.example.gorent.data.enums.PropertyStatus;
import com.example.gorent.data.models.Property;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.CityRepository;
import com.example.gorent.repository.api.PropertyRepository;
import com.example.gorent.ui.property.PropertyDetailsActivity;
import com.example.gorent.ui.rent.RentalHistoryFragment;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;
import com.example.gorent.util.shared.keys.SharedKeys;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertyPostFragment extends Fragment {

    @BindView(R.id.spinner_property_city)
    Spinner citySpinner;

    @BindView(R.id.text_new_property_title)
    TextView propertyPostTitle;

    @BindView(R.id.new_property_address)
    EditText postalAddressEditText;

    @BindView(R.id.new_property_area)
    EditText areaEditText;

    @BindView(R.id.new_property_year)
    EditText yearEditText;

    @BindView(R.id.new_property_bedrooms)
    EditText bedroomsEditText;

    @BindView(R.id.new_property_price)
    EditText priceEditText;

    @BindView(R.id.new_property_date)
    CalendarView availabilityDateCalendar;

    @BindView(R.id.new_property_description)
    EditText descriptionEditText;

    @BindView(R.id.spinner_property_status)
    Spinner propertyStatusSpinner;

    @BindView(R.id.switch_property_balcony)
    SwitchCompat hasBalconySwitch;

    @BindView(R.id.switch_property_garden)
    SwitchCompat hasGardenSwitch;

    private Instant availabilityDate;

    private ArrayAdapter<City> cityArrayAdapter;

    private ArrayAdapter<PropertyStatus> propertyStatusArrayAdapter;

    private List<Disposable> disposables = new ArrayList<>();

    private CityRepository cityRepository;

    private PropertyRepository propertyRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private ProgressDialog progressDialog;

    private Property property;

    public static final String TAG = "PROPERTY_POST";

    public static PropertyPostFragment newInstance(Property property) {
        PropertyPostFragment fragment = new PropertyPostFragment();
        Bundle args = new Bundle();
        if (property != null) {
            args.putSerializable(SharedKeys.EDIT_PROPERTY, property);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_property_post, container, false);
        ButterKnife.bind(this, root);
        cityRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getCityRepository();
        propertyRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getPropertyRepository();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
        initAdapters();
        getCities();
        initViews();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(SharedKeys.EDIT_PROPERTY) != null) {
            this.property = (Property) bundle.getSerializable(SharedKeys.EDIT_PROPERTY);
            fillPropertyInfo();
        }
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
                PropertyStatus.values()
        );
        propertyStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyStatusSpinner.setAdapter(propertyStatusArrayAdapter);
    }

    private void getCities() {
        disposables.add(cityRepository.getCities(0, 20, null,
                sharedPreferencesManager.getUser().getCountryId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(citiesPage -> {
                    cityArrayAdapter.addAll(citiesPage.getContent());
                    if (property != null) {
                        citySpinner.setSelection(cityArrayAdapter.getPosition(property.getCity()));
                    }
                }, error -> {
                    Log.e("GET cities", error.getMessage());
                    ToastMaker.showLongToast(getActivity(), R.string.alrt_get_cities_failed);
                }));
    }

    private void initViews() {
        availabilityDateCalendar.setMinDate(Date.from(Instant.now()).getDate());
        availabilityDateCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            this.availabilityDate = calendar.toInstant();
        });
        availabilityDate = Instant.now();
    }

    private void fillPropertyInfo() {
        propertyPostTitle.setText(R.string.title_edit_property);
        postalAddressEditText.setText(property.getPostalAddress());
        areaEditText.setText(property.getSurfaceArea().toString());
        bedroomsEditText.setText(property.getBedrooms().toString());
        descriptionEditText.setText(property.getDescription());
        priceEditText.setText(property.getRentalPrice().toString());
        yearEditText.setText(property.getConstructionYear());
        propertyStatusSpinner.setSelection(propertyStatusArrayAdapter.getPosition(property.getStatus()));
        hasBalconySwitch.setChecked(property.getHasBalcony());
        hasGardenSwitch.setChecked(property.getHasGarden());
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                Date.from(property.getAvailabilityDate()).getYear(),
                Date.from(property.getAvailabilityDate()).getMonth(),
                Date.from(property.getAvailabilityDate()).getDay());
        availabilityDateCalendar.setDate(Date.from(property.getAvailabilityDate()).getTime(), true, true);
        availabilityDate = property.getAvailabilityDate();
    }

    @OnClick(R.id.btn_property_submit)
    void onPropertySubmit(View view) {
        if (PropertyFormManager.validatePropertyForm(this)) {
            if (this.property != null) {
                progressDialog = ProgressDialog.show(getActivity(), "", "Updating property. Please wait...");
                disposables.add(propertyRepository.updateProperty(this.property.getId(), PropertyFormManager.getPropertyFormValue(this))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            progressDialog.hide();
                            ((PropertyDetailsActivity) getActivity()).finish();
                        }, error -> {
                            progressDialog.hide();
                            Log.e(TAG, "PUT Property: " + error.getMessage());
                        }));
            } else {
                progressDialog = ProgressDialog.show(getActivity(), "", "Creating new property. Please wait...");
                disposables.add(propertyRepository.newProperty(PropertyFormManager.getPropertyFormValue(this))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(property -> {
                            progressDialog.hide();
                            ((MainActivity) getActivity()).goToPropertyList(view);
                        }, error -> {
                            progressDialog.hide();
                            Log.e(TAG, "POST Property: " + error.getMessage());
                            ToastMaker.showLongToast(getActivity(), R.string.alrt_post_new_property);
                        }));
            }
        } else {

        }
    }

    public Instant getAvailabilityDate() {
        return availabilityDate;
    }

    static class PropertyFormManager {

        static boolean validatePropertyForm(PropertyPostFragment propertyPostFragment) {
            return true;
        }

        static PropertyDTO getPropertyFormValue(PropertyPostFragment propertyPostFragment) {
            return new PropertyDTO(
                    propertyPostFragment.postalAddressEditText.getText().toString(),
                    Double.valueOf(propertyPostFragment.areaEditText.getText().toString()),
                    Integer.valueOf(propertyPostFragment.bedroomsEditText.getText().toString()),
                    (PropertyStatus) propertyPostFragment.propertyStatusSpinner.getSelectedItem(),
                    propertyPostFragment.hasBalconySwitch.isChecked(),
                    propertyPostFragment.hasGardenSwitch.isChecked(),
                    propertyPostFragment.yearEditText.getText().toString(),
                    Double.valueOf(propertyPostFragment.priceEditText.getText().toString()),
                    propertyPostFragment.descriptionEditText.getText().toString(),
                    propertyPostFragment.getAvailabilityDate().toString(),
                    ((City) propertyPostFragment.citySpinner.getSelectedItem()).getId()
            );
        }
    }
}