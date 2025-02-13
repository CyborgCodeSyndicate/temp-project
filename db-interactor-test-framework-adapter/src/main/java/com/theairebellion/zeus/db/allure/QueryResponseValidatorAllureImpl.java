package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.json.JsonPathExtractor;
import com.theairebellion.zeus.db.validator.QueryResponseValidatorImpl;
import io.qameta.allure.Allure;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QueryResponseValidatorAllureImpl extends QueryResponseValidatorImpl {

    public QueryResponseValidatorAllureImpl(final JsonPathExtractor jsonPathExtractor) {
        super(jsonPathExtractor);
    }


    @Override
    protected void printAssertionTarget(final Map<String, Object> data) {
        super.printAssertionTarget(data);
        Allure.step("Validating query response with " + data.size() + " assertion(s)", () ->
                Allure.addAttachment("Data to be validated", data.toString()));
    }

}
