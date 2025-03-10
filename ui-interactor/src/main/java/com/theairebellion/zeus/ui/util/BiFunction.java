package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts two input arguments and produces a result.
 * <p>
 * This functional interface is similar to {@link java.util.function.BiFunction}, allowing
 * the execution of a function that processes two inputs and returns a computed value.
 * </p>
 *
 * <p>
 * This interface is commonly used for defining behavior dynamically, such as handling
 * exceptions in Selenium WebDriver operations, as seen in {@code ExceptionHandlingWebDriver}.
 * </p>
 *
 * @param <T> The type of the first input argument.
 * @param <U> The type of the second input argument.
 * @param <V> The type of the result produced by the function.
 *
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface BiFunction<T, U, V> {

    /**
     * Applies this function to the given arguments and produces a result.
     *
     * @param t The first input argument.
     * @param u The second input argument.
     * @return The computed result of type {@code V}.
     */
    V apply(T t, U u);
}
