package com.theairebellion.zeus.db.h2.tests;

import com.theairebellion.zeus.db.h2.H2DatabaseConfig;
import com.theairebellion.zeus.db.h2.H2DatabaseInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDatabaseTest {

    public static final String QUERY = "SELECT COUNT(*) AS total FROM users";

    @BeforeAll
    static void setupDatabase() throws Exception {
        H2DatabaseInitializer.initialize();
    }

    @Test
    void testUserTable() throws Exception {
        try (Connection connection = com.theairebellion.zeus.db.h2.H2DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(QUERY)) {

            if (resultSet.next()) {
                int total = resultSet.getInt("total");
                assertEquals(2, total);
            }
        }
    }

    @AfterAll
    static void tearDownDatabase() throws SQLException {
        H2DatabaseConfig.closeConnection();
    }
}