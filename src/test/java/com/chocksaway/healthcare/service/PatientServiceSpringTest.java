package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.config.ModelMapperConfig;
import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.domain.Patient;
import com.chocksaway.healthcare.dto.ActionDTO;
import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import com.chocksaway.healthcare.testutils.PatientTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {PatientService.class, ModelMapperConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(PatientServiceSpringTest.TestConfig.class)
class PatientServiceSpringTest {

    @Autowired
    PatientService patientService;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ActionRepository actionRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PatientRepository patientRepository() {
            return Mockito.mock(PatientRepository.class);
        }

        @Bean
        public ActionRepository actionRepository() {
            return Mockito.mock(ActionRepository.class);
        }
    }

    @Test
    void listAll_shouldReturnMappedDTOs() {
        Patient p = PatientTestBuilder.aPatient().withId(42L).withGivenName("Alice").withFamilyName("Smith").build();

        when(patientRepository.findAll()).thenReturn(List.of(p));

        List<PatientDTO> all = patientService.listAll();
        assertThat(all).hasSize(1);
        assertThat(all.getFirst().getId()).isEqualTo(42L);
    }

    @Test
    void listPage_shouldReturnPagedDTOs() {
        Patient p = PatientTestBuilder.aPatient().withId(42L).withGivenName("Alice").withFamilyName("Smith").build();
        when(patientRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(p), PageRequest.of(0,10), 1));

        var page = patientService.listPage(0,10);
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void getPatient_shouldReturnMappedPatient() {
        Patient p = PatientTestBuilder.aPatient().withId(42L).withGivenName("Alice").withFamilyName("Smith").build();
        when(patientRepository.findById(42L)).thenReturn(Optional.of(p));

        Optional<PatientDTO> dtoOpt = patientService.getPatient(42L);
        assertThat(dtoOpt).isPresent();
        PatientDTO dto = dtoOpt.get();
        assertThat(dto.getGivenName()).isEqualTo("Alice");
    }

    @Test
    void getActionsForPatient_mapping() {
        Action a = new Action();
        a.setId(101L);
        a.setActivity("Check");
        a.setWhenRecorded(Instant.now());

        when(actionRepository.findByPatientIdOrderByWhenRecordedDesc(7L)).thenReturn(List.of(a));

        List<ActionDTO> res = patientService.getActionsForPatient(7L);
        assertThat(res).hasSize(1);
        assertThat(res.getFirst().getActivity()).isEqualTo("Check");
    }

    @Test
    void counts_delegate() {
        when(patientRepository.countInvited()).thenReturn(3L);
        when(patientRepository.countRegistered()).thenReturn(2L);
        when(patientRepository.countDischarged()).thenReturn(1L);

        assertThat(patientService.countInvited()).isEqualTo(3L);
        assertThat(patientService.countRegistered()).isEqualTo(2L);
        assertThat(patientService.countDischarged()).isEqualTo(1L);
    }

}

