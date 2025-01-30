package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestResponseValidatorAllureImplTest {

    private RestResponseValidatorAllureImpl validator;

    @BeforeEach
    void setUp() {
        validator = new RestResponseValidatorAllureImpl();
    }

    @Test
    void testValidateResponse_AllurePrintAssertionTarget() {
        // We must provide a non-null target for the assertion
        Assertion<?> assertion = mock(Assertion.class);
        when(assertion.getTarget()).thenReturn(com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS);
        when(assertion.getKey()).thenReturn("status");
        when(assertion.getType()).thenReturn(AssertionTypes.IS);

        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(200);

        // We just call validateResponse() for coverage
        validator.validateResponse(response, assertion);
    }

    @Test
    void testPrintAssertionTarget_WithMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", 200);

        // We call the overridden method directly
        validator.printAssertionTarget(data);
    }
}