package com.example.gorent.data.models;

import com.example.gorent.data.enums.RentStatus;

import java.io.Serializable;
import java.time.Instant;

public class RentApplication implements Serializable {

    private Long tenantId;

    private String tenantName;

    private String tenantAddress;

    private Long rentId;

    private Long propertyId;

    private RentStatus status;

    private Instant applicationDate;

    public RentApplication() {
    }

    public RentApplication(Long tenantId, String tenantName, Long rentId, Long propertyId, RentStatus status, Instant applicationDate, String tenantAddress) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.tenantAddress = tenantAddress;
        this.rentId = rentId;
        this.propertyId = propertyId;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantAddress() {
        return tenantAddress;
    }

    public void setTenantAddress(String tenantAddress) {
        this.tenantAddress = tenantAddress;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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
}
