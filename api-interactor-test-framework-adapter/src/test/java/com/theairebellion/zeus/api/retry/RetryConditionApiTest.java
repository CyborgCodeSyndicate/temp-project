package com.theairebellion.zeus.api.retry;

import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.retry.RetryConditionApi;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RetryConditionApiTest {

    public static final String SOME_PATH = "some.path";
    public static final String NON_NULL_VALUE = "nonNullValue";
    public static final String EXPECTED_VALUE = "expectedValue";

    @Mock
    private RestService restService;

    @Mock
    private Endpoint endpoint;

    @Mock
    private Response response;

    @Mock
    private JsonPath jsonPath;

    @BeforeEach
    void setUp() {
        Mockito.framework().clearInlineMocks();
        restService = mock(RestService.class);
        endpoint = mock(Endpoint.class);
        response = mock(Response.class);
        jsonPath = mock(JsonPath.class);

        when(restService.request(endpoint)).thenReturn(response);
        when(response.getBody()).thenReturn(response);
        when(response.jsonPath()).thenReturn(jsonPath);
    }

    @Test
    void testStatusEquals() {
        when(response.getStatusCode()).thenReturn(200);
        RetryCondition<Integer> condition = RetryConditionApi.statusEquals(endpoint, 200);
        Integer result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }

    @Test
    void testStatusEqualsWithBody() {
        Object requestBody = new Object();
        when(restService.request(endpoint, requestBody)).thenReturn(response);
        when(response.getStatusCode()).thenReturn(201);
        RetryCondition<Integer> condition = RetryConditionApi.statusEquals(endpoint, requestBody, 201);
        Integer result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }

    @Test
    void testResponseFieldEqualsTo() {
        when(jsonPath.get(SOME_PATH)).thenReturn(EXPECTED_VALUE);
        RetryCondition<Object> condition = RetryConditionApi.responseFieldEqualsTo(endpoint, SOME_PATH, EXPECTED_VALUE);
        Object result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }

    @Test
    void testResponseFieldEqualsToWithBody() {
        Object requestBody = new Object();
        when(restService.request(endpoint, requestBody)).thenReturn(response);
        when(jsonPath.get(SOME_PATH)).thenReturn(EXPECTED_VALUE);
        RetryCondition<Object> condition = RetryConditionApi.responseFieldEqualsTo(endpoint, requestBody, SOME_PATH, EXPECTED_VALUE);
        Object result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }

    @Test
    void testResponseFieldNonNull() {
        when(jsonPath.get(SOME_PATH)).thenReturn(NON_NULL_VALUE);
        RetryCondition<Object> condition = RetryConditionApi.responseFieldNonNull(endpoint, SOME_PATH);
        Object result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }

    @Test
    void testResponseFieldNonNullWithBody() {
        Object requestBody = new Object();
        when(restService.request(endpoint, requestBody)).thenReturn(response);
        when(jsonPath.get(SOME_PATH)).thenReturn(NON_NULL_VALUE);
        RetryCondition<Object> condition = RetryConditionApi.responseFieldNonNull(endpoint, requestBody, SOME_PATH);
        Object result = condition.function().apply(restService);
        assertTrue(condition.condition().test(result));
    }
}