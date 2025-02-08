package com.theairebellion.zeus.api.allure;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class RestClientAllureImplTest {

    private static final String GET_METHOD = "GET";
    private static final String SAMPLE_URL = "https://example.com";
    private static final String SAMPLE_BODY = "{\"key\":\"value\"}";
    private static final String SAMPLE_BODY_JSON = "{ \"some\": \"json\" }";
    private static final String EMPTY = "";
    private static final String WHITESPACE = "   ";
    private static final String HEADER = "X-Header: val";

    private RestClientAllureImpl restClientAllure;
    private Response mockResponse;
    private ResponseBody<?> mockBody;

    @BeforeEach
    void setUp() {
        restClientAllure = new RestClientAllureImpl();
        mockResponse = mock(Response.class);
        mockBody = mock(ResponseBody.class);

        when(mockBody.prettyPrint()).thenReturn(SAMPLE_BODY_JSON);
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockResponse.getHeaders()).thenReturn(new Headers(Collections.emptyList()));
        when(mockResponse.getStatusCode()).thenReturn(200);
    }

    @Test
    void testPrintRequest_Normal() {
        restClientAllure.printRequest(GET_METHOD, SAMPLE_URL, SAMPLE_BODY, HEADER);
    }

    @Test
    void testPrintRequest_EmptyData() {
        restClientAllure.printRequest(GET_METHOD, EMPTY, null, WHITESPACE);
    }

    @Test
    void testPrintResponse() {
        restClientAllure.printResponse(GET_METHOD, SAMPLE_URL, mockResponse, 123);
    }
}
