package com.theairebellion.zeus.api.allure;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RestClientAllureImplTest {

    private RestClientAllureImpl restClientAllure;

    @BeforeEach
    void setUp() {
        restClientAllure = new RestClientAllureImpl();
    }

    @Test
    void testPrintRequest() {
        // We'll mock static Allure calls
        try (MockedStatic<io.qameta.allure.Allure> mockedAllure = Mockito.mockStatic(io.qameta.allure.Allure.class)) {
            restClientAllure.printRequest("GET", "https://example.com", "{\"key\":\"value\"}", "header1: val");

            // We can verify that addAttachment is invoked
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("HTTP Method"), eq("GET")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("URL"), eq("https://example.com")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Headers"), eq("header1: val")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Request Body"), eq("{\"key\":\"value\"}")), times(1));
        }
    }

    @Test
    void testPrintRequest_EmptyValues() {
        // If some are null or empty, no attachments should be added
        try (MockedStatic<io.qameta.allure.Allure> mockedAllure = Mockito.mockStatic(io.qameta.allure.Allure.class)) {
            restClientAllure.printRequest("GET", "", null, "   ");

            // The only non-empty string is "GET"
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("HTTP Method"), eq("GET")), times(1));

            // Others are empty => no attachments
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("URL"), anyString()), never());
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Request Body"), anyString()), never());
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Headers"), anyString()), never());
        }
    }

    @Test
    void testPrintResponse() {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(200);
        when(response.getHeaders()).thenReturn(null);
        when(response.getBody()).thenReturn(null);

        try (MockedStatic<io.qameta.allure.Allure> mockedAllure = Mockito.mockStatic(io.qameta.allure.Allure.class)) {
            restClientAllure.printResponse("GET", "https://example.com", response, 123);

            // We expect attachments for method, url, response time, status code
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("HTTP Method"), eq("GET")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("URL"), eq("https://example.com")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Response Time (ms)"), eq("123")), times(1));
            mockedAllure.verify(() -> io.qameta.allure.Allure.addAttachment(eq("Status Code"), eq("200")), times(1));
        }
    }
}