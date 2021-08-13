package edu.shmonin.university.model;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Human {

    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String gender;
    private long phone;
    private String address;
    private LocalDate birthdate;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Human human = (Human) o;

        if (phone != human.phone) return false;
        if (!Objects.equals(firstName, human.firstName)) return false;
        if (!Objects.equals(lastName, human.lastName)) return false;
        if (!Objects.equals(email, human.email)) return false;
        if (!Objects.equals(country, human.country)) return false;
        if (!Objects.equals(gender, human.gender)) return false;
        if (!Objects.equals(address, human.address)) return false;
        return Objects.equals(birthdate, human.birthdate);
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (int) (phone ^ (phone >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        return result;
    }
}
