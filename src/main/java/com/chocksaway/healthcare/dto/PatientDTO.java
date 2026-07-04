package com.chocksaway.healthcare.dto;

public class PatientDTO {
    private Long id;
    private String givenName;
    private String familyName;
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

    public void validate() throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("PatientDTO id cannot be null");
        }
        if (givenName == null || givenName.isEmpty()) {
            throw new IllegalArgumentException("PatientDTO givenName cannot be null or empty");
        }
        if (familyName == null || familyName.isEmpty()) {
            throw new IllegalArgumentException("PatientDTO familyName cannot be null or empty");
        }
    }
}
