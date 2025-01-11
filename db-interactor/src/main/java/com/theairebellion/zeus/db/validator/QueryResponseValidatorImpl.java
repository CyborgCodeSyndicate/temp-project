package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.log.LogDB;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
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
        LogDB.info("Starting query response validation with {} assertion(s).", assertions.length);
        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            switch ((DbAssertionTarget) assertion.getTarget()) {
                case NUMBER_ROWS -> data.put("numRows", (T) Integer.valueOf(queryResponse.getRows().size()));
                case QUERY_RESULT -> data.put(assertion.getKey(),
                    (T) jsonPathExtractor.extract(queryResponse.getRows(), assertion.getKey(), Object.class));
                case COLUMNS -> data.put(assertion.getKey(),
                    (T) jsonPathExtractor.extract(queryResponse.getRows().get(0).keySet(), assertion.getKey(),
                        Object.class));
            }
        }
        printAssertionTarget((Map<String, Object>) data);

        return AssertionUtil.validate(data, assertions);
    }


    protected void printAssertionTarget(Map<String, Object> data) {
        LogDB.extended("Validation target: [{}]", data.toString());
    }

}
