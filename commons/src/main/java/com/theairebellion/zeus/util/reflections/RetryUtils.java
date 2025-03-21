package com.theairebellion.zeus.util.reflections;

import com.theairebellion.zeus.logging.LogCommon;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Utility class providing retry mechanisms for executing operations with conditional success.
 * <p>
 * This class offers a method to repeatedly attempt an operation until a specified condition is met
 * or a maximum wait time is exceeded. It supports configurable retry intervals and logs attempts.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Retries an operation until a condition is satisfied or the timeout expires.</li>
 *     <li>Logs each attempt, including failures and exceptions.</li>
 *     <li>Supports configurable retry intervals to control execution pacing.</li>
 *     <li>Handles exceptions gracefully and provides detailed error messages upon failure.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate
 */
public class RetryUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RetryUtils() {
    }

    /**
     * Repeatedly executes a given operation until a condition is met or a timeout occurs.
     * <p>
     * This method invokes the supplied operation at fixed intervals, checking if the result satisfies
     * the provided condition. If the condition is met, the result is returned immediately.
     * If the condition is never satisfied within the allowed time, an exception is thrown.
     * </p>
     *
     * <p>Logging details:</p>
     * <ul>
     *     <li>Each attempt is logged with its attempt count.</li>
     *     <li>Failures and exceptions are logged at different severity levels.</li>
     *     <li>The final failure logs the total number of attempts before throwing an exception.</li>
     * </ul>
     *
     * @param maxWait       The maximum duration to keep retrying before giving up.
     * @param retryInterval The interval between successive retries.
     * @param supplier      The operation to execute, returning a result.
     * @param condition     The predicate that determines whether the retry should stop.
     * @param <T>           The type of result produced by the operation.
     * @return The result of the operation if the condition is met within the allowed time.
     * @throws IllegalStateException If the condition is not met within the maximum wait time.
     * @throws InterruptedException  If the thread is interrupted during the retry process.
     */
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

        // Safety check to prevent negative durations
        if (maxWait.isNegative()) {
            throw new IllegalArgumentException("maxWait must not be negative");
        }
        if (retryInterval.isNegative()) {
            throw new IllegalArgumentException("retryInterval must not be negative");
        }

        long maxWaitNanos = maxWait.toNanos();
        long intervalMillis = retryInterval.toMillis();
        long startTime = System.nanoTime();
        int attemptCount = 0;
        Exception lastException = null;

        // Continue retrying until we exceed the max wait time
        while (true) {
            // Check if we've exceeded the max wait time
            long currentTime = System.nanoTime();
            long elapsedNanos = currentTime - startTime;
            if (elapsedNanos >= maxWaitNanos) {
                break;
            }

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
                lastException = e;
                LogCommon.warn("Exception on attempt #{}: {}", attemptCount, e.getMessage());
                LogCommon.debug("Stack trace:", e);
            }

            // Calculate remaining time more accurately
            currentTime = System.nanoTime();
            long remainingNanos = maxWaitNanos - (currentTime - startTime);

            // If we don't have enough time for another interval, break
            if (remainingNanos <= 0) {
                break;
            }

            // Sleep for the interval or the remaining time, whichever is shorter
            long sleepTimeMillis = Math.min(intervalMillis, remainingNanos / 1_000_000);

            // Ensure we sleep at least 1ms if there's any time remaining
            sleepTimeMillis = Math.max(sleepTimeMillis, 1);

            try {
                Thread.sleep(sleepTimeMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Retry was interrupted", e);
            }
        }

        // Include the last exception in the thrown exception if one was caught
        IllegalStateException timeoutException = new IllegalStateException(String.format(
                "Failed to satisfy condition within %s (after %d attempts).", maxWait, attemptCount));

        if (lastException != null) {
            timeoutException.addSuppressed(lastException);
        }

        throw timeoutException;
    }

}
