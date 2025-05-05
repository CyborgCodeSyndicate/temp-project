package com.theairebellion.zeus.ui.components.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    private static final String TEST_URL = "https://api.example.com/test";
    private static final String TEST_METHOD = "GET"; // Added constant for method
    private static final int TEST_STATUS = 200;
    private static final String TEST_BODY = "{\"key\": \"value\"}";

    @ParameterizedTest
    @MethodSource("urlMethodAndStatusProvider") // Updated provider name
    @DisplayName("constructor should initialize url, method, and status correctly for various inputs")
    void constructor_WithVariousInputs_ShouldInitializeCorrectly(String url, String method, int status) { // Added method param
        // Given - url, method, status provided by MethodSource

        // When
        var apiResponse = new ApiResponse(url, method, status); // Use new constructor

        // Then
        assertEquals(url, apiResponse.getUrl());
        assertEquals(method, apiResponse.getMethod()); // Assert method
        assertEquals(status, apiResponse.getStatus());
        assertNull(apiResponse.getBody(), "Body should be null initially");
    }

    @Test
    @DisplayName("setBody and getters should work correctly")
    void setBody_And_Getters_ShouldWorkCorrectly() {
        // Given
        var apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS); // Use new constructor
        String initialBody = apiResponse.getBody();

        // When
        apiResponse.setBody(TEST_BODY);
        var url = apiResponse.getUrl();
        var method = apiResponse.getMethod(); // Get method
        var status = apiResponse.getStatus();
        var body = apiResponse.getBody();


        // Then
        assertNull(initialBody, "Body should be null initially");
        assertEquals(TEST_URL, url);
        assertEquals(TEST_METHOD, method); // Assert method
        assertEquals(TEST_STATUS, status);
        assertEquals(TEST_BODY, body);
    }

    @Test
    @DisplayName("setBody with null should update body to null")
    void setBody_WithNull_ShouldSetBodyToNull() {
        // Given
        var apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS); // Use new constructor
        apiResponse.setBody(TEST_BODY);

        // When
        apiResponse.setBody(null);

        // Then
        assertNull(apiResponse.getBody());
    }

    // Updated provider to include HTTP method
    private static Stream<Arguments> urlMethodAndStatusProvider() {
        return Stream.of(
                Arguments.of("https://api.example.com/endpoint1", "GET", 200),
                Arguments.of("https://api.example.com/endpoint2", "POST", 201),
                Arguments.of("https://api.example.com/error", "PUT", 400),
                Arguments.of("https://api.example.com/server-error", "DELETE", 500),
                Arguments.of("", "GET", 204), // Empty URL case
                Arguments.of(null, null, 0)  // Null URL/Method case
        );
    }
}