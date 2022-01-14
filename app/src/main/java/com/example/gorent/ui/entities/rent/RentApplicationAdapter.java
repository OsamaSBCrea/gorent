package com.example.gorent.ui.entities.rent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.RentApplication;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

public class RentApplicationAdapter extends RecyclerView.Adapter<RentApplicationViewHolder> {

    private List<RentApplication> rentApplications;

    private final RentApplicationDetailsListener rentApplicationDetailsListener;

    public RentApplicationAdapter(List<RentApplication> rentApplications, RentApplicationDetailsListener rentApplicationDetailsListener) {
        this.rentApplications = rentApplications;
        this.rentApplicationDetailsListener = rentApplicationDetailsListener;
    }

    @NonNull
    @Override
    public RentApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rent_application_list_item, parent, false);
        return new RentApplicationViewHolder(view, rentApplicationDetailsListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RentApplicationViewHolder holder, int position) {
        RentApplication rentApplication = rentApplications.get(position);

        holder.setRentApplication(rentApplication);
        holder.getTenantNameText().setText(rentApplication.getTenantName());
        holder.getTenantAddressText().setText(rentApplication.getTenantAddress());
        holder.getApplicationDateText().setText("Applied on: " +
                new SimpleDateFormat("dd-MM-yyyy").format(Date.from(rentApplication.getApplicationDate())));
    }

    @Override
    public int getItemCount() {
        return this.rentApplications.size();
    }

    public void addAll(Collection<RentApplication> rentApplications) {
        this.rentApplications.addAll(rentApplications);
        notifyDataSetChanged();
    }

    public void setAll(List<RentApplication> rentApplications) {
        this.rentApplications = rentApplications;
        notifyDataSetChanged();
    }
}
