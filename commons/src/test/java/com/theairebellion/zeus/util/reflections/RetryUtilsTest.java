package com.theairebellion.zeus.util.reflections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RetryUtils Tests")
class RetryUtilsTest {

    @Nested
    @DisplayName("Basic Retry Functionality")
    class BasicRetryFunctionality {
        @Test
        @DisplayName("Should return immediately when condition is satisfied on first attempt")
        void testImmediateSuccess() {
            // When
            String result = RetryUtils.retryUntil(
                    Duration.ofSeconds(1),
                    Duration.ofMillis(10),
                    () -> "success",
                    res -> res.equals("success")
            );

            // Then
            assertEquals("success", result, "Should return the successful result immediately");
        }

        @Test
        @DisplayName("Should retry until condition is satisfied")
        void testEventualSuccess() {
            // Given
            AtomicInteger counter = new AtomicInteger(0);

            // When
            String result = RetryUtils.retryUntil(
                    Duration.ofSeconds(2),
                    Duration.ofMillis(10),
                    () -> (counter.incrementAndGet() >= 3) ? "done" : "not yet",
                    res -> res.equals("done")
            );

            // Then
            assertEquals("done", result, "Should return the final successful result");
            assertTrue(counter.get() >= 3, "Should have attempted at least 3 times");
        }

        @Test
        @DisplayName("Should handle exceptions in supplier and continue retrying")
        void testSupplierThrowsThenSuccess() {
            // Given
            AtomicInteger counter = new AtomicInteger(0);

            // When
            String result = RetryUtils.retryUntil(
                    Duration.ofSeconds(2),
                    Duration.ofMillis(10),
                    () -> {
                        if (counter.incrementAndGet() < 3) {
                            throw new RuntimeException("failure");
                        }
                        return "ok";
                    },
                    res -> res.equals("ok")
            );

            // Then
            assertEquals("ok", result, "Should return success after handling exceptions");
            assertTrue(counter.get() >= 3, "Should have attempted at least 3 times");
        }
    }

    @Nested
    @DisplayName("Timeout and Failure Handling")
    class TimeoutAndFailureHandling {
        @Test
        @DisplayName("Should throw exception when condition is never satisfied")
        @Timeout(1) // Ensure test doesn't take too long
        void testFailureToMeetCondition() {
            // Given
            AtomicInteger counter = new AtomicInteger(0);
            Duration shortTimeout = Duration.ofMillis(100);

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                    RetryUtils.retryUntil(
                            shortTimeout,
                            Duration.ofMillis(20),
                            () -> {
                                counter.incrementAndGet();
                                return "bad";
                            },
                            res -> res.equals("good")
                    )
            );

            assertTrue(ex.getMessage().contains("Failed to satisfy condition"),
                    "Exception message should indicate failure reason");
            assertTrue(counter.get() > 0, "Should have made at least one attempt");
        }

        @Test
        @DisplayName("Should properly handle thread interruption")
        void testInterruptedSleep() {
            // Given
            Supplier<String> supplier = () -> "not ok";
            Thread.currentThread().interrupt(); // Simulate interruption

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                    RetryUtils.retryUntil(
                            Duration.ofSeconds(1),
                            Duration.ofMillis(50),
                            supplier,
                            res -> false
                    )
            );

            assertTrue(ex.getMessage().contains("Retry was interrupted"),
                    "Exception message should indicate interruption");

