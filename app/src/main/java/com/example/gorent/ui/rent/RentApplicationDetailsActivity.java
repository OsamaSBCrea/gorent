package com.example.gorent.ui.rent;

import android.content.Intent;
import android.os.Bundle;

import com.example.gorent.MainActivity;
import com.example.gorent.data.models.RentApplication;
import com.example.gorent.data.models.RentHistory;
import com.example.gorent.data.models.Tenant;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.PropertyRepository;
import com.example.gorent.repository.api.RentRepository;
import com.example.gorent.repository.api.TenantRepository;
import com.example.gorent.ui.property.PropertyDetailsActivity;
import com.example.gorent.util.shared.ToastMaker;
import com.example.gorent.util.shared.keys.SharedKeys;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.gorent.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RentApplicationDetailsActivity extends AppCompatActivity {

    @BindView(R.id.application_history_details)
    FrameLayout applicationHistoryDetails;

    @BindView(R.id.application_details_tenant_name)
    TextView tenantNameText;

    @BindView(R.id.application_details_tenant_address)
    TextView tenantAddressText;

    @BindView(R.id.application_details_date)
    TextView applicationDateText;

    private AlertDialog approveAlertDialog;

    private AlertDialog rejectAlertDialog;

    private RentApplication rentApplication;

    private PropertyRepository propertyRepository;

    private RentRepository rentRepository;

    private final List<Disposable> disposables = new ArrayList<>();

    private static final String TAG = "[RentApplicationDetails]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_application_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        rentApplication = (RentApplication) intent.getExtras().get(SharedKeys.RENT_APPLICATION_DETAILS);
        rentRepository = RepositoryManager.getInstance(getApplicationContext()).getRentRepository();
        propertyRepository = RepositoryManager.getInstance(getApplicationContext()).getPropertyRepository();
        initAlertDialogs();
        fillInfo();
    }

    private void initAlertDialogs() {
        approveAlertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.confirm_approve_title).setMessage(R.string.confirm_approve_rent)
                .setPositiveButton("Yes", (dialog, which) -> {
                    disposables.add(rentRepository.approveRent(rentApplication.getRentId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                finish();
                            }));
                }).setNegativeButton("No", null).create();


        rejectAlertDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.confirm_reject_title).setMessage(R.string.confirm_reject_rent)
                .setPositiveButton("Yes", (dialog, which) -> {
                    disposables.add(rentRepository.rejectRent(rentApplication.getRentId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                finish();
                            }));
                }).setNegativeButton("No", null).create();
    }

    @Override
    protected void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void fillInfo() {
        tenantNameText.setText("Tenant Name: " + rentApplication.getTenantName());
        tenantAddressText.setText(rentApplication.getTenantAddress());
        applicationDateText.setText("Applied on: " + new SimpleDateFormat("dd/MM/yyyy").format(Date.from(rentApplication.getApplicationDate())));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.application_history_details, RentalHistoryFragment.newInstance(rentApplication))
                .commit();
    }

    @OnClick(R.id.btn_rent_approve)
    void onApproveClick() {
        approveAlertDialog.show();
    }

    @OnClick(R.id.btn_rent_reject)
    void onRejectClick() {
        rejectAlertDialog.show();
    }

    @OnClick(R.id.btn_application_details_property)
    void onViewPropertyClick() {
        disposables.add(propertyRepository.getPropertyById(rentApplication.getPropertyId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(property -> {
                    Intent intent = new Intent(RentApplicationDetailsActivity.this, PropertyDetailsActivity.class);
                    intent.putExtra(SharedKeys.PROPERTY_DETAILS_KEY, property);
                    startActivity(intent);
                }));
    }
}