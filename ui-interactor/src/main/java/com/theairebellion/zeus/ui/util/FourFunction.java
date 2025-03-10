package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts four input arguments and produces a result.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiFunction}, but it
 * supports four parameters instead of two. It is primarily used for exception handling
 * in WebElement interactions within Selenium-based test automation.
 * </p>
 *
 * @param <T> The type of the first input argument.
 * @param <U> The type of the second input argument.
 * @param <V> The type of the third input argument.
 * @param <K> The type of the fourth input argument.
 * @param <R> The type of the result produced by this function.
 *
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface FourFunction<T, U, V, K, R> {

    /**
     * Applies this function to the given arguments and returns a result.
     *
     * @param t The first input argument.
     * @param u The second input argument.
     * @param k The third input argument.
     * @param v The fourth input argument.
     * @return The function result.
     */
    R apply(T t, U u, K k, V v);
}
