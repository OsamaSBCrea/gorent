package com.example.gorent.ui.entities.property;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.Property;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyViewHolder> {

    private List<Property> properties;

    private final PropertyDetailsListener propertyDetailsListener;

    public PropertyAdapter(List<Property> properties, PropertyDetailsListener propertyDetailsListener) {
        this.properties = properties;
        this.propertyDetailsListener = propertyDetailsListener;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_card, parent, false);
        return new PropertyViewHolder(view, propertyDetailsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = properties.get(position);

        holder.setProperty(property);
        holder.getCityTextView().setText(property.getCity().getName());
        holder.getAddressTextView().setText(property.getPostalAddress());
        holder.getRentalPriceTextView().setText(property.getRentalPrice().intValue() + "$/Month");
        holder.getDescriptionTextView().setText(property.getDescription());
        holder.getConstructionYearTextView().setText(property.getConstructionYear());
        holder.getAvailableDateTextView().setText("Available on: " +
                new SimpleDateFormat("dd-MM-yyyy").format(Date.from(property.getAvailabilityDate())));
        holder.getSurfaceAreaTextView().setText(property.getSurfaceArea().intValue() + "m");
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public void addAll(Collection<Property> properties) {
        this.properties.addAll(properties);
        notifyDataSetChanged();
    }

    public void setAll(List<Property> properties) {
        this.properties = properties;
        notifyDataSetChanged();
    }

}
