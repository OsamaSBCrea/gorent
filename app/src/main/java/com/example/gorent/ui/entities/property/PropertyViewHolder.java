package com.example.gorent.ui.entities.property;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.Property;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_property_address)
    TextView addressTextView;

    @BindView(R.id.text_property_city)
    TextView cityTextView;

    @BindView(R.id.text_rental_price)
    TextView rentalPriceTextView;

    @BindView(R.id.text_description)
    TextView descriptionTextView;

    @BindView(R.id.text_year)
    TextView constructionYearTextView;

    @BindView(R.id.text_available_date)
    TextView availableDateTextView;

    @BindView(R.id.text_surface_area)
    TextView surfaceAreaTextView;

    private Property property;

    public PropertyViewHolder(@NonNull View itemView, PropertyDetailsListener propertyDetailsListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> {
            propertyDetailsListener.showPropertyDetails(property);
        });
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public TextView getCityTextView() {
        return cityTextView;
    }

    public TextView getAddressTextView() {
        return addressTextView;
    }

    public TextView getRentalPriceTextView() {
        return rentalPriceTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

    public TextView getConstructionYearTextView() {
        return constructionYearTextView;
    }

    public TextView getAvailableDateTextView() {
        return availableDateTextView;
    }

    public TextView getSurfaceAreaTextView() {
        return surfaceAreaTextView;
    }
}
