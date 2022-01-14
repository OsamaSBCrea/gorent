package com.example.gorent.ui.entities.rent.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.RentHistory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class RentHistoryAdapter extends RecyclerView.Adapter<RentHistoryViewHolder> {

    private List<RentHistory> rentHistoryList;

    private boolean isAgencyHistory = false;

    public RentHistoryAdapter(List<RentHistory> rentHistoryList, boolean isAgencyHistory) {
        this.rentHistoryList = rentHistoryList;
        this.isAgencyHistory = isAgencyHistory;
    }

    @NonNull
    @Override
    public RentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rent_history_list_item, parent, false);
        return new RentHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentHistoryViewHolder holder, int position) {
        RentHistory rentHistory = this.rentHistoryList.get(position);

        holder.setRentHistory(rentHistory);
        if (this.isAgencyHistory) {
            holder.getTenantNameText().setText("Rented by: " + rentHistory.getTenantName());
        } else {
            holder.getAgencyNameText().setText("Rented from: " + rentHistory.getAgencyName());
        }

        holder.getPropertyCityText().setText(rentHistory.getPropertyCity().getName());
        holder.getPropertyAddressText().setText(rentHistory.getPropertyAddress());
        holder.getStartDateText().setText("Applied on: " +
                new SimpleDateFormat("dd-MM-yyyy").format(Date.from(rentHistory.getApplicationDate())));
        if (rentHistory.getStatus() != null) {
            switch (rentHistory.getStatus()) {
                case NEW_RENT:
                case PENDING:
                case REJECTED:
                case CANCELLED:
                    holder.getPeriodText().setText("Not rented");
                    break;
                case APPROVED:
                    holder.getPeriodText().setText("Not rented yet");
                    break;
                case IN_RENT:
                    holder.getPeriodText().setText("Rented since 4 months");
                    break;
                case COMPLETED:
                    holder.getPeriodText().setText("Rented for 2 years");
                    break;
            }
            holder.getStatusText().setText(rentHistory.getStatus().toString());
        }
    }

    @Override
    public int getItemCount() {
        return rentHistoryList.size();
    }

    public void setAll(List<RentHistory> rentHistoryList) {
        this.rentHistoryList = rentHistoryList;
        notifyDataSetChanged();
    }
}
