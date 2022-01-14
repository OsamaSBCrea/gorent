package com.example.gorent.ui.property;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.gorent.R;
import com.example.gorent.data.enums.UserRole;
import com.example.gorent.data.models.Property;
import com.example.gorent.data.models.auth.UserModel;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.PropertyRepository;
import com.example.gorent.ui.property.post.PropertyPostFragment;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;
import com.example.gorent.util.shared.keys.SharedKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertyDetailsActivity extends AppCompatActivity {

    @BindView(R.id.image_property_details_thumb)
    ImageView propertyThumb;

    @BindView(R.id.property_details_city)
    TextView cityTextView;

    @BindView(R.id.property_details_address)
    TextView postalAddressTextView;

    @BindView(R.id.property_details_price)
    TextView priceTextView;

    @BindView(R.id.property_details_area)
    TextView areaTextView;

    @BindView(R.id.property_details_bedrooms)
    TextView bedroomsTextView;

    @BindView(R.id.property_details_date)
    TextView dateTextView;

    @BindView(R.id.property_details_year)
    TextView yearTextView;

    @BindView(R.id.property_details_agency)
    TextView agencyTextView;

    @BindView(R.id.property_details_status)
    TextView statusTextView;

    @BindView(R.id.property_details_description)
    TextView descriptionTextView;

    @BindView(R.id.img_has_balcony_yes)
    ImageView hasBalconyImg;

    @BindView(R.id.img_has_balcony_no)
    ImageView hasNoBalconyImg;

    @BindView(R.id.img_has_garden_yes)
    ImageView hasGardenImg;

    @BindView(R.id.img_has_garden_no)
    ImageView hasNoGardenImg;

    @BindView(R.id.btn_property_edit)
    ImageButton editBtn;

    @BindView(R.id.btn_property_delete)
    ImageButton deleteBtn;

    @BindView(R.id.btn_property_rent)
    Button rentBtn;

    @BindView(R.id.property_details_scroll)
    ScrollView propertyDetailsScroll;

    @BindView(R.id.edit_property_container)
    FrameLayout editPropertyFragmentContainer;

    private UserModel currentUser;

    private Property property;

    private PropertyRepository propertyRepository;

    private List<Disposable> disposables;

    private AlertDialog deleteAlertDialog;

    private AlertDialog rentAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        ButterKnife.bind(this);
        currentUser = SharedPreferencesManager.getInstance(getApplicationContext()).getUser();
        propertyRepository = RepositoryManager.getInstance(getApplicationContext()).getPropertyRepository();
        disposables = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        property = (Property) intent.getExtras().get(SharedKeys.PROPERTY_DETAILS_KEY);
        fillPropertyDetails();
        initAlertDialogs();
    }

    private void initAlertDialogs() {
        deleteAlertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_delete_title).setMessage(R.string.confirm_delete_property)
                .setPositiveButton("Yes", (dialog, which) -> {
                    disposables.add(propertyRepository.deleteProperty(property.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                finish();
                            }, error -> {
                                Log.e("DELETE Property", error.getMessage());
                                ToastMaker.showLongToast(PropertyDetailsActivity.this, R.string.alrt_delete_property);
                            }));
                }).setNegativeButton("No", null).create();


        rentAlertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.confirm_rent_title).setMessage(R.string.confirm_rent_property)
                .setPositiveButton("Yes", (dialog, which) -> {
                    disposables.add(propertyRepository.rentProperty(property.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                finish();
                            }, error -> {
                                Log.e("Rent Property", error.getMessage());
                                ToastMaker.showLongToast(PropertyDetailsActivity.this, R.string.alrt_rent_property);
                            }));
                }).setNegativeButton("No", null).create();
    }

    private void fillPropertyDetails() {
        cityTextView.setText(property.getCity().getName());
        postalAddressTextView.setText(property.getPostalAddress());
        priceTextView.setText(property.getRentalPrice() + "$/Month");
        areaTextView.append(": " + property.getSurfaceArea() + "m");
        bedroomsTextView.setText("Bedrooms: ");
        bedroomsTextView.append(property.getBedrooms().toString());
        yearTextView.append(": " + property.getConstructionYear());
        statusTextView.setText(property.getStatus().toString());
        descriptionTextView.setText("Description: " + property.getDescription());
        dateTextView.setText("Availability Date: " + new SimpleDateFormat("dd-MM-yyyy").format(Date.from(property.getAvailabilityDate())));
        agencyTextView.setText("Posted by: " + property.getAgency().getName());
        if (property.getHasBalcony()) {
            hasNoBalconyImg.setVisibility(View.GONE);
        } else {
            hasBalconyImg.setVisibility(View.GONE);
        }

        if (property.getHasGarden()) {
            hasNoGardenImg.setVisibility(View.GONE);
        } else {
            hasGardenImg.setVisibility(View.GONE);
        }

        if (currentUser.getRole() == UserRole.RENTING_AGENCY) {
            if (!property.getAgency().getId().equals(currentUser.getAgencyId())) {
                editBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.GONE);
            } else {
                editBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }
            rentBtn.setVisibility(View.GONE);
        } else if (currentUser.getRole() == UserRole.TENANT) {
            editBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            rentBtn.setVisibility(View.VISIBLE);
            if (currentUser.getCurrentRentedPropertyId() != null && currentUser.getCurrentRentedPropertyId().equals(property.getId())) {
                switch (currentUser.getCurrentRentStatus()) {
                    case PENDING:
                    case NEW_RENT:
                        rentBtn.setText(R.string.action_applied_disabled);
                        rentBtn.setEnabled(false);
                        break;
                    case APPROVED:
                        rentBtn.setText(R.string.action_approved_disabled);
                        rentBtn.setEnabled(false);
                        break;
                    case IN_RENT:
                        rentBtn.setText(R.string.action_rented_disabled);
                        rentBtn.setEnabled(false);
                        break;
                }
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (propertyDetailsScroll.getVisibility() == View.GONE) {
            propertyDetailsScroll.setVisibility(View.VISIBLE);
            propertyThumb.setVisibility(View.VISIBLE);
            editPropertyFragmentContainer.setVisibility(View.GONE);
            return false;
        } else {
            finish();
            return true;
        }
    }

    @OnClick(R.id.btn_property_delete)
    void onDelete() {
        deleteAlertDialog.show();
    }

    @OnClick(R.id.btn_property_edit)
    void onEdit() {
        propertyDetailsScroll.setVisibility(View.GONE);
        propertyThumb.setVisibility(View.GONE);
        editPropertyFragmentContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.edit_property_container,
                        PropertyPostFragment.newInstance(this.property), PropertyPostFragment.TAG)
                .commit();
    }

    @OnClick(R.id.btn_property_rent)
    void onRent() {
        rentAlertDialog.show();
    }
}