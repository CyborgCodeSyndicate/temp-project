package com.theairebellion.zeus.ui.util;

/**
 * Represents an operation that accepts four input arguments and performs an action.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiConsumer}, but it
 * supports four parameters instead of two. It does not return a result.
 * </p>
 *
 * <p>
 * This interface is primarily used for structured exception logging in Selenium WebDriver operations,
 * as seen in {@code ExceptionLogging}. It enables flexible logging of exceptions by capturing details
 * such as the affected object, action type, input parameters, and the exception itself.
 * </p>
 *
 * @param <T> The type of the first input argument.
 * @param <U> The type of the second input argument.
 * @param <V> The type of the third input argument.
 * @param <K> The type of the fourth input argument.
 *
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface FourConsumer<T, U, V, K> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t The first input argument.
     * @param u The second input argument.
     * @param v The third input argument.
     * @param k The fourth input argument.
     */
    void accept(T t, U u, V v, K k);
}
