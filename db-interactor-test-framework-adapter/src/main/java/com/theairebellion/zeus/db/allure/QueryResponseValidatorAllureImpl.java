package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.validator.QueryResponseValidatorImpl;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueryResponseValidatorAllureImpl extends QueryResponseValidatorImpl {

    public QueryResponseValidatorAllureImpl(final JsonPathExtractor jsonPathExtractor) {
        super(jsonPathExtractor);
    }


    @Override
    @Step("Validating response with {assertions.length} assertion(s)")
    protected void printAssertionTarget(final Map<String, Object> data) {
        super.printAssertionTarget(data);
        Allure.step("Collected data to be validated", () -> {
            Allure.addAttachment("Data to be validated", data.toString());
        });
    }

}
