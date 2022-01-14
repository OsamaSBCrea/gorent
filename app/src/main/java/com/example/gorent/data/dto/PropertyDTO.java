package com.example.gorent.data.dto;

import com.example.gorent.data.enums.PropertyStatus;
import com.example.gorent.data.models.Property;
import com.example.gorent.data.models.locale.City;

import java.time.Instant;

public class PropertyDTO {

    private String postalAddress;

    private Double surfaceArea;

    private Integer bedrooms;

    private PropertyStatus status;

    private Boolean hasBalcony;

    private Boolean hasGarden;

    private String constructionYear;

    private Double rentalPrice;

    private String description;

    private String availabilityDate;

    private Long cityId;

    public PropertyDTO() {
    }

    public PropertyDTO(String postalAddress, Double surfaceArea, Integer bedrooms, PropertyStatus status, Boolean hasBalcony, Boolean hasGarden, String constructionYear, Double rentalPrice, String description, String availabilityDate, Long cityId) {
        this.postalAddress = postalAddress;
        this.surfaceArea = surfaceArea;
        this.bedrooms = bedrooms;
        this.status = status;
        this.hasBalcony = hasBalcony;
        this.hasGarden = hasGarden;
        this.constructionYear = constructionYear;
        this.rentalPrice = rentalPrice;
        this.description = description;
        this.availabilityDate = availabilityDate;
        this.cityId = cityId;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public Double getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
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

    public String getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(String constructionYear) {
        this.constructionYear = constructionYear;
    }

    public Double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(Double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
