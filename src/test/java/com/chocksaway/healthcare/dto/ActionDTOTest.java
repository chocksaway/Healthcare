package com.chocksaway.healthcare.dto;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionDTOTest {
    private ModelMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ModelMapper();
        mapper.getConfiguration().setPreferNestedProperties(false);
    }

    @Test
    public void testActionDTOMapping() {
        // create action
        Action action = new Action();
        action.setId(201L);

        Patient patient = new Patient();
        patient.setId(101L);
        patient.setGivenName("John");
        patient.setFamilyName("Doe");
        patient.setGender("MALE");
        action.setPatient(patient);

        Instant now = Instant.now();
        action.setWhenRecorded(now);
        action.setId1(UUID.randomUUID());
        action.setActivity("ACTIVITY");
        action.setContext("CTX");
        action.setModuleId("MOD1");

        // map action to actionDTO and verify selected fields are present
        ActionDTO actionDTO = this.mapper.map(action, ActionDTO.class);
        // we only display these on http://localhost:8080/patients/{id}
        // map patient to patientDTO and verify only selected fields are present
        assertNotNull(actionDTO);
        assertEquals(action.getId(), actionDTO.getId());
        assertNotNull(actionDTO.getPatient());
        assertEquals(patient.getGivenName(), actionDTO.getPatient().getGivenName());
        assertEquals(patient.getFamilyName(), actionDTO.getPatient().getFamilyName());
        assertEquals(action.getWhenRecorded(), actionDTO.getWhenRecorded());
        assertEquals(action.getActivity(), actionDTO.getActivity());
        assertEquals(action.getContext(), actionDTO.getContext());
        assertEquals(action.getModuleId(), actionDTO.getModuleId());
    }

    @Test
    public void testEmptyActionDTOMapping() {
        // create empty action
        Action action = new Action();

        // map action to actionDTO and verify fields are null
        ActionDTO actionDTO = this.mapper.map(action, ActionDTO.class);

        assertNotNull(actionDTO);
        // when mapping an empty domain object we expect DTO fields to be null
        assertNull(actionDTO.getId());
        assertNull(actionDTO.getPatient());
        assertNull(actionDTO.getWhenRecorded());
        assertNull(actionDTO.getActivity());
    }
}

