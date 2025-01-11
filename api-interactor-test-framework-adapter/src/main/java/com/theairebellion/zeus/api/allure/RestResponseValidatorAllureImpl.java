package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.validator.RestResponseValidatorImpl;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestResponseValidatorAllureImpl extends RestResponseValidatorImpl {

    @Override
    @Step("Validating response with {assertions.length} assertion(s)")
    protected void printAssertionTarget(final Map<String, Object> data) {
        super.printAssertionTarget(data);
        Allure.step("Collected data to be validated", () -> {
            Allure.addAttachment("Data to be validated", data.toString());
        });
    }

}
