package com.chocksaway.healthcare.dto;

import com.chocksaway.healthcare.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PatientDTOTest {
    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
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

    @Test
    public void testInvalidPatientDTOMapping() {
        // create patient
        Patient patient = new Patient();

        // map patient to patientDTO and verify only selected fields are present
        PatientDTO patientDTO = this.mapper.map(patient, PatientDTO.class);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                patientDTO::validate
        );
    }
}