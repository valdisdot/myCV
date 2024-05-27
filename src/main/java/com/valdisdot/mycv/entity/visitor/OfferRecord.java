package com.valdisdot.mycv.entity.visitor;

import com.valdisdot.mycv.entity.Identifiable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

//for offers
public class OfferRecord extends Identifiable {

    private final Long createdAt;
    @NotBlank
    private String firstName;
    private String middleName;
    private String lastName;
    private String company;
    @NotBlank
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[- .]?[0-9]{3}[- .]?[0-9]{4,6}$")
    private String phone;
    @Email
    private String email;
    @NotBlank
    private String offer;

    public OfferRecord(Long id, String firstName, String middleName, String lastName, String company, String phone, String email, String offer, Long createdAt) {
        super(id);
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.offer = offer;
        this.createdAt = createdAt;
    }

    public OfferRecord(String firstName, String middleName, String lastName, String company, String phone, String email, String offer) {
        this(null, firstName, middleName, lastName, company, phone, email, offer, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public OfferRecord() {
        super(null);
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[- .]?[0-9]{3}[- .]?[0-9]{4,6}$") String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(@NotBlank String offer) {
        this.offer = offer;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
