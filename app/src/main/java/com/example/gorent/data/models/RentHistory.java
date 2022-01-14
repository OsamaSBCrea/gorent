package com.example.gorent.data.models;

import com.example.gorent.data.enums.RentStatus;
import com.example.gorent.data.models.locale.City;

import java.time.Instant;

public class RentHistory {

    private Long rentId;

    private String tenantName;

    private String agencyName;

    private City propertyCity;

    private String propertyAddress;

    private Instant rentDate;

    private Instant endDate;

    private RentStatus status;

    private Instant applicationDate;

    public RentHistory() {
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public City getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(City propertyCity) {
        this.propertyCity = propertyCity;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public Instant getRentDate() {
        return rentDate;
    }

    public void setRentDate(Instant rentDate) {
        this.rentDate = rentDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public String toString() {
        return "RentHistory{" +
                "rentId=" + rentId +
                ", tenantName='" + tenantName + '\'' +
                ", agencyName='" + agencyName + '\'' +
                ", propertyCity=" + propertyCity +
                ", propertyAddress='" + propertyAddress + '\'' +
                ", rentDate=" + rentDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", applicationDate=" + applicationDate +
                '}';
    }
}
