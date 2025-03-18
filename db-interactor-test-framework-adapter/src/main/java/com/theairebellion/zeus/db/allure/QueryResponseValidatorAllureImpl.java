package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.validator.QueryResponseValidatorImpl;
import io.qameta.allure.Allure;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Enhances {@code QueryResponseValidatorImpl} with Allure reporting.
 * <p>
 * This class extends {@link QueryResponseValidatorImpl} to integrate Allure
 * logging, ensuring query validation details are captured in test reports.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
public class QueryResponseValidatorAllureImpl extends QueryResponseValidatorImpl {

    /**
     * Constructs a new {@code QueryResponseValidatorAllureImpl} with the given JSON Path extractor.
     *
     * @param jsonPathExtractor The {@link JsonPathExtractor} used for extracting values from query responses.
     */
    public QueryResponseValidatorAllureImpl(final JsonPathExtractor jsonPathExtractor) {
        super(jsonPathExtractor);
    }

    /**
     * Logs the validation target using Allure reporting.
     * <p>
     * This method overrides the parent implementation to add Allure step-based logging,
     * ensuring that validation details are included in the test execution report.
     * </p>
     *
     * @param data The extracted response data mapped to assertion keys.
     */
    @Override
    protected void printAssertionTarget(final Map<String, Object> data) {
        super.printAssertionTarget(data);
        Allure.step("Validating query response with " + data.size() + " assertion(s)", () ->
                Allure.addAttachment("Data to be validated", data.toString()));
    }
}
