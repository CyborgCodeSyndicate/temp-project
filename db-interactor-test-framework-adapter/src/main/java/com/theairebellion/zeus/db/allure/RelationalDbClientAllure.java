package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.RelationalDbClient;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.query.QueryResponse;
import io.qameta.allure.Allure;

public class RelationalDbClientAllure extends RelationalDbClient {


    public RelationalDbClientAllure(final BaseDbConnectorService connector, final DatabaseConfiguration dbConfig) {
        super(connector, dbConfig);
    }


    @Override
    protected void printQuery(final String query) {
        super.printQuery(query);
        Allure.step(String.format("Executing SQL query: %s", query));
    }


    @Override
    protected void printResponse(final String query, final QueryResponse response, final long duration) {
        super.printResponse(query, response, duration);
        Allure.step(String.format("Finished executing query in %dms", duration), () -> {
            addAttachmentIfPresent("Executed SQL", query);
            addAttachmentIfPresent("Duration (ms)", String.valueOf(duration));

            if (!response.getRows().isEmpty()) {
                addAttachmentIfPresent("Result Rows", response.toString());
            }
        });
    }


    private void addAttachmentIfPresent(String name, String content) {
        if (content != null && !content.trim().isEmpty()) {
            Allure.addAttachment(name, content);
        }
    }

}
