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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    private final JsonPathExtractor jsonPathExtractor;
    public final DbClientManager dbClientManager;
    private final QueryResponseValidator queryResponseValidator;

    @Autowired
    public DatabaseService(JsonPathExtractor jsonPathExtractor,
                           DbClientManager dbClientManager,
                           QueryResponseValidator queryResponseValidator) {
        this.jsonPathExtractor = jsonPathExtractor;
        this.dbClientManager = dbClientManager;
        this.queryResponseValidator = queryResponseValidator;
    }

    public QueryResponse query(DbQuery query) {
        DatabaseConfiguration dbConfig = query.config();
        DbClient client = dbClientManager.getClient(dbConfig);

        return client.executeQuery(query.query());
    }

    public <T> T query(DbQuery query, String jsonPath, Class<T> resultType) {
        DatabaseConfiguration dbConfig = query.config();
        DbClient client = dbClientManager.getClient(dbConfig);

        String sql = query.query();
        QueryResponse queryResponse = client.executeQuery(sql);
        LogDb.step(
                "Extracting value from query result: '{}' by using JsonPath expression: '{}' and casting to class of type: '{}'.",
                sql, jsonPath, resultType.getSimpleName());
        return jsonPathExtractor.extract(queryResponse.getRows(), jsonPath, resultType);
    }

    public <T> List<AssertionResult<T>> validate(QueryResponse queryResponse, Assertion<?>... assertions) {
        return queryResponseValidator.validateQueryResponse(queryResponse, assertions);
    }

    public <T> List<AssertionResult<T>> queryAndValidate(DbQuery query, Assertion<?>... assertions) {
        DatabaseConfiguration dbConfig = query.config();
        DbClient client = dbClientManager.getClient(dbConfig);

        QueryResponse queryResponse = client.executeQuery(query.query());
        return queryResponseValidator.validateQueryResponse(queryResponse, assertions);
    }
}

