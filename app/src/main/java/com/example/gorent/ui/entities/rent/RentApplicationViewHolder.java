package com.example.gorent.ui.entities.rent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.RentApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RentApplicationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.application_tenant_name)
    TextView tenantNameText;

    @BindView(R.id.application_date)
    TextView applicationDateText;

    @BindView(R.id.application_tenant_address)
    TextView tenantAddressText;

    private RentApplication rentApplication;

    public RentApplicationViewHolder(@NonNull View itemView, RentApplicationDetailsListener rentApplicationDetailsListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            rentApplicationDetailsListener.showRentApplicationDetails(rentApplication);
        });
    }

    public void setRentApplication(RentApplication rentApplication) {
        this.rentApplication = rentApplication;
    }

    public TextView getTenantNameText() {
        return tenantNameText;
    }

    public TextView getApplicationDateText() {
        return applicationDateText;
    }

    public TextView getTenantAddressText() {
        return tenantAddressText;
    }
}
