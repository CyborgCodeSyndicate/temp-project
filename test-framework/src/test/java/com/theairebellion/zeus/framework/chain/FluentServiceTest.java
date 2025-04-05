package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.util.reflections.RetryUtils;
import com.theairebellion.zeus.validator.core.AssertionResult;
import io.qameta.allure.Allure;
import org.assertj.core.api.BooleanAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FluentService Tests")
class FluentServiceTest {

    @Spy
    private FluentService service;

    @Mock
    private SuperQuest superQuest;

    @Mock
    private Quest originalQuest;

    @Mock
    private CustomSoftAssertion customSoftAssertions;

    @BeforeEach
    void setUp() {
        service.setQuest(superQuest);
    }

    @Test
    @DisplayName("then() should return original quest and log message")
    void testThen() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            when(superQuest.getOriginal()).thenReturn(originalQuest);

            // When
            Quest result = service.then();

            // Then
            verify(superQuest).getOriginal();
            assertSame(originalQuest, result, "Should return original quest");
            logTestMock.verify(() -> LogTest.info(anyString()));
        }
    }

    @Nested
    @DisplayName("RetryUntil Tests")
    @ExtendWith(MockitoExtension.class)
    class RetryUntilTests {

        @Mock
        RetryCondition<Object> retryCondition;

        @Mock
        Function<Object, Object> function;

        @Mock
        Predicate<Object> predicate;

        @Test
        @DisplayName("retryUntil should use RetryUtils and return self")
        void testRetryUntil() {
            try (MockedStatic<RetryUtils> retryUtilsMock = mockStatic(RetryUtils.class)) {
                // Given

                when(retryCondition.function()).thenReturn(function);

                when(retryCondition.condition()).thenReturn(predicate);

                Object testService = new Object();
                Duration maxWait = Duration.ofMillis(10);
                Duration retryInterval = Duration.ofMillis(5);
                Object functionResult = new Object();

                // Mock the lambda capture
                when(function.apply(testService)).thenReturn(functionResult);

                // Set up the RetryUtils to call the supplier
                retryUtilsMock.when(() -> RetryUtils.retryUntil(any(), any(), any(), any()))
                        .thenAnswer(invocation -> {
                            Supplier<?> supplier = invocation.getArgument(2);
                            Object result = supplier.get();
                            assertSame(functionResult, result);
                            return null;
                        });

                // When
                FluentService result = service.retryUntil(
                        retryCondition,
                        maxWait,
                        retryInterval,
                        testService
                );

                // Then
                retryUtilsMock.verify(() ->
                                RetryUtils.retryUntil(
                                        eq(maxWait),
                                        eq(retryInterval),
                                        any(),
                                        eq(predicate)
                                ),
                        times(1)
                );
                verify(function).apply(testService);
                verify(retryCondition).function();
                verify(retryCondition).condition();
                assertSame(service, result, "Should return this for chaining");
            }
        }
    }

    @Test
    @DisplayName("setQuest should set the quest field")
    void testSetQuest() {
        // Given
        FluentService newService = new FluentService();
        SuperQuest newQuest = mock(SuperQuest.class);

        // When
        newService.setQuest(newQuest);

        // Then
        assertSame(newQuest, newService.quest, "Quest field should be set to provided quest");
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {
        @Test
        @DisplayName("validation should handle empty results list")
        void testValidationWithEmptyList() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
                // When
                service.validation(Collections.emptyList());

                // Then - no logs or steps should be created, and no exception thrown
                logTestMock.verifyNoInteractions();
                allureMock.verifyNoInteractions();
                verifyNoInteractions(superQuest);
            }
        }

        @Test
        @DisplayName("validation should process passing soft assertions correctly")
        void testValidationWithPassingSoftAssertions() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
                // Given
                AssertionResult<Object> softAssertion = new AssertionResult<>(true, "Test Description", "expected", "actual", true);

                // Create a fresh mock for this test to avoid interference
                BooleanAssert testBooleanAssert = mock(BooleanAssert.class);
                when(testBooleanAssert.as(anyString())).thenReturn(testBooleanAssert);

                when(superQuest.getSoftAssertions()).thenReturn(customSoftAssertions);
                when(customSoftAssertions.assertThat(true)).thenReturn(testBooleanAssert);

                // When
                service.validation(List.of(softAssertion));

                // Then
                logTestMock.verify(() -> LogTest.validation(softAssertion.toString()));
                allureMock.verify(() -> Allure.step(softAssertion.toString()));
                verify(superQuest).getSoftAssertions();
                verify(customSoftAssertions).assertThat(true);
                verify(testBooleanAssert).as(softAssertion.toString());
                verify(testBooleanAssert).isTrue();
            }
        }

        @Test
        @DisplayName("validation should process failing soft assertions correctly")
        void testValidationWithFailingSoftAssertions() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class)) {
                // Given
                AssertionResult<Object> softAssertion = new AssertionResult<>(false, "Test Description", "expected", "actual", true);

                // Create a fresh mock for this test to avoid interference
                BooleanAssert testBooleanAssert = mock(BooleanAssert.class);
                when(testBooleanAssert.as(anyString())).thenReturn(testBooleanAssert);

                when(superQuest.getSoftAssertions()).thenReturn(customSoftAssertions);
                when(customSoftAssertions.assertThat(false)).thenReturn(testBooleanAssert);

                // When
                service.validation(List.of(softAssertion));

                // Then
                logTestMock.verify(() -> LogTest.validation(softAssertion.toString()));
                allureMock.verify(() -> Allure.step(softAssertion.toString()));
                verify(superQuest).getSoftAssertions();
                verify(customSoftAssertions).assertThat(false);
                verify(testBooleanAssert).as(softAssertion.toString());
                verify(testBooleanAssert).isTrue();
            }
        }

        @Test
        @DisplayName("validation should process passing hard assertions correctly")
        void testValidationWithPassingHardAssertions() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class);
                 MockedStatic<org.assertj.core.api.Assertions> assertionsMock =
                         mockStatic(org.assertj.core.api.Assertions.class)) {

                // Given
                AssertionResult<Object> hardAssertion = new AssertionResult<>(true, "Test Description", "expected", "actual", false);

                // Mock the assertThat static method with a properly chained mock
                BooleanAssert localBooleanAssert = mock(BooleanAssert.class);
                when(localBooleanAssert.as(anyString())).thenReturn(localBooleanAssert);

                assertionsMock.when(() -> org.assertj.core.api.Assertions.assertThat(true))
                        .thenReturn(localBooleanAssert);

                // When
                service.validation(List.of(hardAssertion));

                // Then
                logTestMock.verify(() -> LogTest.validation(hardAssertion.toString()));
                allureMock.verify(() -> Allure.step(hardAssertion.toString()));
                assertionsMock.verify(() -> org.assertj.core.api.Assertions.assertThat(true));
                verify(localBooleanAssert).as(hardAssertion.toString());
                verify(localBooleanAssert).isTrue();
            }
        }

        @Test
        @DisplayName("validation should process failing hard assertions correctly")
        void testValidationWithFailingHardAssertions() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class);
                 MockedStatic<org.assertj.core.api.Assertions> assertionsMock =
                         mockStatic(org.assertj.core.api.Assertions.class)) {

                // Given
                AssertionResult<Object> hardAssertion = new AssertionResult<>(false, "Test Description", "expected", "actual", false);

                // Mock the assertThat static method with a properly chained mock
                BooleanAssert localBooleanAssert = mock(BooleanAssert.class);
                when(localBooleanAssert.as(anyString())).thenReturn(localBooleanAssert);

                assertionsMock.when(() -> org.assertj.core.api.Assertions.assertThat(false))
                        .thenReturn(localBooleanAssert);

                // When
                service.validation(List.of(hardAssertion));

                // Then
                logTestMock.verify(() -> LogTest.validation(hardAssertion.toString()));
                allureMock.verify(() -> Allure.step(hardAssertion.toString()));
                assertionsMock.verify(() -> org.assertj.core.api.Assertions.assertThat(false));
                verify(localBooleanAssert).as(hardAssertion.toString());
                verify(localBooleanAssert).isTrue();
            }
        }

        @Test
        @DisplayName("validation should handle multiple assertion results")
        void testValidationWithMultipleResults() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class);
                 MockedStatic<Allure> allureMock = mockStatic(Allure.class);
                 MockedStatic<org.assertj.core.api.Assertions> assertionsMock =
                         mockStatic(org.assertj.core.api.Assertions.class)) {

                // Given
                AssertionResult<Object> softAssertion = new AssertionResult<>(true, "Soft Test", "expected", "actual", true);
                AssertionResult<Object> hardAssertion = new AssertionResult<>(true, "Hard Test", "expected", "actual", false);

                when(superQuest.getSoftAssertions()).thenReturn(customSoftAssertions);

                // Create separate mockings for both assertion types
                BooleanAssert softBooleanAssert = mock(BooleanAssert.class);
                when(softBooleanAssert.as(anyString())).thenReturn(softBooleanAssert);
                when(customSoftAssertions.assertThat(true)).thenReturn(softBooleanAssert);

                BooleanAssert hardBooleanAssert = mock(BooleanAssert.class);
                when(hardBooleanAssert.as(anyString())).thenReturn(hardBooleanAssert);
                assertionsMock.when(() -> org.assertj.core.api.Assertions.assertThat(true))
                        .thenReturn(hardBooleanAssert);

                // When
                service.validation(Arrays.asList(softAssertion, hardAssertion));

                // Then
                logTestMock.verify(() -> LogTest.validation(softAssertion.toString()));
                logTestMock.verify(() -> LogTest.validation(hardAssertion.toString()));
                allureMock.verify(() -> Allure.step(softAssertion.toString()));
                allureMock.verify(() -> Allure.step(hardAssertion.toString()));
                verify(superQuest).getSoftAssertions();
                verify(customSoftAssertions).assertThat(true);
                assertionsMock.verify(() -> org.assertj.core.api.Assertions.assertThat(true));
                verify(softBooleanAssert).as(softAssertion.toString());
                verify(softBooleanAssert).isTrue();
                verify(hardBooleanAssert).as(hardAssertion.toString());
                verify(hardBooleanAssert).isTrue();
            }
        }
    }

    @Test
    @DisplayName("postQuestSetupInitialization should be overridable")
    void testPostQuestSetupInitialization() {
        // This is an empty method in the base class, intended to be overridden
        // So we just need to check it can be called without throwing exceptions

        // When
        service.postQuestSetupInitialization();

        // Then - verify that the method was called on our spy
        verify(service).postQuestSetupInitialization();
    }

    @Test
    @DisplayName("FluentChain.complete should be implemented")
    void testComplete() {
        // Given
        when(superQuest.getOriginal()).thenReturn(originalQuest);

        // When
        service.complete();

        // Then
        verify(originalQuest).complete();
    }
}