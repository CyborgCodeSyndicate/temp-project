package com.theairebellion.zeus.api.allure;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class RestClientAllureImplTest {

    private RestClientAllureImpl restClientAllure;
    private Response mockResponse;
    private ResponseBody<?> mockBody;

    @BeforeEach
    void setUp() {
        restClientAllure = new RestClientAllureImpl();
        mockResponse = mock(Response.class);
        mockBody = mock(ResponseBody.class);

        when(mockBody.prettyPrint()).thenReturn("{ \"some\": \"json\" }");
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockResponse.getHeaders()).thenReturn(new Headers(Collections.emptyList()));
        when(mockResponse.getStatusCode()).thenReturn(200);
    }

    @Test
    void testPrintRequest_Normal() {
        restClientAllure.printRequest("GET", "https://example.com", "{\"key\":\"value\"}", "X-Header: val");
    }

    @Test
    void testPrintRequest_EmptyData() {
        restClientAllure.printRequest("GET", "", null, "   ");
    }

    @Test
    void testPrintResponse() {
        restClientAllure.printResponse("GET", "https://example.com", mockResponse, 123);
    }
}