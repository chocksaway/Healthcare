package com.chocksaway.healthcare.repository;


import com.chocksaway.healthcare.domain.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByPatientIdOrderByWhenRecordedDesc(Long patientId);
}
