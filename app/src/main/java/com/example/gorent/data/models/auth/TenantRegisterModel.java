package com.example.gorent.data.models.auth;

import com.example.gorent.data.enums.Nationality;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;

public class TenantRegisterModel extends RegisterModel {

    private String firstName;

    private String lastName;

    private String gender;

    private Nationality nationality;

    private Double grossMonthlySalary;

    private String occupation;

    private Integer familySize;

    private Long countryId;

    private Long cityId;

    private String phoneNumber;

    public TenantRegisterModel() {
    }

    public TenantRegisterModel(String email,
                               String password,
                               String confirmPassword,
                               String firstName,
                               String lastName,
                               String gender,
                               Nationality nationality,
                               Double grossMonthlySalary,
                               String occupation,
                               Integer familySize,
                               Long countryId,
                               Long cityId,
                               String phoneNumber) {
        super(email, password, confirmPassword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nationality = nationality;
        this.grossMonthlySalary = grossMonthlySalary;
        this.occupation = occupation;
        this.familySize = familySize;
        this.countryId = countryId;
        this.cityId = cityId;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Double getGrossMonthlySalary() {
        return grossMonthlySalary;
    }

    public void setGrossMonthlySalary(Double grossMonthlySalary) {
        this.grossMonthlySalary = grossMonthlySalary;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Integer getFamilySize() {
        return familySize;
    }

    public void setFamilySize(Integer familySize) {
        this.familySize = familySize;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
