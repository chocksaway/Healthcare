package com.chocksaway.healthcare.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "action")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_entity_seq")
    @jakarta.persistence.SequenceGenerator(name = "action_entity_seq", sequenceName = "action_entity_id_seq", allocationSize = 1)
    @Column(name = "entity_id", nullable = false)
    private Long id;

    @Column(name = "entity_created", nullable = false)
    private Instant entityCreated;

    @Column(name = "entity_updated", nullable = false)
    private Instant entityUpdated;

    @Column(name = "entity_version", nullable = false)
    private Long entityVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_entity_id", referencedColumnName = "entity_id", nullable = false)
    private Patient patient;

    @Column(name = "when_recorded", nullable = false)
    private Instant whenRecorded;

    @Column(name = "id", nullable = false)
    private UUID externalId;

    @Column(name = "activity", nullable = false)
    private String activity;

    @Column(name = "context", nullable = false)
    private String context;

    @Column(name = "module_id", nullable = false)
    private String moduleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEntityCreated() {
        return entityCreated;
    }

    public void setEntityCreated(Instant entityCreated) {
        this.entityCreated = entityCreated;
    }

    public Instant getEntityUpdated() {
        return entityUpdated;
    }

    public void setEntityUpdated(Instant entityUpdated) {
        this.entityUpdated = entityUpdated;
    }

    public Long getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(Long entityVersion) {
        this.entityVersion = entityVersion;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Instant getWhenRecorded() {
        return whenRecorded;
    }

    public void setWhenRecorded(Instant whenRecorded) {
        this.whenRecorded = whenRecorded;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

}