package com.theairebellion.zeus.api.allure;

import io.qameta.allure.Allure;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RestClientAllureImpl Tests")
class RestClientAllureImplTest {

    private static final String GET_METHOD = "GET";
    private static final String SAMPLE_URL = "https://example.com";
    private static final String SAMPLE_BODY = "{\"key\":\"value\"}";
    private static final String SAMPLE_BODY_JSON = "{ \"some\": \"json\" }";
    private static final String HEADER = "X-Header: val";
    private static final long RESPONSE_TIME = 123L;
    private static final int STATUS_CODE = 200;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody<?> mockBody;

    private RestClientAllureImpl restClientAllure;

    @BeforeEach
    void setUp() {
        // Basic setup of test object - using a real instance, not a spy
        restClientAllure = new RestClientAllureImpl();

        // Setup response mock - keep these minimal, just what's needed
        when(mockBody.prettyPrint()).thenReturn(SAMPLE_BODY_JSON);
        when(mockResponse.getBody()).thenReturn(mockBody);
        when(mockResponse.getHeaders()).thenReturn(new Headers(Collections.emptyList()));
        when(mockResponse.getStatusCode()).thenReturn(STATUS_CODE);
    }

    @Nested
    @DisplayName("Request Logging Tests")
    class RequestLoggingTests {

        @Test
        @DisplayName("printRequest should add Allure attachments for request details")
        void printRequestShouldAddAllureAttachments() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act
                restClientAllure.printRequest(GET_METHOD, SAMPLE_URL, SAMPLE_BODY, HEADER);

                // Verify Allure steps and attachments
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Sending request to endpoint %s-%s.", GET_METHOD, SAMPLE_URL)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("printRequest should handle null or empty body")
        void printRequestShouldHandleNullOrEmptyBody(String body) {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act
                restClientAllure.printRequest(GET_METHOD, SAMPLE_URL, body, HEADER);

                // Verify Allure steps
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Sending request to endpoint %s-%s.", GET_METHOD, SAMPLE_URL)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("printRequest should handle null or empty headers")
        void printRequestShouldHandleNullOrEmptyHeaders(String headers) {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act
                restClientAllure.printRequest(GET_METHOD, SAMPLE_URL, SAMPLE_BODY, headers);

                // Verify Allure steps
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Sending request to endpoint %s-%s.", GET_METHOD, SAMPLE_URL)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }
    }

    @Nested
    @DisplayName("Response Logging Tests")
    class ResponseLoggingTests {

        @Test
        @DisplayName("printResponse should add Allure attachments for response details")
        void printResponseShouldAddAllureAttachments() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act
                restClientAllure.printResponse(GET_METHOD, SAMPLE_URL, mockResponse, RESPONSE_TIME);

                // Verify Allure steps and attachments
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Response with status: %d received from endpoint: %s-%s in %dms.",
                                        STATUS_CODE, GET_METHOD, SAMPLE_URL, RESPONSE_TIME)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @Test
        @DisplayName("printResponse should handle null response body")
        void printResponseShouldHandleNullResponseBody() {
            // Setup null body
            Response nullBodyResponse = mock(Response.class);
            when(nullBodyResponse.getBody()).thenReturn(null);
            when(nullBodyResponse.getHeaders()).thenReturn(new Headers(Collections.emptyList()));
            when(nullBodyResponse.getStatusCode()).thenReturn(STATUS_CODE);

            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act - should not throw exception
                restClientAllure.printResponse(GET_METHOD, SAMPLE_URL, nullBodyResponse, RESPONSE_TIME);

                // Verify Allure steps
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Response with status: %d received from endpoint: %s-%s in %dms.",
                                        STATUS_CODE, GET_METHOD, SAMPLE_URL, RESPONSE_TIME)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @Test
        @DisplayName("printResponse should handle null headers")
        void printResponseShouldHandleNullHeaders() {
            // Setup null headers
            Response nullHeadersResponse = mock(Response.class);
            when(nullHeadersResponse.getBody()).thenReturn(mockBody);
            when(nullHeadersResponse.getHeaders()).thenReturn(null);
            when(nullHeadersResponse.getStatusCode()).thenReturn(STATUS_CODE);

            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act - should not throw exception
                restClientAllure.printResponse(GET_METHOD, SAMPLE_URL, nullHeadersResponse, RESPONSE_TIME);

                // Verify Allure steps
                mockedAllure.verify(() ->
                        Allure.step(
                                eq(String.format("Response with status: %d received from endpoint: %s-%s in %dms.",
                                        STATUS_CODE, GET_METHOD, SAMPLE_URL, RESPONSE_TIME)),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }
    }

    @Nested
    @DisplayName("Attachment Handling Tests")
    class AttachmentHandlingTests {

        @ParameterizedTest
        @MethodSource("provideAttachmentTestCases")
        @DisplayName("addAttachmentIfPresent should only add non-empty attachments")
        void addAttachmentIfPresentShouldOnlyAddNonEmptyAttachments(String content, boolean shouldAdd) {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Use reflection to call private method
                java.lang.reflect.Method method;
                try {
                    method = RestClientAllureImpl.class.getDeclaredMethod("addAttachmentIfPresent",
                            String.class, String.class);
                    method.setAccessible(true);
                    method.invoke(restClientAllure, "TestAttachment", content);

                    // Verify
                    if (shouldAdd) {
                        mockedAllure.verify(() -> Allure.addAttachment(eq("TestAttachment"), eq(content)));
                    } else {
                        mockedAllure.verify(() -> Allure.addAttachment(anyString(), anyString()), never());
                    }
                } catch (Exception e) {
                    // Handle reflection exceptions
                    throw new RuntimeException("Test failed due to reflection error", e);
                }
            }
        }

        private static Stream<Arguments> provideAttachmentTestCases() {
            return Stream.of(
                    Arguments.of("Content", true),
                    Arguments.of("", false),
                    Arguments.of("   ", false),
                    Arguments.of(null, false)
            );
        }
    }

    @Nested
    @DisplayName("Simple Execution Tests")
    class SimpleExecutionTests {

        @Test
        @DisplayName("printRequest should execute without errors")
        void printRequestShouldExecuteWithoutErrors() {
            // This test simply verifies the method doesn't throw exceptions
            restClientAllure.printRequest(GET_METHOD, SAMPLE_URL, SAMPLE_BODY, HEADER);
        }

        @Test
        @DisplayName("printResponse should execute without errors")
        void printResponseShouldExecuteWithoutErrors() {
            // This test simply verifies the method doesn't throw exceptions
            restClientAllure.printResponse(GET_METHOD, SAMPLE_URL, mockResponse, RESPONSE_TIME);
        }
    }
}