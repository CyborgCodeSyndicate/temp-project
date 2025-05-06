package com.theairebellion.zeus.ui.util;

/**
 * Represents an operation that accepts four input arguments and performs an action.
 *
 * <p>This functional interface is similar to {@link java.util.function.BiConsumer}, but it
 * supports four parameters instead of two. It does not return a result.
 *
 * <p>The first argument, {@code T}, represents the primary entity involved in the operation.
 * The second argument, {@code U}, provides contextual information or additional parameters.
 * The third argument, {@code V}, captures supplementary data relevant to the action.
 * The fourth argument, {@code K}, is typically used for handling exceptions or other metadata.
 *
 * <p>This interface is primarily used for structured exception logging in Selenium WebDriver operations,
 * as seen in {@code ExceptionLogging}. It enables flexible logging of exceptions by capturing details
 * such as the affected object, action type, input parameters, and the exception itself.
 *
 * @author Cyborg Code Syndicate 💍👨💻
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
