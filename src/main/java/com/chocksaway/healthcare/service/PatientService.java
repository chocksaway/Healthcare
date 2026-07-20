package com.chocksaway.healthcare.service;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.dto.ActionDTO;
import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.exception.ServiceException;
import com.chocksaway.healthcare.repository.ActionRepository;
import com.chocksaway.healthcare.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final ActionRepository actionRepository;
    private final ModelMapper mapper;

    public PatientService(PatientRepository patientRepository, ActionRepository actionRepository, ModelMapper mapper) {
        this.patientRepository = patientRepository;
        this.actionRepository = actionRepository;
        this.mapper = mapper;
    }

    public List<PatientDTO> search(String q) {
        log.debug("search(q={})", q);
        try {
            if (q == null || q.trim().isEmpty()) {
                return listAll();
            }

            final String likeParam = sanitize(q);
            return patientRepository.searchByQuery(likeParam).stream()
                    .map(p -> mapper.map(p, PatientDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Failed to search patients for q={}", q, ex);
            throw new ServiceException("Failed to search patients", ex);
        }
    }

    private String sanitize(final String q) {
        // sanitize and escape wildcard characters to avoid unexpected LIKE behavior
        String normalized = q.toLowerCase();
        // escape backslash first, then % and _
        normalized = normalized.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + normalized + "%";
    }

    public List<PatientDTO> listAll() {
        log.debug("listAll()");
        try {
            return patientRepository.findAll().stream()
                    .map(p -> mapper.map(p, PatientDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Failed to list all patients", ex);
            throw new ServiceException("Failed to list patients", ex);
        }
    }

    public Page<PatientDTO> listPage(int page, int size) {
        log.debug("listPage(page={}, size={})", page, size);
        try {
            Pageable pg = PageRequest.of(page, size);
            return patientRepository.findAll(pg)
                    .map(p -> mapper.map(p, PatientDTO.class));
        } catch (Exception ex) {
            log.error("Failed to list patient page page={} size={}", page, size, ex);
            throw new ServiceException("Failed to list patient page", ex);
        }
    }

    public Optional<PatientDTO> getPatient(Long entityId) {
        log.debug("getPatient(entityId={})", entityId);
        try {
            return patientRepository.findById(entityId)
                    .map(p -> mapper.map(p, PatientDTO.class));
        } catch (Exception ex) {
            log.error("Failed to fetch patient id={}", entityId, ex);
            throw new ServiceException("Failed to fetch patient", ex);
        }
    }

    public List<ActionDTO> getActionsForPatient(Long entityId) {
        log.debug("getActionsForPatient(entityId={})", entityId);
        try {
            List<Action> actions = actionRepository.findByPatientIdOrderByWhenRecordedDesc(entityId);
            return actions.stream()
                    .map(a -> mapper.map(a, ActionDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Failed to fetch actions for patient id={}", entityId, ex);
            throw new ServiceException("Failed to fetch patient actions", ex);
        }
    }

    public long countInvited(){
        try {
            return patientRepository.countByWhenInvitedIsNotNull();
        } catch (Exception ex) {
            log.error("Failed to count invited patients", ex);
            throw new ServiceException("Failed to count invited patients", ex);
        }
    }

    public long countRegistered(){
        try {
            return patientRepository.countByWhenRegisteredIsNotNull();
        } catch (Exception ex) {
            log.error("Failed to count registered patients", ex);
            throw new ServiceException("Failed to count registered patients", ex);
        }
    }

    public long countDischarged(){
        try {
            return patientRepository.countByWhenDischargedIsNotNull();
        } catch (Exception ex) {
            log.error("Failed to count discharged patients", ex);
            throw new ServiceException("Failed to count discharged patients", ex);
        }
    }
}
