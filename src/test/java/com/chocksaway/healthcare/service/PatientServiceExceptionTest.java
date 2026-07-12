package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.exception.ServiceException;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        RuntimeException cause = new RuntimeException("Error in search");
        when(patientRepository.searchByQuery(anyString())).thenThrow(cause);
        ServiceException ex = assertThrows(ServiceException.class, () -> service.search("q"));
        // verify cause was preserved
        assertNotNull(ex.getCause());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void listAll_throwsServiceExceptionOnRepoError() {
        RuntimeException cause = new RuntimeException("Error in findAll");
        when(patientRepository.findAll()).thenThrow(cause);
        ServiceException ex = assertThrows(ServiceException.class, () -> service.listAll());
        assertNotNull(ex.getCause());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void listPage_throwsServiceExceptionOnRepoError() {
        RuntimeException cause = new RuntimeException("Error in findAll");
        when(patientRepository.findAll(any(Pageable.class))).thenThrow(cause);
        ServiceException ex = assertThrows(ServiceException.class, () -> service.listPage(0, 10));
        assertNotNull(ex.getCause());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void getPatient_throwsServiceExceptionOnRepoError() {
        RuntimeException cause = new RuntimeException("Error in findById");
        when(patientRepository.findById(1L)).thenThrow(cause);
        ServiceException ex = assertThrows(ServiceException.class, () -> service.getPatient(1L));
        assertNotNull(ex.getCause());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void getActionsForPatient_throwsServiceExceptionOnRepoError() {
        RuntimeException cause = new RuntimeException("Error in findByPatientIdOrderByWhenRecordedDesc");
        when(actionRepository.findByPatientIdOrderByWhenRecordedDesc(1L)).thenThrow(cause);
        ServiceException ex = assertThrows(ServiceException.class, () -> service.getActionsForPatient(1L));
        assertNotNull(ex.getCause());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void countMethods_throwServiceExceptionOnRepoError() {
        RuntimeException cause1 = new RuntimeException("Error in countInvited");
        when(patientRepository.countInvited()).thenThrow(cause1);
        ServiceException ex1 = assertThrows(ServiceException.class, () -> service.countInvited());
        assertNotNull(ex1.getCause());
        assertEquals(cause1, ex1.getCause());

        RuntimeException cause2 = new RuntimeException("Error in countRegistered");
        when(patientRepository.countRegistered()).thenThrow(cause2);
        ServiceException ex2 = assertThrows(ServiceException.class, () -> service.countRegistered());
        assertNotNull(ex2.getCause());
        assertEquals(cause2, ex2.getCause());

        RuntimeException cause3 = new RuntimeException("Error in countDischarged");
        when(patientRepository.countDischarged()).thenThrow(cause3);
        ServiceException ex3 = assertThrows(ServiceException.class, () -> service.countDischarged());
        assertNotNull(ex3.getCause());
        assertEquals(cause3, ex3.getCause());
    }
}

