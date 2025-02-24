package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestResponseValidatorImplTest {

    @Mock private Response responseMock;
    private RestResponseValidatorImpl validator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        validator = new RestResponseValidatorImpl();
    }

    @Test
    void testValidateResponse_STATUS() {
        var statusAssertion = mock(Assertion.class);
        when(statusAssertion.getTarget()).thenReturn(RestAssertionTarget.STATUS);
        when(statusAssertion.getKey()).thenReturn("AssertionKeyForStatus");
        when(statusAssertion.getType()).thenReturn(AssertionTypes.IS);
        when(responseMock.getStatusCode()).thenReturn(200);

        var assertionsArr = new Assertion[]{statusAssertion};

        assertNotNull(validator.validateResponse(responseMock, assertionsArr));
    }

    @Test
    void testValidateResponse_BODY() {
        var bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn("some.json.path");
        when(bodyAssertion.getType()).thenReturn(AssertionTypes.IS);
        when(responseMock.jsonPath()).thenReturn(new JsonPath("{\"some\":{\"json\":{\"path\":\"val\"}}}"));

        var assertionsArr = new Assertion[]{bodyAssertion};

        assertNotNull(validator.validateResponse(responseMock, assertionsArr));
    }

    @Test
    void testValidateResponse_BODY_NoKey() {
        var bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn(null);

        var arr = new Assertion[]{bodyAssertion};

        assertThrows(IllegalArgumentException.class, () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_BODY_NullPathValue() {
        var bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn("not.existing");
        when(responseMock.jsonPath()).thenReturn(new JsonPath("{}"));

        var arr = new Assertion[]{bodyAssertion};

        assertThrows(IllegalArgumentException.class, () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_HEADER() {
        var headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn("X-Something");
        when(headerAssertion.getType()).thenReturn(AssertionTypes.IS);
        when(responseMock.getHeader("X-Something")).thenReturn("Value");

        var arr = new Assertion[]{headerAssertion};

        assertNotNull(validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_HEADER_NoKey() {
        var headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn(null);

        var arr = new Assertion[]{headerAssertion};

        assertThrows(IllegalArgumentException.class, () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_HEADER_MissingHeader() {
        var headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn("X-NotThere");
        when(responseMock.getHeader("X-NotThere")).thenReturn(null);

        var arr = new Assertion[]{headerAssertion};

        assertThrows(IllegalArgumentException.class, () -> validator.validateResponse(responseMock, arr));
    }
}