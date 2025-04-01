package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.log.LogDb;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements validation logic for database query responses.
 * <p>
 * This class applies assertions on query results, validating row counts,
 * column values, and specific query outputs using JsonPath expressions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
public class QueryResponseValidatorImpl implements QueryResponseValidator {

    private final JsonPathExtractor jsonPathExtractor;

    /**
     * Constructs a new {@code QueryResponseValidatorImpl} with the necessary dependencies.
     *
     * @param jsonPathExtractor The JSON Path extractor for parsing query responses.
     */
    @Autowired
    public QueryResponseValidatorImpl(final JsonPathExtractor jsonPathExtractor) {
        this.jsonPathExtractor = jsonPathExtractor;
    }

    /**
     * Validates a query response against the given assertions.
     * <p>
     * This method supports validation of:
     * <ul>
     *     <li>Row count in the query result.</li>
     *     <li>Specific query result values using JsonPath.</li>
     *     <li>Presence of specific columns in the result set.</li>
     * </ul>
     * </p>
     *
     * @param queryResponse The query response containing the result set.
     * @param assertions    The assertions to validate against.
     * @param <T>           The expected type for validation results.
     * @return A list of assertion results indicating validation success or failure.
     * @throws InvalidAssertionException If an assertion is missing a key or has invalid parameters.
     * @throws IllegalArgumentException  If a required JsonPath expression or column does not exist.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<AssertionResult<T>> validateQueryResponse(final QueryResponse queryResponse,
                                                              final Assertion... assertions) {
        LogDb.info("Starting query response validation with {} assertion(s).", assertions.length);
        Map<String, T> data = new HashMap<>();

        for (Assertion assertion : assertions) {
            String key = assertion.getKey();
            switch ((DbAssertionTarget) assertion.getTarget()) {
                case NUMBER_ROWS -> data.put("numRows", (T) Integer.valueOf(queryResponse.getRows().size()));

                case QUERY_RESULT -> {
                    if (key == null) {
                        throw new InvalidAssertionException(
                                "Assertion value must have a non-null key. Key must contain a valid JsonPath expression.");
                    }
                    Object value = jsonPathExtractor.extract(queryResponse.getRows(), key, Object.class);
                    if (value == null) {
                        throw new IllegalArgumentException("JsonPath expression: '" + key + "' not found in query result.");
                    }
                    data.put(assertion.getKey(), (T) value);
                }

                case COLUMNS -> {
                    if (key == null) {
                        throw new InvalidAssertionException(
                                "Assertion value must have a non-null key. Key must contain a valid JsonPath expression.");
                    }
                    if (queryResponse.getRows().isEmpty()) {
                        throw new IllegalArgumentException("Query result is empty; cannot validate columns.");
                    }
                    Object value = jsonPathExtractor.extract(queryResponse.getRows().get(0).keySet(), key, Object.class);
                    if (value == null) {
                        throw new IllegalArgumentException("Column: '" + key + "' not found in query result.");
                    }
                    data.put(key, (T) value);
                }
            }
        }
        printAssertionTarget((Map<String, Object>) data);

        return AssertionUtil.validate(data, List.of(assertions));
    }

    /**
     * Logs the extracted data used for validation.
     *
     * @param data The extracted response data mapped to assertion keys.
     */
    protected void printAssertionTarget(Map<String, Object> data) {
        LogDb.extended("Validation target: [{}]", data.toString());
    }

}
