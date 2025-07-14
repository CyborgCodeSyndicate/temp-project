package com.theairebellion.zeus.ui.util;

/**
 * Represents a function that accepts three input arguments and produces a result.
 *
 * <p>This functional interface is similar to {@link java.util.function.BiFunction},
 * but it supports three parameters instead of two. It is useful in scenarios
 * where a computation or transformation requires three inputs to generate an output.
 *
 * <p>The first argument, {@code T}, represents the primary input or entity being processed.
 * The second argument, {@code U}, provides supporting data or configuration.
 * The third argument, {@code V}, offers additional context necessary for computation.
 * The function then produces a result of type {@code R}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
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
