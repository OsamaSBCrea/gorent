package com.example.gorent.data.models;

import com.example.gorent.data.enums.RentStatus;

import java.time.Instant;
import java.util.Date;

public class Rent {

    private Long id;

    private Property property;

    private Tenant tenant;

    private Instant applicationDate;

    private RentStatus status;

    public Rent() {
    }

    public Rent(Long id, Property property, Tenant tenant, Instant applicationDate, RentStatus status) {
        this.id = id;
        this.property = property;
        this.tenant = tenant;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Instant getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Instant applicationDate) {
        this.applicationDate = applicationDate;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", property=" + property +
                ", tenant=" + tenant +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                '}';
    }
}
