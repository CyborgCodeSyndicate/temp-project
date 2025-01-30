package com.theairebellion.zeus.api.allure;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestClientAllureImplTest {

    private RestClientAllureImpl restClientAllure;
    private Response mockResponse;
    private ResponseBody<?> mockBody;

    @BeforeEach
    void setUp() {
        restClientAllure = new RestClientAllureImpl();

        // 1) mock the Response
        mockResponse = mock(Response.class);

        // 2) mock the Body, returning something non-null from .prettyPrint()
        mockBody = mock(ResponseBody.class);
        when(mockBody.prettyPrint()).thenReturn("{ \"some\": \"json\" }");

        // 3) ensure the main calls won't return null
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockResponse.getHeaders())
                .thenReturn(new Headers(Collections.emptyList())); // an empty list is safe
        when(mockResponse.getStatusCode()).thenReturn(200);
    }

    @Test
    void testPrintRequest_Normal() {
        // Just call the method for coverage.
        // The internal Allure.step(...) call won't run the attachments in real-time
        restClientAllure.printRequest("GET", "https://example.com", "{\"key\":\"value\"}", "X-Header: val");
    }

    @Test
    void testPrintRequest_EmptyData() {
        // Check coverage for null or empty strings
        restClientAllure.printRequest("GET", "", null, "   ");
    }

    @Test
    void testPrintResponse() {
        restClientAllure.printResponse("GET", "https://example.com", mockResponse, 123);
    }
}