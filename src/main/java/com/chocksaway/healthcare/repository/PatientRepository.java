package com.chocksaway.healthcare.repository;

import com.chocksaway.healthcare.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT count(p) FROM Patient p WHERE p.whenInvited IS NOT NULL")
    long countInvited();

    @Query("SELECT count(p) FROM Patient p WHERE p.whenRegistered IS NOT NULL")
    long countRegistered();

    @Query("SELECT count(p) FROM Patient p WHERE p.whenDischarged IS NOT NULL")
    long countDischarged();

    @Query("SELECT p FROM Patient p WHERE lower(p.givenName) LIKE :q ESCAPE '\\' OR lower(p.familyName) LIKE :q ESCAPE '\\' OR lower(p.nhsNumber) LIKE :q ESCAPE '\\' OR lower(p.hospitalId) LIKE :q ESCAPE '\\'")
    List<Patient> searchByQuery(@Param("q") String q);
}
