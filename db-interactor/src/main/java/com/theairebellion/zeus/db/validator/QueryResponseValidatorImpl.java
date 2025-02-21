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

@Component
public class QueryResponseValidatorImpl implements QueryResponseValidator {

    private final JsonPathExtractor jsonPathExtractor;


    @Autowired
    public QueryResponseValidatorImpl(final JsonPathExtractor jsonPathExtractor) {
        this.jsonPathExtractor = jsonPathExtractor;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> List<AssertionResult<T>> validateQueryResponse(final QueryResponse queryResponse,
                                                              final Assertion<?>... assertions) {
        LogDb.info("Starting query response validation with {} assertion(s).", assertions.length);
        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            String key = assertion.getKey();
            switch ((DbAssertionTarget) assertion.getTarget()) {
                case NUMBER_ROWS -> data.put("numRows", (T) Integer.valueOf(queryResponse.getRows().size()));
                case QUERY_RESULT -> {
                    if (key == null) {
                        throw new InvalidAssertionException(
                                "Assertion value must have a non-null key. Key must contain a valid Jsonpath expression.");
                    }
                    Object value = jsonPathExtractor.extract(queryResponse.getRows(), assertion.getKey(), Object.class);
                    if (value == null) {
                        throw new IllegalArgumentException("Jsonpath expression: '" + key + "' not found in query result.");
                    }
                    data.put(assertion.getKey(), (T) value);
                }
                case COLUMNS -> {
                    if (key == null) {
                        throw new InvalidAssertionException(
                                "Assertion value must have a non-null key. Key must contain a valid Jsonpath expression.");
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

        return AssertionUtil.validate(data, assertions);
    }


    protected void printAssertionTarget(Map<String, Object> data) {
        LogDb.extended("Validation target: [{}]", data.toString());
    }

}
