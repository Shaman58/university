package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Human {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private Gender gender;
    private String phone;
    private String address;
    private LocalDate birthDate;

    protected Human() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Human)) return false;

        var human = (Human) o;

        if (!Objects.equals(firstName, human.firstName)) return false;
        if (!Objects.equals(lastName, human.lastName)) return false;
        if (gender != human.gender) return false;
        return Objects.equals(birthDate, human.birthDate);
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "firstName=" + firstName +
               ", lastName=" + lastName +
               ", email=" + email +
               ", country=" + country +
               ", gender=" + gender +
               ", phone=" + phone +
               ", address=" + address +
               ", birthDate=" + birthDate;
    }
}