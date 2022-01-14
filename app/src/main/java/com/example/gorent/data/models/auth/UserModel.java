package com.example.gorent.data.models.auth;

import com.example.gorent.data.enums.RentStatus;
import com.example.gorent.data.enums.UserRole;

public class UserModel {

    private Long id;

    private String email;

    private String name;

    private UserRole role;

    private Long agencyId;

    private Long tenantId;

    private Long countryId;

    private Long currentRentedPropertyId;

    private RentStatus currentRentStatus;

    public UserModel() {
    }

    public UserModel(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }

    public UserModel(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UserModel(Long id, String email, String name, UserRole role, Long countryId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.countryId = countryId;
    }

    public UserModel(Long id, String email, String name, UserRole role, Long agencyId, Long tenantId, Long countryId, Long currentRentedPropertyId, RentStatus currentRentStatus) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.agencyId = agencyId;
        this.tenantId = tenantId;
        this.countryId = countryId;
        this.currentRentedPropertyId = currentRentedPropertyId;
        this.currentRentStatus = currentRentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCurrentRentedPropertyId() {
        return currentRentedPropertyId;
    }

    public void setCurrentRentedPropertyId(Long currentRentedPropertyId) {
        this.currentRentedPropertyId = currentRentedPropertyId;
    }

    public RentStatus getCurrentRentStatus() {
        return currentRentStatus;
    }

    public void setCurrentRentStatus(RentStatus currentRentStatus) {
        this.currentRentStatus = currentRentStatus;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", agencyId=" + agencyId +
                ", tenantId=" + tenantId +
                ", countryId=" + countryId +
                '}';
    }
}
