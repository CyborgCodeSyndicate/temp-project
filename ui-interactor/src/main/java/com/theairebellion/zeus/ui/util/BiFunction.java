package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts two input arguments and produces a result.
 *
 * <p>This functional interface is similar to {@link java.util.function.BiFunction}, allowing
 * the execution of a function that processes two inputs and returns a computed value.
 *
 * <p>The first input argument, {@code T}, represents the primary entity being processed.
 * The second input argument, {@code U}, provides additional data or context necessary for computation.
 * The function returns a result of type {@code V}, which represents the computed output.
 *
 * <p>This interface is commonly used for defining behavior dynamically, such as handling
 * exceptions in Selenium WebDriver operations, as seen in {@code ExceptionHandlingWebDriver}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
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
