package com.chocksaway.healthcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PatientDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String givenName;
    @NotBlank
    private String familyName;

    // I'm not applying @NotBlank because of constraint on the database column, which allows null values
    // Please refer to DEV-LOG.md
    private String gender;

    public PatientDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

}
