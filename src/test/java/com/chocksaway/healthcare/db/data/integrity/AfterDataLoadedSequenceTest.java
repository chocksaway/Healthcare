package com.chocksaway.healthcare.db.data.integrity;

import com.chocksaway.healthcare.HealthcareApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootTest(classes = HealthcareApplication.class)
public class AfterDataLoadedSequenceTest {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Test
    public void testPatientAndActionSequences() throws Exception {
        try (Connection c = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement ps = c.prepareStatement("SELECT last_value FROM mydb.public.action_seq")) {
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "action_seq returned no rows");
                    assertEquals(3201L, rs.getLong("last_value"), "action_seq value mismatch");
                }
            }

            try (PreparedStatement ps = c.prepareStatement("SELECT last_value FROM mydb.public.patient_seq")) {
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "patient_seq returned no rows");
                    assertEquals(101L, rs.getLong("last_value"), "patient_seq value mismatch");
                }
            }
        }
    }
}
