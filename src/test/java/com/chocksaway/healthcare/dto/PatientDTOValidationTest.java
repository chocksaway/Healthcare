package com.chocksaway.healthcare.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientDTOValidationTest {
    private static Validator validator;
    private static ValidatorFactory validatorFactory;

    @BeforeAll
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void tearDown() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    public void validPatient_noViolations() {
        PatientDTO p = new PatientDTO();
        p.setId(1L);
        p.setGivenName("John");
        p.setFamilyName("Doe");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertThat(violations).isEmpty();
    }

    @Test
    public void missingId_violationsContainId() {
        PatientDTO p = new PatientDTO();
        p.setGivenName("John");
        p.setFamilyName("Doe");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> "id".equals(v.getPropertyPath().toString()));
    }

    @Test
    public void blankNames_violateNotBlank() {
        PatientDTO p = new PatientDTO();
        p.setId(2L);
        p.setGivenName("");
        p.setFamilyName("");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> "givenName".equals(v.getPropertyPath().toString()));
        assertThat(violations).anyMatch(v -> "familyName".equals(v.getPropertyPath().toString()));
    }

    @Test
    public void actionDto_cascadesToPatient() {
        PatientDTO p = new PatientDTO();
        p.setId(3L);
        p.setGivenName(""); // invalid
        p.setFamilyName("Smith");

        ActionDTO a = new ActionDTO();
        a.setPatient(p);

        Set<ConstraintViolation<ActionDTO>> violations = validator.validate(a);
        assertThat(violations).isNotEmpty();
        // property path should include patient.givenName
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("patient") && v.getPropertyPath().toString().contains("givenName"));
    }
}
