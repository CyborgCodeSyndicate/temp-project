package com.theairebellion.zeus.db.h2;

import com.theairebellion.zeus.db.log.LogDb;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class H2DatabaseInitializer {

   public static final String DB_H2_FILES_SCHEMA_SQL = "db/h2/files/schema.sql";
   public static final String DB_H2_FILES_DATA_SQL = "db/h2/files/data.sql";

   public static final String EXECUTING_SQL = "Executing SQL: ";
   public static final String SCRIPT_NOT_FOUND = "Script not found: ";
   public static final String FAILED_TO_EXECUTE_STATEMENT = "Failed to execute statement: ";
   public static final String FAILED_TO_EXECUTE_SCRIPT = "Failed to execute script: ";

   public static void initialize() throws SQLException {
      InputStream schemaStream =
            H2DatabaseInitializer.class.getClassLoader().getResourceAsStream(DB_H2_FILES_SCHEMA_SQL);
      LogDb.debug(schemaStream != null ? "schema.sql found" : "schema.sql NOT found");

      InputStream dataStream = H2DatabaseInitializer.class.getClassLoader().getResourceAsStream(DB_H2_FILES_DATA_SQL);
      LogDb.debug(dataStream != null ? "data.sql found" : "data.sql NOT found");

      try (Connection connection = H2DatabaseConfig.getConnection()) {
         executeScript(connection, DB_H2_FILES_SCHEMA_SQL);
         executeScript(connection, DB_H2_FILES_DATA_SQL);
      }
   }

   public static void executeScript(Connection connection, String scriptPath) {
      try (InputStream inputStream = H2DatabaseInitializer.class.getClassLoader().getResourceAsStream(scriptPath)) {
         Objects.requireNonNull(inputStream, SCRIPT_NOT_FOUND + scriptPath);
         String sql = new BufferedReader(new InputStreamReader(inputStream))
               .lines().collect(Collectors.joining("\n"));

         Arrays.stream(sql.split(";"))
               .map(String::trim)
               .filter(statement -> !statement.isEmpty())
               .forEach(statement -> {
                  LogDb.info(EXECUTING_SQL + statement);
                  try (Statement stmt = connection.createStatement()) {
                     stmt.execute(statement);
                  } catch (SQLException e) {
                     throw new RuntimeException(FAILED_TO_EXECUTE_STATEMENT + statement, e);
                  }
               });
      } catch (Exception e) {
         throw new RuntimeException(FAILED_TO_EXECUTE_SCRIPT + scriptPath, e);
      }
   }
}