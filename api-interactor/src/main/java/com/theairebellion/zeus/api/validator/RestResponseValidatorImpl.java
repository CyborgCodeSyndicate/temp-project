package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.api.log.LogApi;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import io.restassured.response.Response;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RestResponseValidatorImpl implements RestResponseValidator {

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<AssertionResult<T>> validateResponse(final Response response, Assertion<?>... assertions) {
        LogApi.info("Starting response validation with {} assertion(s).", assertions.length);

        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            switch ((RestAssertionTarget) assertion.getTarget()) {
                case STATUS -> {
                    // todo: check if it can be improved
                    final String ASSERTION_KEY_FOR_STATUS = "AssertionKeyForStatus";
                    data.put(ASSERTION_KEY_FOR_STATUS, (T) Integer.valueOf(response.getStatusCode()));
                    assertion.setKey(ASSERTION_KEY_FOR_STATUS);
                }
                case BODY -> {
                    String key = assertion.getKey();
                    if (key == null) {
                        throw new IllegalArgumentException(
                            "Key is not specified in the assertion: " + assertion);
                    }

                    T value = response.jsonPath().get(key);
                    if (value == null) {
                        throw new IllegalArgumentException(
                            "Jsonpath expression: '" + key + "' not found in response body.");
                    }
                    data.put(key, value);
                }
                case HEADER -> {
                    String key = assertion.getKey();
                    if (key == null) {
                        throw new IllegalArgumentException(
                            "Key is not specified in the assertion: " + assertion);
                    }

                    String header = response.getHeader(key);
                    if (header == null) {
                        throw new IllegalArgumentException(
                            "Header '" + key + "' not found in response.");
                    }
                    data.put(key, (T) header);
                }
            }
        }

        printAssertionTarget((Map<String, Object>) data);
        return AssertionUtil.validate(data, assertions);
    }


    protected void printAssertionTarget(Map<String, Object> data) {
        LogApi.extended("Validation target: [{}]", data.toString());
    }

}
