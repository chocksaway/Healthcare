package com.chocksaway.healthcare.db.data.integrity;

import com.chocksaway.healthcare.domain.Action;
import com.chocksaway.healthcare.domain.Patient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
public class ActionPatientJoinTest {

    @Autowired
    private EntityManager em;

    @Test
    void testPatientActionsJoinAndOrder() {
        // advance DB sequences to avoid key collisions with existing data
        Number maxPatientIdN = (Number) em.createQuery("SELECT COALESCE(MAX(p.id), 0) FROM Patient p").getSingleResult();
        em.createNativeQuery("SELECT setval('patient_entity_id_seq', :val)")
                .setParameter("val", maxPatientIdN.longValue())
                .getSingleResult();

        Number maxActionIdN = (Number) em.createQuery("SELECT COALESCE(MAX(a.id), 0) FROM Action a").getSingleResult();
        em.createNativeQuery("SELECT setval('action_entity_id_seq', :val)")
                .setParameter("val", maxActionIdN.longValue())
                .getSingleResult();

        // create patient
        Patient p = new Patient();
        p.setEntityCreated(Instant.now());
        p.setEntityUpdated(Instant.now());
        p.setEntityVersion(0L);
        p.setWhenInvited(Instant.now());
        p.setGivenName("John");
        p.setFamilyName("Doe");
        p.setId1(UUID.randomUUID());
        em.persist(p);

        // create two actions referencing the patient
        Action a1 = new Action();
        a1.setEntityCreated(Instant.now());
        a1.setEntityUpdated(Instant.now());
        a1.setEntityVersion(0L);
        a1.setWhenRecorded(Instant.now().minusSeconds(60));
        a1.setId1(UUID.randomUUID());
        a1.setActivity("TASK-COMPLETED");
        a1.setContext("FATIGUE");
        a1.setModuleId("PROGRAMME");
        a1.setPatient(p);
        em.persist(a1);

        Action a2 = new Action();
        a2.setEntityCreated(Instant.now());
        a2.setEntityUpdated(Instant.now());
        a2.setEntityVersion(0L);
        a2.setWhenRecorded(Instant.now());
        a2.setId1(UUID.randomUUID());
        a2.setActivity("STARTED");
        a2.setContext("FATIGUE");
        a2.setModuleId("PROGRAMME");
        a2.setPatient(p);
        em.persist(a2);

        em.flush();
        em.clear();

        // JPQL join equivalent of the requested SQL
        List<Action> results = em
                .createQuery("SELECT a FROM Action a JOIN a.patient p WHERE p.id = :pid ORDER BY a.whenRecorded DESC", Action.class)
                .setParameter("pid", p.getId())
                .getResultList();

        // verify join returned both actions in expected order (a2 then a1)
        // expect a2 (more recent) first, then a1
        Assertions.assertThat(results.get(0).getId()).isEqualTo(a2.getId());
        Assertions.assertThat(results.get(1).getId()).isEqualTo(a1.getId());

        // also verify the patient relationship is set on the returned actions
        Assertions.assertThat(results.get(0).getPatient()).isNotNull();
        Assertions.assertThat(results.get(0).getPatient().getId()).isEqualTo(p.getId());

        assertFalse(results.isEmpty());
    }
}