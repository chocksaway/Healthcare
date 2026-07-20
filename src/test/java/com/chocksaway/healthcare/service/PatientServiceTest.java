package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.domain.Patient;
import com.chocksaway.healthcare.testutils.PatientTestBuilder;
import com.chocksaway.healthcare.testutils.ActionTestBuilder;
import com.chocksaway.healthcare.dto.ActionDTO;
import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository;
    private ActionRepository actionRepository;
    private ModelMapper mapper;
    private PatientService service;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        actionRepository = mock(ActionRepository.class);
        mapper = mock(ModelMapper.class);
        service = new PatientService(patientRepository, actionRepository, mapper);
    }

    @Test
    void listAll_mapsEntitiesToDTOs() {
        Patient p = PatientTestBuilder.aPatient().withId(123L).withGivenName("John").withFamilyName("Doe").build();
        PatientDTO dto = PatientTestBuilder.aPatient().withId(123L).withGivenName("John").withFamilyName("Doe").buildDTO();

        when(patientRepository.findAll()).thenReturn(List.of(p));
        when(mapper.map(p, PatientDTO.class)).thenReturn(dto);

        List<PatientDTO> result = service.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
        verify(patientRepository).findAll();
    }

    @Test
    void listPage_returnsPagedDTOs() {
        Patient p = PatientTestBuilder.aPatient().withId(1L).build();
        PatientDTO dto = PatientTestBuilder.aPatient().withId(1L).buildDTO();

        when(patientRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(p), PageRequest.of(0,10), 1));
        when(mapper.map(p, PatientDTO.class)).thenReturn(dto);

        Page<PatientDTO> result = service.listPage(0,10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().getFirst());
        verify(patientRepository).findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    void getPatient_returnsMappedDTO_orEmpty() {
        Patient p = new Patient(); p.setId(12L);
        PatientDTO dto = new PatientDTO(); dto.setId(12L);

        when(patientRepository.findById(12L)).thenReturn(Optional.of(p));
        when(mapper.map(p, PatientDTO.class)).thenReturn(dto);

        Optional<PatientDTO> res = service.getPatient(12L);
        assertTrue(res.isPresent());
        assertEquals(12L, res.get().getId());

        when(patientRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(service.getPatient(99L).isEmpty());
    }

    @Test
    void getActionsForPatient_mapsActions() {
        Action a = ActionTestBuilder.anAction().withId(11L).withActivity("act").build();
        ActionDTO dto = ActionTestBuilder.anAction().withId(11L).withActivity("act").buildDTO();

        when(actionRepository.findByPatientIdOrderByWhenRecordedDesc(5L)).thenReturn(List.of(a));
        when(mapper.map(a, ActionDTO.class)).thenReturn(dto);

        List<ActionDTO> res = service.getActionsForPatient(5L);
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(dto, res.getFirst());
    }

    @Test
    void counts_areDelegatedToRepository() {
        when(patientRepository.countByWhenInvitedIsNotNull()).thenReturn(7L);
        when(patientRepository.countByWhenRegisteredIsNotNull()).thenReturn(5L);
        when(patientRepository.countByWhenDischargedIsNotNull()).thenReturn(2L);

        assertEquals(7L, service.countInvited());
        assertEquals(5L, service.countRegistered());
        assertEquals(2L, service.countDischarged());
    }

    @Test
    void search_nullOrEmpty_returnsAll() {
        Patient p = PatientTestBuilder.aPatient().withId(1L).build();
        PatientDTO dto = PatientTestBuilder.aPatient().withId(1L).buildDTO();
        when(patientRepository.findAll()).thenReturn(List.of(p));
        when(mapper.map(p, PatientDTO.class)).thenReturn(dto);

        List<PatientDTO> r1 = service.search(null);
        assertEquals(1, r1.size());

        List<PatientDTO> r2 = service.search("   ");
        assertEquals(1, r2.size());
    }

    @Test
    void search_nonEmpty_usesRepositorySearch() {
        Patient p = PatientTestBuilder.aPatient().withId(2L).build();
        PatientDTO dto = PatientTestBuilder.aPatient().withId(2L).buildDTO();
        when(patientRepository.searchByQuery(anyString())).thenReturn(List.of(p));
        when(mapper.map(p, PatientDTO.class)).thenReturn(dto);

        List<PatientDTO> r = service.search("Bob");
        assertEquals(1, r.size());
        assertEquals(2L, r.getFirst().getId());
    }

    @Test
    void search_escapesWildcardsAndBackslash() {
        // ensure user wildcards and backslashes are escaped before passing to repository
        when(patientRepository.searchByQuery(anyString())).thenReturn(List.of());

        String input = "100%_\\Foo"; // contains %, _, and backslash
        service.search(input);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(patientRepository).searchByQuery(captor.capture());
        String passed = captor.getValue();

        // wrapped with % on both sides
        assertTrue(passed.startsWith("%"));
        assertTrue(passed.endsWith("%"));
        // lowercased input present
        assertTrue(passed.toLowerCase().contains("100"));
        // contains escaped percent, underscore and backslash sequences
        assertTrue(passed.contains("\\%"));
        assertTrue(passed.contains("\\_"));
        assertTrue(passed.contains("\\\\"));
    }
}

