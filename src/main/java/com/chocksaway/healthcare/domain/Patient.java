package com.chocksaway.healthcare.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_entity_seq")
    @jakarta.persistence.SequenceGenerator(name = "patient_entity_seq", sequenceName = "patient_entity_id_seq", allocationSize = 1)
    @Column(name = "entity_id", nullable = false)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "entity_created", nullable = false)
    private Instant entityCreated;

    @Column(name = "entity_updated", nullable = false)
    private Instant entityUpdated;

    @Column(name = "entity_version", nullable = false)
    private Long entityVersion;

    @Column(name = "when_discharged")
    private Instant whenDischarged;

    @Column(name = "when_invited", nullable = false)
    private Instant whenInvited;

    @Column(name = "when_registered")
    private Instant whenRegistered;

    @Column(name = "id", nullable = false)
    private UUID externalId;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "hospital_id")
    private String hospitalId;

    @Column(name = "nhs_number")
    private String nhsNumber;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Action> actions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public Instant getWhenDischarged() {
        return whenDischarged;
    }

    public void setWhenDischarged(Instant whenDischarged) {
        this.whenDischarged = whenDischarged;
    }

    public Instant getWhenInvited() {
        return whenInvited;
    }

    public void setWhenInvited(Instant whenInvited) {
        this.whenInvited = whenInvited;
    }

    public Instant getWhenRegistered() {
        return whenRegistered;
    }

    public void setWhenRegistered(Instant whenRegistered) {
        this.whenRegistered = whenRegistered;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(String nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        actions.add(action);
        action.setPatient(this);
    }

    public void removeAction(Action action) {
        actions.remove(action);
        action.setPatient(null);
    }

}