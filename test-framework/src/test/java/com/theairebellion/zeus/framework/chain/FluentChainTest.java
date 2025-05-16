package com.theairebellion.zeus.framework.chain;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FluentChain Interface Tests")
class FluentChainTest {

    @Mock
    private Quest quest;

    private TestableFluentChain fluentChain;

    /**
     * Testable implementation of FluentChain that allows mocking the SuperQuest creation
     */
    static class TestableFluentChain implements FluentChain {
        private final Quest quest;
        private SuperQuest superQuestToReturn;

        TestableFluentChain(Quest quest) {
            this.quest = quest;
        }

        @Override
        public Quest then() {
            return quest;
        }

        @Override
        public SuperQuest createSuperQuest(Quest quest) {
            return superQuestToReturn != null ? superQuestToReturn : FluentChain.super.createSuperQuest(quest);
        }

        // Allow tests to set the SuperQuest to return
        void setMockedSuperQuest(SuperQuest superQuest) {
            this.superQuestToReturn = superQuest;
        }
    }

    @BeforeEach
    void setUp() {
        fluentChain = new TestableFluentChain(quest);
    }

    @Nested
    @DisplayName("Validation methods tests")
    class ValidationTests {

        @Test
        @DisplayName("validate(Consumer) should execute consumer for soft assertions")
        void testValidateWithConsumer() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
                // Given
                CustomSoftAssertion mockedSoftAssertion = mock(CustomSoftAssertion.class);
                SuperQuest mockedSuperQuest = mock(SuperQuest.class);
                when(mockedSuperQuest.getSoftAssertions()).thenReturn(mockedSoftAssertion);

                // Set the mocked SuperQuest to be returned by our testable implementation
                fluentChain.setMockedSuperQuest(mockedSuperQuest);

                // Create and configure the consumer mock
                @SuppressWarnings("unchecked")
                Consumer<SoftAssertions> consumer = mock(Consumer.class);

                // When
                FluentChain result = fluentChain.validate(consumer);

                // Then
                verify(consumer).accept(mockedSoftAssertion);
                verify(mockedSuperQuest).getSoftAssertions();

                // Verify the log message was called
                logTestMock.verify(() -> LogTest.validation(anyString()));

                // Verify method returns this for chaining
                assertSame(fluentChain, result, "Method should return this for chaining");
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

                // Verify log messages
                logTestMock.verify(() -> LogTest.validation(anyString()), times(2));

                assertSame(fluentChain, result, "Method should return this for chaining");
            }
        }
    }

    @Nested
    @DisplayName("Basic interface method tests")
    class BasicMethodTests {
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
        @DisplayName("Factory method pattern works with validate(Consumer)")
        void testFactoryMethodWithValidate() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
                // Given
                CustomSoftAssertion mockedSoftAssertion = mock(CustomSoftAssertion.class);
                SuperQuest mockedSuperQuest = mock(SuperQuest.class);
                when(mockedSuperQuest.getSoftAssertions()).thenReturn(mockedSoftAssertion);

                // Set up our TestableFluentChain to return the mock
                fluentChain.setMockedSuperQuest(mockedSuperQuest);

                @SuppressWarnings("unchecked")
                Consumer<SoftAssertions> consumer = mock(Consumer.class);

                // When
                fluentChain.validate(consumer);

                // Then
                verify(consumer).accept(mockedSoftAssertion);

                // We've validated that we get the assertions from our mocked SuperQuest,
                // proving that createSuperQuest is called and works correctly
            }
        }
    }

    @Nested
    @DisplayName("Chained method tests")
    class ChainedMethodTests {
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

                // Verify log messages
                logTestMock.verify(() -> LogTest.validation(anyString()), times(2));
            }
        }

        @Test
        @DisplayName("Soft validation and complete should work together")
        void testSoftValidationChained() {
            try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
                // Given
                CustomSoftAssertion mockedSoftAssertion = mock(CustomSoftAssertion.class);
                SuperQuest mockedSuperQuest = mock(SuperQuest.class);
                when(mockedSuperQuest.getSoftAssertions()).thenReturn(mockedSoftAssertion);

                // Set the mocked SuperQuest to be returned by our testable implementation
                fluentChain.setMockedSuperQuest(mockedSuperQuest);

                // Create and configure the consumer mock
                @SuppressWarnings("unchecked")
                Consumer<SoftAssertions> consumer = mock(Consumer.class);

                // When - Chain validation and complete
                fluentChain.validate(consumer).complete();

                // Then
                verify(consumer).accept(mockedSoftAssertion);
                verify(quest).complete();

                // Verify the log message was called
                logTestMock.verify(() -> LogTest.validation(anyString()), times(1));
            }
        }
    }

    @Test
    @DisplayName("createSuperQuest should create a new SuperQuest with the given Quest")
    void testCreateSuperQuest() {
        // Given
        Quest mockQuest = mock(Quest.class);

        // When
        SuperQuest resultSuperQuest = fluentChain.createSuperQuest(mockQuest);

        // Then
        assertNotNull(resultSuperQuest, "SuperQuest should not be null");
    }

    @Test
    @DisplayName("validate(Runnable) should handle exceptions and log the failure")
    void testValidateWithRunnableThrowsException() {
        try (MockedStatic<LogTest> logTestMock = mockStatic(LogTest.class)) {
            // Given
            Runnable runnable = () -> { throw new RuntimeException("Test exception"); }; // Simulating an exception

            // When
            try {
                fluentChain.validate(runnable);
            } catch (RuntimeException e) {
                // Then
                // Verify that the exception was logged
                logTestMock.verify(() -> LogTest.validation("Hard validation failed: Test exception"));
                assertSame("Test exception", e.getMessage(), "The exception message should match.");
            }
        }
    }
}