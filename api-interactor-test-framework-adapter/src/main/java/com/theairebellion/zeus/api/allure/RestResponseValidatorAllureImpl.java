package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.validator.RestResponseValidatorImpl;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Enhances {@link RestResponseValidatorImpl} with Allure reporting.
 * <p>
 * This class logs assertion validation details to Allure, providing
 * insights into the response validation process.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
public class RestResponseValidatorAllureImpl extends RestResponseValidatorImpl {

    /**
     * Logs validation targets to Allure reports.
     * <p>
     * Overrides {@link RestResponseValidatorImpl#printAssertionTarget(Map)} to attach
     * extracted validation data as an Allure step.
     * </p>
     *
     * @param data The extracted response data mapped to assertion keys.
     */
    @Override
    @Step("Validating response with {assertions.length} assertion(s)")
    protected void printAssertionTarget(final Map<String, Object> data) {
        super.printAssertionTarget(data);
        Allure.step("Collected data to be validated", () -> {
            Allure.addAttachment("Data to be validated", data.toString());
        });
    }

}
