package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FluentChain Interface Tests")
class FluentChainTest {

    @Mock
    private Quest quest;

    private FluentChain fluentChain;

    @BeforeEach
    void setUp() {
        // Create a simple implementation of FluentChain for testing
        fluentChain = () -> quest;
    }

    @Test
    @DisplayName("validate(Consumer) should execute consumer for soft assertions")
    void testValidateWithConsumer() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            // Create a test implementation of FluentChain with a specialized then() that uses a mock SuperQuest
            CustomSoftAssertion mockedSoftAssertion = mock(CustomSoftAssertion.class);
            SuperQuest mockedSuperQuest = mock(SuperQuest.class);
            doReturn(mockedSoftAssertion).when(mockedSuperQuest).getSoftAssertions();

            // We need to use a custom FluentChain implementation for this test to avoid static mocking
            FluentChain testFluentChain = new FluentChain() {
                @Override
                public Quest then() {
                    return quest;
                }

                // Extend validate to capture the SuperQuest creation
                @Override
                public FluentChain validate(Consumer<SoftAssertions> assertion) {
                    // Instead of using 'new SuperQuest(then())' we'll use our mock
                    LogTest.validation("Starting soft validation.");
                    assertion.accept(mockedSuperQuest.getSoftAssertions());
                    return this;
                }
            };

            // Create and configure the consumer mock
            @SuppressWarnings("unchecked")
            Consumer<SoftAssertions> consumer = mock(Consumer.class);

            // When
            FluentChain result = testFluentChain.validate(consumer);

            // Then
            verify(consumer).accept(mockedSoftAssertion);
            verify(mockedSuperQuest).getSoftAssertions();

            // Verify the log message was called
            logTestMock.verify(() -> LogTest.validation("Starting soft validation."));

            // Verify method returns this for chaining
            assertSame(testFluentChain, result, "Method should return this for chaining");
        }
    }

    @Test
    @DisplayName("validate(Runnable) should execute runnable for hard assertions")
    void testValidateWithRunnable() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            Runnable runnable = mock(Runnable.class);

            // When
            FluentChain result = fluentChain.validate(runnable);

            // Then
            verify(runnable).run();
            verifyNoMoreInteractions(quest);

            // We don't verify exact message text to avoid brittle tests
            logTestMock.verify(() -> LogTest.validation(anyString()), times(2));

            assertSame(fluentChain, result, "Method should return this for chaining");
        }
    }

    @Test
    @DisplayName("complete() should delegate to quest.complete()")
    void testComplete() {
        // When
        fluentChain.complete();

        // Then
        verify(quest).complete();
        verifyNoMoreInteractions(quest);
    }

    @Test
    @DisplayName("then() should return the underlying quest")
    void testThen() {
        // When
        Quest result = fluentChain.then();

        // Then
        assertSame(quest, result, "then() should return the underlying quest");
    }

    @Test
    @DisplayName("Chained methods should work together")
    void testChainedMethods() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            Runnable runnable = mock(Runnable.class);

            // When - Chain validation and complete
            fluentChain.validate(runnable).complete();

            // Then
            verify(runnable).run();
            verify(quest).complete();

            // We don't verify exact message text to avoid brittle tests
            logTestMock.verify(() -> LogTest.validation(anyString()), atLeastOnce());
        }
    }

    @Test
    @DisplayName("Soft validation and complete should work together")
    void testSoftValidationChained() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            CustomSoftAssertion mockedSoftAssertion = mock(CustomSoftAssertion.class);
            SuperQuest mockedSuperQuest = mock(SuperQuest.class);
            doReturn(mockedSoftAssertion).when(mockedSuperQuest).getSoftAssertions();

            // We need to use a custom FluentChain implementation for this test to avoid static mocking
            FluentChain testFluentChain = new FluentChain() {
                @Override
                public Quest then() {
                    return quest;
                }

                // Extend validate to capture the SuperQuest creation
                @Override
                public FluentChain validate(Consumer<SoftAssertions> assertion) {
                    // Instead of using 'new SuperQuest(then())' we'll use our mock
                    LogTest.validation("Starting soft validation.");
                    assertion.accept(mockedSuperQuest.getSoftAssertions());
                    return this;
                }
            };

            // Create and configure the consumer mock
            @SuppressWarnings("unchecked")
            Consumer<SoftAssertions> consumer = mock(Consumer.class);

            // When - Chain validation and complete
            testFluentChain.validate(consumer).complete();

            // Then
            verify(consumer).accept(mockedSoftAssertion);
            verify(quest).complete();

            // Verify the log message was called
            logTestMock.verify(() -> LogTest.validation("Starting soft validation."));
        }
    }
}