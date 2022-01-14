package com.example.gorent.ui.entities.rent.history;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.RentHistory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RentHistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rent_agency_name)
    TextView agencyNameText;

    @BindView(R.id.rent_tenant_name)
    TextView tenantNameText;

    @BindView(R.id.rent_period)
    TextView periodText;

    @BindView(R.id.rent_property_city)
    TextView propertyCityText;

    @BindView(R.id.rent_property_address)
    TextView propertyAddressText;

    @BindView(R.id.rent_start_date)
    TextView startDateText;

    @BindView(R.id.rent_status)
    TextView statusText;

    private RentHistory rentHistory;

    public RentHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setRentHistory(RentHistory rentHistory) {
        this.rentHistory = rentHistory;
    }

    public TextView getAgencyNameText() {
        return agencyNameText;
    }

    public TextView getTenantNameText() {
        return tenantNameText;
    }

    public TextView getPeriodText() {
        return periodText;
    }

    public TextView getPropertyCityText() {
        return propertyCityText;
    }

    public TextView getPropertyAddressText() {
        return propertyAddressText;
    }

    public TextView getStartDateText() {
        return startDateText;
    }

    public RentHistory getRentHistory() {
        return rentHistory;
    }

    public TextView getStatusText() {
        return statusText;
    }
}
