package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.springframework.stereotype.Component;

@Component
public class AllureDbClientManager extends DbClientManager {

    public AllureDbClientManager(final BaseDbConnectorService connector) {
        super(connector);
    }


    @Override
    protected DbClient initializeDbClient(final DatabaseConfiguration dbConfig) {
        return new RelationalDbClientAllure(getConnector(), dbConfig);
    }

}