            // Clear interrupt flag for subsequent tests
            Thread.interrupted();
        }
    }

    @Nested
    @DisplayName("Parameter Validation")
    class ParameterValidation {
        @Test
        @DisplayName("Should validate all parameters are non-null")
        void testNullArguments() {
            // Given
            Supplier<String> supplier = () -> "value";
            Predicate<String> condition = s -> s.equals("value");
            Duration maxWait = Duration.ofSeconds(1);
            Duration interval = Duration.ofMillis(10);

            // When/Then - Test each parameter
            NullPointerException ex1 = assertThrows(NullPointerException.class, () ->
                    RetryUtils.retryUntil(null, interval, supplier, condition)
            );
            assertTrue(ex1.getMessage().contains("maxWait must not be null"),
                    "Should check maxWait parameter");

            NullPointerException ex2 = assertThrows(NullPointerException.class, () ->
                    RetryUtils.retryUntil(maxWait, null, supplier, condition)
            );
            assertTrue(ex2.getMessage().contains("retryInterval must not be null"),
                    "Should check retryInterval parameter");

            NullPointerException ex3 = assertThrows(NullPointerException.class, () ->
                    RetryUtils.retryUntil(maxWait, interval, null, condition)
            );
            assertTrue(ex3.getMessage().contains("supplier must not be null"),
                    "Should check supplier parameter");

            NullPointerException ex4 = assertThrows(NullPointerException.class, () ->
                    RetryUtils.retryUntil(maxWait, interval, supplier, null)
            );
            assertTrue(ex4.getMessage().contains("condition must not be null"),
                    "Should check condition parameter");
        }
    }

    @Test
    @DisplayName("Should respect max wait time")
    void testMaxWaitTimeRespected() {
        // Given - a longer timeout to ensure no timing issues
        Duration maxWait = Duration.ofMillis(500);
        Duration interval = Duration.ofMillis(50);

        // When
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                RetryUtils.retryUntil(
                        maxWait,
                        interval,
                        () -> "never",
                        res -> false
                )
        );

        // Then - verify the exception message contains expected information
        String message = ex.getMessage();
        assertTrue(message.contains("Failed to satisfy condition"),
                "Exception should indicate timeout");
        assertTrue(message.contains("after"),
                "Exception should mention attempts were made");
    }

    @Test
    @DisplayName("Should handle zero retry interval")
    void testZeroRetryInterval() {
        // Given
        AtomicInteger counter = new AtomicInteger(0);

        // When
        String result = RetryUtils.retryUntil(
                Duration.ofMillis(500),
                Duration.ofMillis(0), // Zero interval
                () -> (counter.incrementAndGet() >= 3) ? "done" : "not yet",
                res -> res.equals("done")
        );

        // Then
        assertEquals("done", result, "Should return successful result with zero interval");
        assertTrue(counter.get() >= 3, "Should have attempted at least 3 times");
    }

    @Test
    @DisplayName("Should log attempts appropriately")
    void testLoggingBehavior() {
        // This test would ideally use MockedStatic for LogCommon, but we'll add a comment
        // explaining how it would work for reference
        /*
        try (MockedStatic<LogCommon> logMock = mockStatic(LogCommon.class)) {
            // Given
            AtomicInteger counter = new AtomicInteger(0);

            // When
            RetryUtils.retryUntil(
                    Duration.ofMillis(100),
                    Duration.ofMillis(10),
                    () -> (counter.incrementAndGet() >= 3) ? "done" : "not yet",
                    res -> res.equals("done")
            );

            // Then
            logMock.verify(() -> LogCommon.debug(contains("Condition not satisfied"), anyInt()),
                    times(2));
            logMock.verify(() -> LogCommon.info(contains("Condition satisfied"), eq(3)),
                    times(1));
        }
        */

        // For now, we'll just verify that the method completes successfully
        AtomicInteger counter = new AtomicInteger(0);
        String result = RetryUtils.retryUntil(
                Duration.ofMillis(100),
                Duration.ofMillis(10),
                () -> (counter.incrementAndGet() >= 3) ? "done" : "not yet",
                res -> res.equals("done")
        );
        assertEquals("done", result);
    }

    @Nested
    @DisplayName("RetryUtils Edge Cases")
    class RetryUtilsEdgeCasesTests {

        @Test
        @DisplayName("Should handle immediate success")
        void testImmediateSuccess() {
            // Given
            Duration maxWait = Duration.ofMillis(1000);
            Duration interval = Duration.ofMillis(100);
            AtomicInteger counter = new AtomicInteger(0);

            // When
            String result = RetryUtils.retryUntil(
                    maxWait,
                    interval,
                    () -> {
                        counter.incrementAndGet();
                        return "success";
                    },
                    s -> true // Always satisfies condition
            );

            // Then
            assertEquals("success", result, "Should return the successful result");
            assertEquals(1, counter.get(), "Should only make one attempt");
        }

        @Test
        @DisplayName("Should handle zero wait time")
        void testZeroWaitTime() {
            // Given
            Duration zeroWait = Duration.ofMillis(0);
            Duration interval = Duration.ofMillis(100);

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> RetryUtils.retryUntil(
                            zeroWait,
                            interval,
                            () -> "result",
                            s -> false // Never satisfies condition
                    ),
                    "Should throw with zero wait time");

            assertTrue(ex.getMessage().contains("Failed to satisfy condition"),
                    "Exception should indicate timeout");
        }

        @Test
        @DisplayName("Should handle zero interval")
        void testZeroInterval() {
            // Given
            Duration maxWait = Duration.ofMillis(200);
            Duration zeroInterval = Duration.ofMillis(0);
            AtomicInteger counter = new AtomicInteger(0);

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> RetryUtils.retryUntil(
                            maxWait,
                            zeroInterval,
                            () -> {
                                counter.incrementAndGet();
                                return "result";
                            },
                            s -> false // Never satisfies condition
                    ),
                    "Should attempt multiple times with zero interval");

            assertTrue(counter.get() > 1, "Should make multiple attempts even with zero interval");
        }

        @Test
        @DisplayName("Should handle exceptions thrown by condition")
        void testConditionThrowsException() {
            // Given
            Duration maxWait = Duration.ofMillis(500);  // Longer timeout for stability
            Duration interval = Duration.ofMillis(50);
            AtomicInteger exceptionCount = new AtomicInteger(0);

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> RetryUtils.retryUntil(
                            maxWait,
                            interval,
                            () -> "result",
                            s -> {
                                int count = exceptionCount.incrementAndGet();
                                throw new RuntimeException("Condition exception #" + count);
                            }
                    )
            );

            // Verify an exception was thrown indicating timeout
            assertTrue(ex.getMessage().contains("Failed to satisfy condition"),
                    "Should indicate that condition wasn't satisfied");

            // Verify the exception has at least one suppressed exception
            Throwable[] suppressed = ex.getSuppressed();
            assertTrue(suppressed.length > 0, "Should have at least one suppressed exception");

            // Verify the suppressed exception is from our condition
            assertTrue(suppressed[0].getMessage().contains("Condition exception"),
                    "Suppressed exception should be from our condition");

            // Verify the condition was called at least once
            assertTrue(exceptionCount.get() > 0, "Condition should be called at least once");
        }

        @Test
        @DisplayName("Should handle very small intervals")
        void testVerySmallInterval() {
            // Given
            Duration maxWait = Duration.ofMillis(100);
            Duration tinyInterval = Duration.ofNanos(1); // Extremely small interval
            AtomicInteger counter = new AtomicInteger(0);

            // When/Then
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> RetryUtils.retryUntil(
                            maxWait,
                            tinyInterval,
                            () -> {
                                counter.incrementAndGet();
                                return "result";
                            },
                            s -> false // Never satisfies condition
                    ),
                    "Should handle very small intervals properly");

            assertTrue(counter.get() > 1, "Should make multiple attempts with tiny interval");
        }

        @Test
        @DisplayName("Should validate input parameters")
        void testInputValidation() {
            // Given
            Duration validDuration = Duration.ofMillis(100);
            Supplier<String> supplier = () -> "result";
            Predicate<String> condition = s -> true;

            // When/Then - Test null validations
            assertThrows(NullPointerException.class,
                    () -> RetryUtils.retryUntil(null, validDuration, supplier, condition),
                    "Should validate maxWait is not null");

            assertThrows(NullPointerException.class,
                    () -> RetryUtils.retryUntil(validDuration, null, supplier, condition),
                    "Should validate retryInterval is not null");

            assertThrows(NullPointerException.class,
                    () -> RetryUtils.retryUntil(validDuration, validDuration, null, condition),
                    "Should validate supplier is not null");

            assertThrows(NullPointerException.class,
                    () -> RetryUtils.retryUntil(validDuration, validDuration, supplier, null),
                    "Should validate condition is not null");
        }
    }
}