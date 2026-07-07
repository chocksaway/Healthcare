package com.chocksaway.healthcare.dto;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;

public class ActionDTO {
    private Long id;
    @Valid
    private PatientDTO patient;
    private Instant whenRecorded;
    private UUID externalId;
    private String activity;
    private String context;
    private String moduleId;

    public ActionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PatientDTO getPatient() { return patient; }
    public void setPatient(PatientDTO patient) { this.patient = patient; }

    public Instant getWhenRecorded() { return whenRecorded; }
    public void setWhenRecorded(Instant whenRecorded) { this.whenRecorded = whenRecorded; }

    public UUID getExternalId() { return externalId; }
    public void setExternalId(UUID externalId) { this.externalId = externalId; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
}
