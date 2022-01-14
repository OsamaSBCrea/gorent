package com.example.gorent.data.models;

import com.example.gorent.data.enums.Gender;
import com.example.gorent.data.enums.Nationality;
import com.example.gorent.data.models.locale.City;
import com.example.gorent.data.models.locale.Country;

public class Tenant {

    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Nationality nationality;

    private Double grossMonthlySalary;

    private String occupation;

    private Integer familySize;

    private String phoneNumber;

    private Country country;

    private City city;

    public Tenant() {
    }

    public Tenant(Long id, String firstName, String lastName, Gender gender, Nationality nationality, Double grossMonthlySalary, String occupation, Integer familySize, String phoneNumber, Country country, City city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nationality = nationality;
        this.grossMonthlySalary = grossMonthlySalary;
        this.occupation = occupation;
        this.familySize = familySize;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", nationality=" + nationality +
                ", grossMonthlySalary=" + grossMonthlySalary +
                ", occupation='" + occupation + '\'' +
                ", familySize=" + familySize +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country=" + country +
                ", city=" + city +
                '}';
    }
}
