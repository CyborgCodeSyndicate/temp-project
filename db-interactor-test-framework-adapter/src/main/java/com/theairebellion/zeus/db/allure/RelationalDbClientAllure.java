package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.RelationalDbClient;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import com.theairebellion.zeus.db.query.QueryResponse;
import io.qameta.allure.Allure;

/**
 * Enhances {@code RelationalDbClient} with Allure reporting.
 * <p>
 * This class extends {@link RelationalDbClient} to integrate Allure logging,
 * ensuring SQL query execution details are captured in test reports.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class RelationalDbClientAllure extends RelationalDbClient {

    /**
     * Constructs a new {@code RelationalDbClientAllure} instance with the specified connector and database configuration.
     *
     * @param connector The database connection service.
     * @param dbConfig  The database configuration details.
     */
    public RelationalDbClientAllure(final BaseDbConnectorService connector, final DatabaseConfiguration dbConfig) {
        super(connector, dbConfig);
    }

    /**
     * Logs the executed SQL query using Allure reporting.
     * <p>
     * This method overrides the parent implementation to include Allure step-based logging,
     * ensuring that query execution details are captured in test reports.
     * </p>
     *
     * @param query The SQL query being executed.
     */
    @Override
    protected void printQuery(final String query) {
        super.printQuery(query);
        Allure.step(String.format("Executing SQL query: %s", query));
    }

    /**
     * Logs the query execution response details with Allure reporting.
     * <p>
     * This method overrides the parent implementation to attach the executed SQL query,
     * execution duration, and query results (if available) to Allure reports.
     * </p>
     *
     * @param query    The executed SQL query.
     * @param response The query response containing results.
     * @param duration The execution duration in milliseconds.
     */
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

    /**
     * Adds an attachment to Allure reports if the provided content is not empty.
     *
     * @param name    The attachment name.
     * @param content The content to be attached.
     */
    private void addAttachmentIfPresent(String name, String content) {
        if (content != null && !content.trim().isEmpty()) {
            Allure.addAttachment(name, content);
        }
    }

}
