package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.logging.LogCommon;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RetryUtils {

    private RetryUtils() {
    }


    public static <T> T retryUntil(
        Duration maxWait,
        Duration retryInterval,
        Supplier<T> supplier,
        Predicate<T> condition
    ) {
        Objects.requireNonNull(maxWait, "maxWait must not be null");
        Objects.requireNonNull(retryInterval, "retryInterval must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");
        Objects.requireNonNull(condition, "condition must not be null");

        long maxWaitNanos = maxWait.toNanos();
        long intervalMillis = retryInterval.toMillis();
        long startTime = System.nanoTime();
        int attemptCount = 0;

        while ((System.nanoTime() - startTime) < maxWaitNanos) {
            attemptCount++;
            try {
                T result = supplier.get();
                if (condition.test(result)) {
                    LogCommon.info("Condition satisfied on attempt #{}, returning result.", attemptCount);
                    return result;
                } else {
                    LogCommon.debug("Condition not satisfied on attempt #{}. Retrying...", attemptCount);
                }
            } catch (Exception e) {
                LogCommon.warn("Exception on attempt #{}: {}", attemptCount, e.getMessage());
                LogCommon.debug("Stack trace:", e);
            }

            if ((System.nanoTime() - startTime + retryInterval.toNanos()) >= maxWaitNanos) {
                break;
            }

            if (intervalMillis > 0) {
                try {
                    Thread.sleep(intervalMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Retry was interrupted", e);
                }
            }
        }

        throw new IllegalStateException(String.format(
            "Failed to satisfy condition within %s (after %d attempts).", maxWait, attemptCount));
    }


}
