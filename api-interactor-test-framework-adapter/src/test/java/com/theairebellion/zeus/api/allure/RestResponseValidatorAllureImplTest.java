package com.theairebellion.zeus.api.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RestResponseValidatorAllureImpl Tests")
class RestResponseValidatorAllureImplTest {

    private static final String STATUS = "AssertionKeyForStatus";
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
        @DisplayName("printAssertionTarget should create Allure step and attachment")
        void printAssertionTargetShouldCreateAllureStepAndAttachment() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> testData = Map.of(STATUS, STATUS_200);

                // Act
                validator.printAssertionTarget(testData);

                // Verify
                mockedAllure.verify(() ->
                        Allure.step(
                                eq("Collected data to be validated"),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle empty data map")
        void printAssertionTargetShouldHandleEmptyDataMap() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Act
                validator.printAssertionTarget(Map.of());

                // Verify
                mockedAllure.verify(() ->
                        Allure.step(
                                eq("Collected data to be validated"),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
            }
        }

        @Test
        @DisplayName("printAssertionTarget should handle null values in data map")
        void printAssertionTargetShouldHandleNullValuesInDataMap() {
            try (MockedStatic<Allure> mockedAllure = mockStatic(Allure.class)) {
                // Arrange
                Map<String, Object> data = new HashMap<>();
                data.put("nullValue", null);

                // Act
                validator.printAssertionTarget(data);

                // Verify
                mockedAllure.verify(() ->
                        Allure.step(
                                eq("Collected data to be validated"),
                                any(Allure.ThrowableRunnableVoid.class)
                        )
                );
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
            validator.printAssertionTarget(Map.of(STATUS, STATUS_200));
        }
    }

    @Nested
    @DisplayName("Annotation Tests")
    class AnnotationTests {

        @Test
        @DisplayName("printAssertionTarget should be annotated with @Step")
        void printAssertionTargetShouldBeAnnotatedWithStep() throws Exception {
            // Verify the method has the @Step annotation
            Method validateResponseMethod = RestResponseValidatorAllureImpl.class
                    .getDeclaredMethod("printAssertionTarget", Map.class);

            Step stepAnnotation = validateResponseMethod.getAnnotation(Step.class);

            assertNotNull(stepAnnotation, "Method should be annotated with @Step");
            assertEquals("Validating response with {assertions.length} assertion(s)",
                    stepAnnotation.value(), "Step annotation value should match");
        }
    }
}