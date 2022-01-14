package com.example.gorent.data.models;

import com.example.gorent.data.enums.PropertyStatus;
import com.example.gorent.data.models.locale.City;

import java.io.Serializable;
import java.time.Instant;


public class Property implements Serializable {

    private Long id;

    private Agency agency;

    private String postalAddress;

    private Double surfaceArea;

    private Integer bedrooms;

    private PropertyStatus status;

    private Boolean hasBalcony;

    private Boolean hasGarden;

    private City city;

    private String constructionYear;

    private Double rentalPrice;

    private String description;

    private Instant availabilityDate;

    private Instant createdDate;

    public Property() {
    }

    public Property(Long id, Agency agency, String postalAddress, Double surfaceArea, Integer bedrooms, PropertyStatus status, Boolean hasBalcony, Boolean hasGarden, City city, String constructionYear, Double rentalPrice, String description, Instant availabilityDate, Instant createdDate) {
        this.id = id;
        this.agency = agency;
        this.postalAddress = postalAddress;
        this.surfaceArea = surfaceArea;
        this.bedrooms = bedrooms;
        this.status = status;
        this.hasBalcony = hasBalcony;
        this.hasGarden = hasGarden;
        this.city = city;
        this.constructionYear = constructionYear;
        this.rentalPrice = rentalPrice;
        this.description = description;
        this.availabilityDate = availabilityDate;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public Instant getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(Instant availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", agency=" + agency +
                ", postalAddress='" + postalAddress + '\'' +
                ", surfaceArea=" + surfaceArea +
                ", bedrooms=" + bedrooms +
                ", status=" + status +
                ", hasBalcony=" + hasBalcony +
                ", hasGarden=" + hasGarden +
                ", city=" + city +
                ", constructionYear='" + constructionYear + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", description='" + description + '\'' +
                ", availabilityDate=" + availabilityDate +
                ", createdDate=" + createdDate +
                '}';
    }
}
