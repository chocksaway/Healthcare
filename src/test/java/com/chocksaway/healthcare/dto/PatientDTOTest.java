package com.chocksaway.healthcare.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import com.chocksaway.healthcare.testutils.ValidationTestFixture;
import com.chocksaway.healthcare.testutils.ModelMapperTestFixture;
import com.chocksaway.healthcare.domain.Patient;
import java.util.Set;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PatientDTOTest {
    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = ModelMapperTestFixture.createConfiguredMapper();
        mapper.getConfiguration().setPreferNestedProperties(false);
    }

    @Test
    public void testPatientDTOMapping() {
        // create patient
        Patient patient = new Patient();
        patient.setId(101L);
        patient.setGivenName("John");
        patient.setFamilyName("Doe");
        patient.setGender("MALE");
        Instant now = Instant.now();
        patient.setEntityCreated(now);
        patient.setEntityUpdated(now);
        patient.setEntityVersion(1L);
        patient.setWhenInvited(now);
        patient.setHospitalId("HOSP1");
        patient.setNhsNumber("NHS-123");
        patient.setTitle("Mr");

        // map patient to patientDTO and verify only selected fields are present
        PatientDTO patientDTO = this.mapper.map(patient, PatientDTO.class);

        assertNotNull(patientDTO);
        // we only display these on http://localhost:8080/patients
        assertEquals(patient.getId(), patientDTO.getId());
        assertEquals(patient.getGivenName(), patientDTO.getGivenName());
        assertEquals(patient.getFamilyName(), patientDTO.getFamilyName());
        assertEquals(patient.getGender(), patientDTO.getGender());
    }

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        validator = ValidationTestFixture.createValidator();
    }

    @Test
    public void validPatient_noViolations() {
        PatientDTO p = new PatientDTO();
        p.setId(1L);
        p.setGivenName("Alice");
        p.setFamilyName("Jones");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void missingId_hasNotNullViolation() {
        PatientDTO p = new PatientDTO();
        p.setGivenName("Alice");
        p.setFamilyName("Jones");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "id".equals(v.getPropertyPath().toString())));
    }

    @Test
    public void blankNames_haveNotBlankViolations() {
        PatientDTO p = new PatientDTO();
        p.setId(2L);
        p.setGivenName("");
        p.setFamilyName(" ");

        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "givenName".equals(v.getPropertyPath().toString())));
        assertTrue(violations.stream().anyMatch(v -> "familyName".equals(v.getPropertyPath().toString())));
    }
}