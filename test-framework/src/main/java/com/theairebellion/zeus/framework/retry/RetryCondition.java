package com.theairebellion.zeus.framework.retry;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a condition to be checked repeatedly until it succeeds or times out.
 * <p>
 * Implementations of this interface define two key elements:
 * <ul>
 *   <li>A {@link #function()} that produces a value to be evaluated.</li>
 *   <li>A {@link #condition()} that tests the produced value for success.</li>
 * </ul>
 * This pattern allows for flexible retry logic, where an operation can be invoked repeatedly
 * until the returned value satisfies the specified condition.
 * </p>
 *
 * <p>
 * This interface is often used in conjunction with a retry mechanism that repeatedly calls
 * the {@code function()} until the {@code condition()} is met or a timeout is reached.
 * </p>
 *
 * <p>
 * Author: Cyborg Code Syndicate
 * </p>
 */
public interface RetryCondition<T> {

    /**
     * Produces a value to be evaluated by the retry logic.
     * <p>
     * This function is typically called repeatedly by a retry mechanism until
     * the result satisfies the corresponding {@link #condition()}.
     * </p>
     *
     * @return a {@link Function} that takes an {@code Object} (e.g., a service or context)
     * and returns a value of type {@code T} for evaluation.
     */
    Function<Object, T> function();

    /**
     * Evaluates the value produced by {@link #function()} to determine success.
     * <p>
     * If this predicate returns {@code true}, the retry mechanism can stop. Otherwise,
     * the retry mechanism continues to invoke {@code function()} until a timeout is reached.
     * </p>
     *
     * @return a {@link Predicate} that tests the value of type {@code T} for success.
     */
    Predicate<T> condition();

}
