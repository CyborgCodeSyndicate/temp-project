package com.theairebellion.zeus.db.service;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.log.LogDb;
import com.theairebellion.zeus.db.query.DbQuery;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides services for executing and validating database queries.
 *
 * <p>This class facilitates database interactions by executing queries,
 * extracting specific data from query responses, and validating results
 * using predefined assertions.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Service
public class DatabaseService {

   private final JsonPathExtractor jsonPathExtractor;
   @Getter
   private final DbClientManager dbClientManager;
   private final QueryResponseValidator queryResponseValidator;

   /**
    * Constructs a {@code DatabaseService} with the required dependencies.
    *
    * @param jsonPathExtractor      Utility for extracting specific values from query results using JSONPath.
    * @param dbClientManager        Manages database client connections.
    * @param queryResponseValidator Validates query responses against assertions.
    */
   @Autowired
   public DatabaseService(JsonPathExtractor jsonPathExtractor,
                          DbClientManager dbClientManager,
                          QueryResponseValidator queryResponseValidator) {
      this.jsonPathExtractor = jsonPathExtractor;
      this.dbClientManager = dbClientManager;
      this.queryResponseValidator = queryResponseValidator;
   }

   /**
    * Executes a database query and retrieves the result.
    *
    * @param query The query to execute.
    * @return The query response containing the result set.
    */
   @SuppressWarnings("squid:S3740")
   public QueryResponse query(DbQuery query) {
      DatabaseConfiguration dbConfig = query.config();
      DbClient client = dbClientManager.getClient(dbConfig);
      return client.executeQuery(query.query());
   }

   /**
    * Executes a query and extracts a specific value using JSONPath.
    *
    * @param query      The query to execute.
    * @param jsonPath   The JSONPath expression to extract a value.
    * @param resultType The expected type of the extracted value.
    * @param <T>        The type parameter for the extracted value.
    * @return The extracted value.
    */
   @SuppressWarnings("squid:S3740")
   public <T> T query(DbQuery query, String jsonPath, Class<T> resultType) {
      DatabaseConfiguration dbConfig = query.config();
      DbClient client = dbClientManager.getClient(dbConfig);

      String sql = query.query();
      QueryResponse queryResponse = client.executeQuery(sql);
      LogDb.step(
            "Extracting value from query result: '{}' using JSONPath expression: '{}' and casting to '{}'.",
            sql, jsonPath, resultType.getSimpleName());

      return jsonPathExtractor.extract(queryResponse.getRows(), jsonPath, resultType);
   }

   /**
    * Validates a query response against a set of assertions.
    *
    * @param queryResponse The query response to validate.
    * @param assertions    The assertions to apply.
    * @param <T>           The expected result type.
    * @return A list of assertion results indicating pass or failure.
    */
   public <T> List<AssertionResult<T>> validate(QueryResponse queryResponse, Assertion... assertions) {
      return queryResponseValidator.validateQueryResponse(queryResponse, assertions);
   }

   /**
    * Executes a query and validates its response against assertions.
    *
    * @param query      The query to execute.
    * @param assertions The assertions to validate against.
    * @param <T>        The expected result type.
    * @return A list of assertion results indicating pass or failure.
    */
   @SuppressWarnings("squid:S3740")
   public <T> List<AssertionResult<T>> queryAndValidate(DbQuery query, Assertion... assertions) {
      DatabaseConfiguration dbConfig = query.config();
      DbClient client = dbClientManager.getClient(dbConfig);

      QueryResponse queryResponse = client.executeQuery(query.query());
      return queryResponseValidator.validateQueryResponse(queryResponse, assertions);
   }

}
