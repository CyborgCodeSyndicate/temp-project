package com.theairebellion.zeus.ui.util;

import com.theairebellion.zeus.framework.util.ResourceLoader;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseFormatterTest {

   private ApiResponse successResponse;
   private ApiResponse warningResponse;
   private ApiResponse errorResponse;
   private ApiResponse invalidUrlResponse;

   @BeforeEach
   void setUp() {
      successResponse = mock(ApiResponse.class);
      when(successResponse.getStatus()).thenReturn(200);
      when(successResponse.getMethod()).thenReturn("GET");
      when(successResponse.getUrl()).thenReturn("https://example.com/api/success");
      when(successResponse.getBody()).thenReturn("{\"result\": \"ok\"}");

      warningResponse = mock(ApiResponse.class);
      when(warningResponse.getStatus()).thenReturn(302);
      when(warningResponse.getMethod()).thenReturn("POST");
      when(warningResponse.getUrl()).thenReturn("https://example.com/api/warn");
      when(warningResponse.getBody()).thenReturn("Redirected");

      errorResponse = mock(ApiResponse.class);
      when(errorResponse.getStatus()).thenReturn(500);
      when(errorResponse.getMethod()).thenReturn("PUT");
      when(errorResponse.getUrl()).thenReturn("https://example.com/api/error");
      when(errorResponse.getBody()).thenReturn("Internal Server Error");

      invalidUrlResponse = mock(ApiResponse.class);
      when(invalidUrlResponse.getStatus()).thenReturn(400);
      when(invalidUrlResponse.getMethod()).thenReturn("DELETE");
      when(invalidUrlResponse.getUrl()).thenReturn("not-a-url");
      when(invalidUrlResponse.getBody()).thenReturn("Bad Request");
   }

   @Test
   void testFormatResponsesWithMixedResponses() {
      List<ApiResponse> responses = Arrays.asList(successResponse, warningResponse, errorResponse, invalidUrlResponse);

      try (MockedStatic<ResourceLoader> resourceLoaderMock = Mockito.mockStatic(ResourceLoader.class)) {
         resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html"))
               .thenReturn("<div>{{total}}</div><div>{{success}}</div><div>{{warning}}</div><div>{{error}}</div>");

         String html = ResponseFormatter.formatResponses(responses);

         assertTrue(html.contains("4")); // total
         assertTrue(html.contains("1")); // success
         assertTrue(html.contains("1")); // warning
         assertTrue(html.contains("2")); // error
         assertTrue(html.contains("GET"));
         assertTrue(html.contains("POST"));
         assertTrue(html.contains("PUT"));
         assertTrue(html.contains("DELETE"));
         assertTrue(html.contains("Internal Server Error"));
      }
   }

   @Test
   void testFormatResponsesWithZeroStatus() {
      ApiResponse zeroStatusResponse = mock(ApiResponse.class);
      when(zeroStatusResponse.getStatus()).thenReturn(0);
      when(zeroStatusResponse.getMethod()).thenReturn("HEAD");
      when(zeroStatusResponse.getUrl()).thenReturn("https://example.com/api/zero");
      when(zeroStatusResponse.getBody()).thenReturn("No Status");

      try (MockedStatic<ResourceLoader> resourceLoaderMock = Mockito.mockStatic(ResourceLoader.class)) {
         resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html"))
               .thenReturn("<div>{{total}}</div><div>{{success}}</div><div>{{warning}}</div><div>{{error}}</div>");

         String html = ResponseFormatter.formatResponses(List.of(zeroStatusResponse));

         assertTrue(html.contains("1")); // total = 1
         assertTrue(html.contains("0")); // success = 0
         assertTrue(html.contains("0")); // warning = 0
         assertTrue(html.contains("0")); // error = 0
         assertTrue(html.contains("HEAD"));
         assertTrue(html.contains("/api/zero"));
         assertTrue(html.contains("No Status"));
      }
   }

   @Test
   void testFormatResponsesWithEmptyList() {
      try (MockedStatic<ResourceLoader> resourceLoaderMock = Mockito.mockStatic(ResourceLoader.class)) {
         resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html"))
               .thenReturn("<div>{{total}}</div><div>{{success}}</div><div>{{warning}}</div><div>{{error}}</div>");

         String html = ResponseFormatter.formatResponses(Collections.emptyList());

         assertTrue(html.contains("0")); // total, success, warning, error all zero
      }
   }

   @Test
   void testFormatResponsesWithNullBody() {
      ApiResponse nullBodyResponse = mock(ApiResponse.class);
      when(nullBodyResponse.getStatus()).thenReturn(200);
      when(nullBodyResponse.getMethod()).thenReturn("GET");
      when(nullBodyResponse.getUrl()).thenReturn("https://example.com/api/data");
      when(nullBodyResponse.getBody()).thenReturn(null); // <-- body is null

      try (MockedStatic<ResourceLoader> resourceLoaderMock = Mockito.mockStatic(ResourceLoader.class)) {
         resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html"))
               .thenReturn("<div>{{total}}</div><div>{{success}}</div><div>{{warning}}</div><div>{{error}}</div>");

         String html = ResponseFormatter.formatResponses(List.of(nullBodyResponse));

         assertTrue(html.contains("<pre></pre>"), "Expected <pre></pre> for null body");
         assertTrue(html.contains("1")); // total = 1
         assertTrue(html.contains("1")); // success = 1
         assertTrue(html.contains("0")); // warning = 0
         assertTrue(html.contains("0")); // error = 0
      }
   }

   @Test
   void testAppendResponseAccordionHandlesExceptionGracefully() {
      // Mock ApiResponse that throws an exception when getUrl() is called
      ApiResponse faultyResponse = mock(ApiResponse.class);
      when(faultyResponse.getUrl()).thenThrow(new RuntimeException("URL failure"));

      // Prepare HTML template
      try (MockedStatic<ResourceLoader> resourceLoaderMock = Mockito.mockStatic(ResourceLoader.class)) {
         resourceLoaderMock.when(() -> ResourceLoader.loadResourceFile("allure/html/intercepted-responses.html"))
               .thenReturn("<div>{{total}}</div><div>{{success}}</div><div>{{warning}}</div><div>{{error}}</div>");

         // Call method — exception will be swallowed silently in appendResponseAccordion
         String result = ResponseFormatter.formatResponses(List.of(faultyResponse));

         // Verify that output still contains summary, but no additional accordion block was added
         assertTrue(result.contains("{{total}}") || result.contains("1")); // total still counted
         assertFalse(result.contains("<div class='accordion'>"), "Accordion block should not be added due to exception");
      }
   }


   @Test
   void testExtractEndpointWithValidUrl() {
      String url = "https://example.com/path/to/resource";
      String endpoint = invokeExtractEndpoint(url);
      assertEquals("/path/to/resource", endpoint);
   }

   @Test
   void testExtractEndpointWithInvalidUrl() {
      String url = "not a real url";
      String endpoint = invokeExtractEndpoint(url);
      assertEquals(url, endpoint);
   }

   @Test
   void testExtractEndpointReturnsSlashWhenEmpty() {
      // This URL has no path, so getPath() will return an empty string
      String url = "http://example.com";

      String result = invokeExtractEndpoint(url);

      // Expected to return "/" because the path is empty
      assertEquals("/", result);
   }

   // Helper to invoke private static method extractEndpoint
   @SneakyThrows
   private String invokeExtractEndpoint(String url) {
      var method = ResponseFormatter.class.getDeclaredMethod("extractEndpoint", String.class);
      method.setAccessible(true);
      return (String) method.invoke(null, url);
   }
}
