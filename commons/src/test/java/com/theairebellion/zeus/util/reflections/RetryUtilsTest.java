package com.theairebellion.zeus.util.reflections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class RetryUtilsTest {

    @Test
    void testImmediateSuccess() {
        String result = RetryUtils.retryUntil(
                Duration.ofSeconds(1),
                Duration.ofMillis(10),
                () -> "success",
                res -> res.equals("success")
        );
        assertEquals("success", result);
    }

    @Test
    void testEventualSuccess() {
        AtomicInteger counter = new AtomicInteger(0);
        String result = RetryUtils.retryUntil(
                Duration.ofSeconds(2),
                Duration.ofMillis(10),
                () -> (counter.incrementAndGet() >= 3) ? "done" : "not yet",
                res -> res.equals("done")
        );
        assertEquals("done", result);
        assertTrue(counter.get() >= 3);
    }

    @Test
    void testSupplierThrowsThenSuccess() {
        AtomicInteger counter = new AtomicInteger(0);
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
        assertEquals("ok", result);
        assertTrue(counter.get() >= 3);
    }

    @Test
    void testFailureToMeetCondition() {
        AtomicInteger counter = new AtomicInteger(0);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                RetryUtils.retryUntil(
                        Duration.ofMillis(100),
                        Duration.ofMillis(20),
                        () -> {
                            counter.incrementAndGet();
                            return "bad";
                        },
                        res -> res.equals("good")
                )
        );
        assertTrue(ex.getMessage().contains("Failed to satisfy condition"));
        assertTrue(counter.get() > 0);
    }

    @Test
    void testInterruptedSleep() {
        Supplier<String> supplier = () -> "not ok";
        // Interrupt current thread to simulate InterruptedException in Thread.sleep.
        Thread.currentThread().interrupt();
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                RetryUtils.retryUntil(
                        Duration.ofSeconds(1),
                        Duration.ofMillis(50),
                        supplier,
                        res -> false
                )
        );
        assertTrue(ex.getMessage().contains("Retry was interrupted"));
        // Clear the interrupted status for subsequent tests.
        Thread.interrupted();
    }

    @Test
    void testNullArguments() {
        Supplier<String> supplier = () -> "value";
        Predicate<String> condition = s -> s.equals("value");

        NullPointerException ex1 = assertThrows(NullPointerException.class, () ->
                RetryUtils.retryUntil(null, Duration.ofMillis(10), supplier, condition)
        );
        assertTrue(ex1.getMessage().contains("maxWait must not be null"));

        NullPointerException ex2 = assertThrows(NullPointerException.class, () ->
                RetryUtils.retryUntil(Duration.ofSeconds(1), null, supplier, condition)
        );
        assertTrue(ex2.getMessage().contains("retryInterval must not be null"));

        NullPointerException ex3 = assertThrows(NullPointerException.class, () ->
                RetryUtils.retryUntil(Duration.ofSeconds(1), Duration.ofMillis(10), null, condition)
        );
        assertTrue(ex3.getMessage().contains("supplier must not be null"));

        NullPointerException ex4 = assertThrows(NullPointerException.class, () ->
                RetryUtils.retryUntil(Duration.ofSeconds(1), Duration.ofMillis(10), supplier, null)
        );
        assertTrue(ex4.getMessage().contains("condition must not be null"));
    }
}