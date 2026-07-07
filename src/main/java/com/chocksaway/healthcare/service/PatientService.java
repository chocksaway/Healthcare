package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.dto.ActionDTO;
import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final ActionRepository actionRepository;
    private final ModelMapper mapper;

    public PatientService(PatientRepository patientRepository, ActionRepository actionRepository, ModelMapper mapper) {
        this.patientRepository = patientRepository;
        this.actionRepository = actionRepository;
        this.mapper = mapper;
    }

    public List<PatientDTO> search(String q) {
        if (q == null || q.trim().isEmpty()) {
            return listAll();
        }
        return patientRepository.searchByQuery(q).stream()
                .map(p -> mapper.map(p, PatientDTO.class))
                .collect(Collectors.toList());
    }

    public List<PatientDTO> listAll() {
        return patientRepository.findAll().stream()
                .map(p -> mapper.map(p, PatientDTO.class))
                .collect(Collectors.toList());
    }

    public Page<PatientDTO> listPage(int page, int size) {
        Pageable pg = PageRequest.of(page, size);
        return patientRepository.findAll(pg)
                .map(p -> mapper.map(p, PatientDTO.class));
    }

    public Optional<PatientDTO> getPatient(Long entityId) {
        return patientRepository.findById(entityId)
                .map(p -> mapper.map(p, PatientDTO.class));
    }

    public List<ActionDTO> getActionsForPatient(Long entityId) {
        List<Action> actions = actionRepository.findByPatientIdOrderByWhenRecordedDesc(entityId);
        return actions.stream()
                .map(a -> mapper.map(a, ActionDTO.class))
                .collect(Collectors.toList());
    }

    public long countInvited(){ return patientRepository.countInvited(); }
    public long countRegistered(){ return patientRepository.countRegistered(); }
    public long countDischarged(){ return patientRepository.countDischarged(); }
}
