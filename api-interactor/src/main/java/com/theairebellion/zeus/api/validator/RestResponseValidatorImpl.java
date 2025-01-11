package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.api.log.LogAPI;
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
    public <T> List<AssertionResult<T>> validateResponse(final Response response, final Assertion<?>... assertions) {
        LogAPI.info("Starting response validation with {} assertion(s).", assertions.length);

        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            switch ((RestAssertionTarget) assertion.getTarget()) {
                case STATUS -> data.put("status", (T) Integer.valueOf(response.getStatusCode()));
                case BODY -> data.put(assertion.getKey(), response.jsonPath().get(assertion.getKey()));
                case HEADER -> data.put(assertion.getKey(), (T) response.getHeader(assertion.getKey()));
            }
        }

        printAssertionTarget((Map<String, Object>) data);
        return AssertionUtil.validate(data, assertions);
    }


    protected void printAssertionTarget(Map<String, Object> data) {
        LogAPI.extended("Validation target: [{}]", data.toString());
    }

}
