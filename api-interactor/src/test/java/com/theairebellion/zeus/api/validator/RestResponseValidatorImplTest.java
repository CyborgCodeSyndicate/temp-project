package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestResponseValidatorImplTest {

    @Mock
    private Response responseMock;

    private RestResponseValidatorImpl validator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        validator = new RestResponseValidatorImpl();
    }

    @Test
    void testValidateResponse_STATUS() {
        @SuppressWarnings("unchecked")
        Assertion<Integer> statusAssertion = mock(Assertion.class);

        when(statusAssertion.getTarget()).thenReturn(RestAssertionTarget.STATUS);
        when(statusAssertion.getKey()).thenReturn("status"); // non-empty
        when(statusAssertion.getType()).thenReturn(AssertionTypes.IS); // non-null type

        when(responseMock.getStatusCode()).thenReturn(200);

        List<AssertionResult<Integer>> results = validator.validateResponse(responseMock, statusAssertion);
        assertNotNull(results);
    }

    @Test
    void testValidateResponse_BODY() {
        Assertion<String> bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn("some.json.path");
        when(bodyAssertion.getType()).thenReturn(AssertionTypes.IS);      // non-null type

        when(responseMock.jsonPath()).thenReturn(new JsonPath("{\"some\":{\"json\":{\"path\":\"val\"}}}"));

        @SuppressWarnings("unchecked")
        Assertion<String>[] assertionsArr = new Assertion[]{bodyAssertion};

        List<AssertionResult<String>> actual = validator.validateResponse(responseMock, assertionsArr);

        assertNotNull(actual);
    }

    @Test
    void testValidateResponse_BODY_NoKey() {
        Assertion<String> bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn(null);

        @SuppressWarnings("unchecked")
        Assertion<String>[] arr = new Assertion[]{bodyAssertion};

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_BODY_NullPathValue() {
        Assertion<String> bodyAssertion = mock(Assertion.class);
        when(bodyAssertion.getTarget()).thenReturn(RestAssertionTarget.BODY);
        when(bodyAssertion.getKey()).thenReturn("not.existing");
        when(responseMock.jsonPath()).thenReturn(new JsonPath("{}"));

        @SuppressWarnings("unchecked")
        Assertion<String>[] arr = new Assertion[]{bodyAssertion};

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_HEADER() {
        Assertion<String> headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn("X-Something");
        when(headerAssertion.getType()).thenReturn(AssertionTypes.IS);
        when(responseMock.getHeader("X-Something")).thenReturn("Value");

        @SuppressWarnings("unchecked")
        Assertion<String>[] arr = new Assertion[]{headerAssertion};

        List<AssertionResult<String>> actual = validator.validateResponse(responseMock, arr);
        assertNotNull(actual);
    }

    @Test
    void testValidateResponse_HEADER_NoKey() {
        Assertion<String> headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn(null);

        @SuppressWarnings("unchecked")
        Assertion<String>[] arr = new Assertion[]{headerAssertion};

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateResponse(responseMock, arr));
    }

    @Test
    void testValidateResponse_HEADER_MissingHeader() {
        Assertion<String> headerAssertion = mock(Assertion.class);
        when(headerAssertion.getTarget()).thenReturn(RestAssertionTarget.HEADER);
        when(headerAssertion.getKey()).thenReturn("X-NotThere");
        when(responseMock.getHeader("X-NotThere")).thenReturn(null);

        @SuppressWarnings("unchecked")
        Assertion<String>[] arr = new Assertion[]{headerAssertion};

        assertThrows(IllegalArgumentException.class,
                () -> validator.validateResponse(responseMock, arr));
    }
}