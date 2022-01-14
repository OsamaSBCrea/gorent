package com.example.gorent.data.models;

import com.example.gorent.data.enums.PropertySearchStatus;

import java.io.Serializable;

public class PropertySearch implements Serializable {

    private Long cityId;

    private Long minArea;

    private Long maxArea;

    private Long minBedrooms;

    private Long maxBedrooms;

    private Long minPrice;

    private PropertySearchStatus status;

    private Boolean hasBalcony;

    private Boolean hasGarden;

    public PropertySearch() {
    }

    public PropertySearch(Long cityId, Long minArea, Long maxArea, Long minBedrooms, Long maxBedrooms, Long minPrice, PropertySearchStatus status, Boolean hasBalcony, Boolean hasGarden) {
        this.cityId = cityId;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.minBedrooms = minBedrooms;
        this.maxBedrooms = maxBedrooms;
        this.minPrice = minPrice;
        this.status = status;
        this.hasBalcony = hasBalcony;
        this.hasGarden = hasGarden;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getMinArea() {
        return minArea;
    }

    public void setMinArea(Long minArea) {
        this.minArea = minArea;
    }

    public Long getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(Long maxArea) {
        this.maxArea = maxArea;
    }

    public Long getMinBedrooms() {
        return minBedrooms;
    }

    public void setMinBedrooms(Long minBedrooms) {
        this.minBedrooms = minBedrooms;
    }

    public Long getMaxBedrooms() {
        return maxBedrooms;
    }

    public void setMaxBedrooms(Long maxBedrooms) {
        this.maxBedrooms = maxBedrooms;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public PropertySearchStatus getStatus() {
        return status;
    }

    public void setStatus(PropertySearchStatus status) {
        this.status = status;
    }

    public Boolean getHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(Boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public Boolean getHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(Boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    @Override
    public String toString() {
        return "PropertySearch{" +
                "cityId=" + cityId +
                ", minArea=" + minArea +
                ", maxArea=" + maxArea +
                ", minBedrooms=" + minBedrooms +
                ", maxBedrooms=" + maxBedrooms +
                ", minPrice=" + minPrice +
                ", status=" + status +
                ", hasBalcony=" + hasBalcony +
                ", hasGarden=" + hasGarden +
                '}';
    }
}
