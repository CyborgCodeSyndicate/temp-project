package com.example.project.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.service.DatabaseService;

public interface HooksDatabase {

    default DatabaseService dbService() {
        JsonPathExtractor jsonPathExtractor = new JsonPathExtractor(new ObjectMapper());
        return new DatabaseService(jsonPathExtractor,
                new DbClientManager(new BaseDbConnectorService()),
                new QueryResponseValidatorAllureImpl(jsonPathExtractor));
    }
}
