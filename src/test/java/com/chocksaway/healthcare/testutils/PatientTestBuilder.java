package com.chocksaway.healthcare.testutils;

import com.chocksaway.healthcare.domain.Patient;
import com.chocksaway.healthcare.dto.PatientDTO;

public class PatientTestBuilder {
    private Long id;
    private String givenName = "John";
    private String familyName = "Doe";
    private String gender = null;

    public static PatientTestBuilder aPatient() {
        return new PatientTestBuilder();
    }

    public PatientTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PatientTestBuilder withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public PatientTestBuilder withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public PatientTestBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Patient build() {
        Patient p = new Patient();
        if (id != null) p.setId(id);
        p.setGivenName(givenName);
        p.setFamilyName(familyName);
        p.setGender(gender);
        return p;
    }

    public PatientDTO buildDTO() {
        PatientDTO dto = new PatientDTO();
        if (id != null) dto.setId(id);
        dto.setGivenName(givenName);
        dto.setFamilyName(familyName);
        dto.setGender(gender);
        return dto;
    }
}

