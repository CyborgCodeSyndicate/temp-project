package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.validator.RestResponseValidatorImpl;
import io.qameta.allure.Allure;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestResponseValidatorAllureImpl extends RestResponseValidatorImpl {

    @Override
    protected void printAssertionTarget(final Map<String, Object> data) {
        Allure.step("Validating response with " + data.size() + " assertion(s)", () -> {
            super.printAssertionTarget(data);
            Allure.addAttachment("Data to be validated", data.toString());
            if (data.containsKey("statusCode")) {
                Allure.addAttachment("Expected Status Code", data.get("statusCode").toString());
            }
            if (data.containsKey("headers")) {
                Allure.addAttachment("Expected Headers", data.get("headers").toString());
            }
            if (data.containsKey("body")) {
                Allure.addAttachment("Expected Response Body", data.get("body").toString());
            }
        });
    }

}
