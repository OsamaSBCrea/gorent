package com.example.gorent.data.models.auth;

public class AgencyRegisterModel extends RegisterModel {

    private String name;

    private Long countryId;

    private Long cityId;

    private String phoneNumber;

    public AgencyRegisterModel() {
    }

    public AgencyRegisterModel(String email, String password, String confirmPassword, String name, Long countryId, Long cityId, String phoneNumber) {
        super(email, password, confirmPassword);
        this.name = name;
        this.countryId = countryId;
        this.cityId = cityId;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
