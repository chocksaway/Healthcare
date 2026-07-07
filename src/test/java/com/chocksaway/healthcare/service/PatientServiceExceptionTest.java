package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.exception.ServiceException;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PatientServiceExceptionTest {

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
    void search_throwsServiceExceptionOnRepoError() {
        // service.search() sanitises the query (adds % and lowercases) before calling
        // the repository, so mock using anyString() to match the actual call.
        when(patientRepository.searchByQuery(anyString())).thenThrow(new RuntimeException("Error in search"));
        assertThrows(ServiceException.class, () -> service.search("q"));
    }

    @Test
    void listAll_throwsServiceExceptionOnRepoError() {
        when(patientRepository.findAll()).thenThrow(new RuntimeException("Error in findAll"));
        assertThrows(ServiceException.class, () -> service.listAll());
    }

    @Test
    void listPage_throwsServiceExceptionOnRepoError() {
        when(patientRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Error in findAll"));
        assertThrows(ServiceException.class, () -> service.listPage(0, 10));
    }

    @Test
    void getPatient_throwsServiceExceptionOnRepoError() {
        when(patientRepository.findById(1L)).thenThrow(new RuntimeException("Error in findById"));
        assertThrows(ServiceException.class, () -> service.getPatient(1L));
    }

    @Test
    void getActionsForPatient_throwsServiceExceptionOnRepoError() {
        when(actionRepository.findByPatientIdOrderByWhenRecordedDesc(1L)).thenThrow(new RuntimeException("Error in findByPatientIdOrderByWhenRecordedDesc"));
        assertThrows(ServiceException.class, () -> service.getActionsForPatient(1L));
    }

    @Test
    void countMethods_throwServiceExceptionOnRepoError() {
        when(patientRepository.countInvited()).thenThrow(new RuntimeException("Error in countInvited"));
        assertThrows(ServiceException.class, () -> service.countInvited());

        when(patientRepository.countRegistered()).thenThrow(new RuntimeException("Error in countRegistered"));
        assertThrows(ServiceException.class, () -> service.countRegistered());

        when(patientRepository.countDischarged()).thenThrow(new RuntimeException("Error in countDischarged"));
        assertThrows(ServiceException.class, () -> service.countDischarged());
    }
}

