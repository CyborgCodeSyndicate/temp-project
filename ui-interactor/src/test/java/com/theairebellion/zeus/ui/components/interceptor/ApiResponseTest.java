package com.theairebellion.zeus.ui.components.interceptor;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiResponseTest {

   private static final String TEST_URL = "https://api.example.com/test";
   private static final int TEST_STATUS = 200;
   private static final String TEST_METHOD = "GET";
   private static final String TEST_BODY = "{\"key\": \"value\"}";

   @Test
   void constructor_ShouldInitializeUrlAndStatus() {
      // Act
      ApiResponse apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS);

      // Assert
      assertEquals(TEST_URL, apiResponse.getUrl());
      assertEquals(TEST_STATUS, apiResponse.getStatus());
      assertNull(apiResponse.getBody());
   }

   @ParameterizedTest
   @MethodSource("urlAndStatusProvider")
   void constructor_WithVariousInputs_ShouldInitializeCorrectly(String url, int status) {
      // Act
      ApiResponse apiResponse = new ApiResponse(url, TEST_METHOD, status);

      // Assert
      assertEquals(url, apiResponse.getUrl());
      assertEquals(status, apiResponse.getStatus());
   }

   @Test
   void setBody_ShouldUpdateBody() {
      // Arrange
      ApiResponse apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS);

      // Act
      apiResponse.setBody(TEST_BODY);

      // Assert
      assertEquals(TEST_BODY, apiResponse.getBody());
   }

   @Test
   void getters_ShouldReturnCorrectValues() {
      // Arrange
      ApiResponse apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS);
      apiResponse.setBody(TEST_BODY);

      // Act & Assert
      assertEquals(TEST_URL, apiResponse.getUrl());
      assertEquals(TEST_STATUS, apiResponse.getStatus());
      assertEquals(TEST_BODY, apiResponse.getBody());
   }

   @Test
   void setBody_WithNull_ShouldSetBodyToNull() {
      // Arrange
      ApiResponse apiResponse = new ApiResponse(TEST_URL, TEST_METHOD, TEST_STATUS);
      apiResponse.setBody(TEST_BODY);

      // Act
      apiResponse.setBody(null);

      // Assert
      assertNull(apiResponse.getBody());
   }

   private static Stream<Arguments> urlAndStatusProvider() {
      return Stream.of(
            Arguments.of("https://api.example.com/endpoint1", 200),
            Arguments.of("https://api.example.com/endpoint2", 201),
            Arguments.of("https://api.example.com/error", 400),
            Arguments.of("https://api.example.com/server-error", 500),
            Arguments.of("", 204),
            Arguments.of(null, 0)
      );
   }
}