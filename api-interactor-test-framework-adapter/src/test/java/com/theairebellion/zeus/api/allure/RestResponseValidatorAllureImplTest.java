package com.theairebellion.zeus.api.allure;

import com.theairebellion.zeus.api.validator.RestAssertionTarget;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class RestResponseValidatorAllureImplTest {

    private static final String STATUS = "status";
    private static final int STATUS_200 = 200;

    private RestResponseValidatorAllureImpl validator;

    @BeforeEach
    void setUp() {
        validator = new RestResponseValidatorAllureImpl();
    }

    @Test
    void testValidateResponse_AllurePrintAssertionTarget() {
        var assertion = mock(Assertion.class);
        when(assertion.getTarget()).thenReturn(RestAssertionTarget.STATUS);
        when(assertion.getKey()).thenReturn(STATUS);
        when(assertion.getType()).thenReturn(AssertionTypes.IS);

        var response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(STATUS_200);

        validator.validateResponse(response, assertion);
    }

    @Test
    void testPrintAssertionTarget_WithMap() {
        validator.printAssertionTarget(Map.of(STATUS, STATUS_200));
    }
}