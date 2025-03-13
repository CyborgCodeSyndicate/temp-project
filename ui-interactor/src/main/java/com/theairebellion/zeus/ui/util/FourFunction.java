package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts four input arguments and produces a result.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiFunction}, but it
 * supports four parameters instead of two. It is primarily used for exception handling
 * in WebElement interactions within Selenium-based test automation.
 * </p>
 *
 * <p>
 * The first argument, {@code T}, represents the primary entity involved in the operation.
 * The second argument, {@code U}, provides contextual information or additional parameters.
 * The third argument, {@code V}, captures supplementary data relevant to the action.
 * The fourth argument, {@code K}, is typically used for handling exceptions or other metadata.
 * The return value, {@code R}, represents the computed result of the function.
 * </p>
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
