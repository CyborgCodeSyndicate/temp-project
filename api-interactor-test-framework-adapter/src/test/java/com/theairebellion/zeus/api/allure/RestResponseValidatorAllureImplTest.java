package com.theairebellion.zeus.api.allure;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RestResponseValidatorAllureImpl Tests")
class RestResponseValidatorAllureImplTest {

    private static final String STATUS_CODE = "statusCode";
    private static final String HEADERS = "headers";
    private static final String BODY = "body";
    private static final int STATUS_200 = 200;

    @Mock
    private Response mockResponse;

    private RestResponseValidatorAllureImpl validator;

    @BeforeEach
    void setUp() {
        validator = new RestResponseValidatorAllureImpl();
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("printAssertionTarget should create Allure step with status code")
        void printAssertionTargetShouldCreateAllureStepWithStatusCode() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> testData = Map.of(STATUS_CODE, STATUS_200);

                // Capture the lambda so we can execute it
                ArgumentCaptor<Allure.ThrowableRunnableVoid> stepLambdaCaptor =
                        ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

                // Act
                validator.printAssertionTarget(testData);

                // Verify step was created with correct message
                mockedAllure.verify(() ->
                        Allure.step(eq("Validating response with 1 assertion(s)"), stepLambdaCaptor.capture())
                );

                // Execute the lambda to trigger its internal calls
                try {
                    stepLambdaCaptor.getValue().run();

                    // Now verify attachments were added
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Data to be validated"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Status Code"), anyString())
                    );
                } catch (Throwable e) {
                    throw new RuntimeException("Lambda execution failed", e);
                }
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle empty data map")
        void printAssertionTargetShouldHandleEmptyDataMap() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Capture the lambda so we can execute it
                ArgumentCaptor<Allure.ThrowableRunnableVoid> stepLambdaCaptor =
                        ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

                // Act
                validator.printAssertionTarget(Map.of());

                // Verify step was created with correct message
                mockedAllure.verify(() ->
                        Allure.step(eq("Validating response with 0 assertion(s)"), stepLambdaCaptor.capture())
                );

                // Execute the lambda to trigger its internal calls
                try {
                    stepLambdaCaptor.getValue().run();

                    // Now verify attachment was added
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Data to be validated"), anyString())
                    );

                    // Verify that no other attachments were added since map is empty
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Status Code"), anyString()), never()
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Headers"), anyString()), never()
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Response Body"), anyString()), never()
                    );
                } catch (Throwable e) {
                    throw new RuntimeException("Lambda execution failed", e);
                }
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle null values in data map")
        void printAssertionTargetShouldHandleNullValuesInDataMap() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> data = new HashMap<>();
                data.put("nullValue", null);

                // Capture the lambda so we can execute it
                ArgumentCaptor<Allure.ThrowableRunnableVoid> stepLambdaCaptor =
                        ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

                // Act
                validator.printAssertionTarget(data);

                // Verify step was created with correct message
                mockedAllure.verify(() ->
                        Allure.step(eq("Validating response with 1 assertion(s)"), stepLambdaCaptor.capture())
                );

                // Execute the lambda to trigger its internal calls
                try {
                    stepLambdaCaptor.getValue().run();

                    // Now verify attachment was added
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Data to be validated"), anyString())
                    );
                } catch (Throwable e) {
                    throw new RuntimeException("Lambda execution failed", e);
                }
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle headers and body data")
        void printAssertionTargetShouldHandleHeadersAndBodyData() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> data = new HashMap<>();
                data.put(HEADERS, Map.of("Content-Type", "application/json"));
                data.put(BODY, "{\"key\":\"value\"}");

                // Capture the lambda so we can execute it
                ArgumentCaptor<Allure.ThrowableRunnableVoid> stepLambdaCaptor =
                        ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

                // Act
                validator.printAssertionTarget(data);

                // Verify step was created with correct message
                mockedAllure.verify(() ->
                        Allure.step(eq("Validating response with 2 assertion(s)"), stepLambdaCaptor.capture())
                );

                // Execute the lambda to trigger its internal calls
                try {
                    stepLambdaCaptor.getValue().run();

                    // Now verify attachments were added
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Data to be validated"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Headers"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Response Body"), anyString())
                    );
                } catch (Throwable e) {
                    throw new RuntimeException("Lambda execution failed", e);
                }
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle all data types")
        void printAssertionTargetShouldHandleAllDataTypes() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> data = new HashMap<>();
                data.put(STATUS_CODE, STATUS_200);
                data.put(HEADERS, Map.of("Content-Type", "application/json"));
                data.put(BODY, "{\"key\":\"value\"}");

                // Capture the lambda so we can execute it
                ArgumentCaptor<Allure.ThrowableRunnableVoid> stepLambdaCaptor =
                        ArgumentCaptor.forClass(Allure.ThrowableRunnableVoid.class);

                // Act
                validator.printAssertionTarget(data);

                // Verify step was created with correct message
                mockedAllure.verify(() ->
                        Allure.step(eq("Validating response with 3 assertion(s)"), stepLambdaCaptor.capture())
                );

                // Execute the lambda to trigger its internal calls
                try {
                    stepLambdaCaptor.getValue().run();

                    // Now verify attachments were added
                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Data to be validated"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Status Code"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Headers"), anyString())
                    );

                    mockedAllure.verify(() ->
                            Allure.addAttachment(eq("Expected Response Body"), anyString())
                    );
                } catch (Throwable e) {
                    throw new RuntimeException("Lambda execution failed", e);
                }
            }
        }
    }

    @Nested
    @DisplayName("Simple Execution Tests")
    class SimpleExecutionTests {

        @Test
        @DisplayName("printAssertionTarget should execute without errors")
        void printAssertionTargetShouldExecuteWithoutErrors() {
            // Simply test execution without error
            validator.printAssertionTarget(Map.of(STATUS_CODE, STATUS_200));
        }

        @Test
        @DisplayName("printAssertionTarget should execute without errors with multiple assertions")
        void printAssertionTargetShouldExecuteWithMultipleAssertions() {
            // Create a test map with all possible keys
            Map<String, Object> testData = new HashMap<>();
            testData.put(STATUS_CODE, STATUS_200);
            testData.put(HEADERS, Map.of("Content-Type", "application/json"));
            testData.put(BODY, "{\"key\":\"value\"}");

            // Simply test execution without error
            validator.printAssertionTarget(testData);
        }
    }
}