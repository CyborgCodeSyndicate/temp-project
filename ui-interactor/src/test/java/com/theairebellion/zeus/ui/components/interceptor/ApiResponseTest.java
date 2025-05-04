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
    private static final int TEST_STATUS = 200;
    private static final String TEST_BODY = "{\"key\": \"value\"}";

    @ParameterizedTest
    @MethodSource("urlAndStatusProvider")
    @DisplayName("constructor should initialize url and status correctly for various inputs")
    void constructor_WithVariousInputs_ShouldInitializeCorrectly(String url, int status) {
        // Given - url and status provided by MethodSource

        // When
        var apiResponse = new ApiResponse(url, status);

        // Then
        assertEquals(url, apiResponse.getUrl());
        assertEquals(status, apiResponse.getStatus());
        assertNull(apiResponse.getBody(), "Body should be null initially");
    }

    @Test
    @DisplayName("setBody and getters should work correctly")
    void setBody_And_Getters_ShouldWorkCorrectly() {
        // Given
        var apiResponse = new ApiResponse(TEST_URL, TEST_STATUS);
        String initialBody = apiResponse.getBody();

        // When
        apiResponse.setBody(TEST_BODY);
        var url = apiResponse.getUrl();
        var status = apiResponse.getStatus();
        var body = apiResponse.getBody();


        // Then
        assertNull(initialBody, "Body should be null initially");
        assertEquals(TEST_URL, url);
        assertEquals(TEST_STATUS, status);
        assertEquals(TEST_BODY, body);
    }

    @Test
    @DisplayName("setBody with null should update body to null")
    void setBody_WithNull_ShouldSetBodyToNull() {
        // Given
        var apiResponse = new ApiResponse(TEST_URL, TEST_STATUS);
        apiResponse.setBody(TEST_BODY); // Set to non-null first

        // When
        apiResponse.setBody(null);

        // Then
        assertNull(apiResponse.getBody());
    }

    private static Stream<Arguments> urlAndStatusProvider() {
        return Stream.of(
                Arguments.of("https://api.example.com/endpoint1", 200),
                Arguments.of("https://api.example.com/endpoint2", 201),
                Arguments.of("https://api.example.com/error", 400),
                Arguments.of("https://api.example.com/server-error", 500),
                Arguments.of("", 204), // Empty URL case
                Arguments.of(null, 0)  // Null URL case
        );
    }
}