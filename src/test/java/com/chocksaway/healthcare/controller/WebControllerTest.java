package com.chocksaway.healthcare.controller;

import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WebControllerTest {
    private MockMvc mockMvc;
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        patientService = mock(PatientService.class);
        WebController controller = new WebController(patientService);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @Test
    void search_withValidQuery_delegatesToService() throws Exception {
        PatientDTO dto = new PatientDTO();
        dto.setId(1L);
        dto.setGivenName("Bob");
        dto.setFamilyName("Smith");

        when(patientService.search("Bob")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/patients/search").param("q", "Bob"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).search("Bob");
    }

    @Test
    void search_withOversizedQuery_returnsBadRequestAndDoesNotCallService() throws Exception {
        String invalidInput = "a".repeat(300);

        mockMvc.perform(get("/api/patients/search").param("q", invalidInput))
                .andExpect(status().isBadRequest());

        verify(patientService, never()).search(anyString());
    }
}
