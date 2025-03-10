package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts three input arguments and produces a result.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiFunction},
 * but it supports three parameters instead of two. It is useful in scenarios
 * where a computation or transformation requires three inputs to generate an output.
 * </p>
 *
 * @param <T> The type of the first input argument.
 * @param <U> The type of the second input argument.
 * @param <V> The type of the third input argument.
 * @param <R> The type of the result produced by the function.
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * Applies this function to the given arguments and returns a result.
     *
     * @param t The first input argument.
     * @param u The second input argument.
     * @param v The third input argument.
     * @return The computed result of type {@code R}.
     */
    R apply(T t, U u, V v);
}
