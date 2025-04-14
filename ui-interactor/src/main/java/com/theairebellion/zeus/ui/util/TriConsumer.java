package com.theairebellion.zeus.ui.util;

/**
 * Represents an operation that accepts three input arguments and performs an action.
 *
 * <p>This functional interface is similar to {@link java.util.function.BiConsumer}, but it
 * supports three parameters instead of two. It is primarily used for handling operations
 * that require three input parameters in a functional manner.
 *
 * <p>The first argument, {@code T}, represents the primary entity involved in the operation.
 * The second argument, {@code U}, provides contextual information or additional parameters.
 * The third argument, {@code K}, captures supplementary data relevant to the action.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@FunctionalInterface
public interface TriConsumer<T, U, K> {

   /**
    * Performs the operation given the specified inputs.
    *
    * @param t The first input argument.
    * @param u The second input argument.
    * @param k The third input argument.
    */
   void accept(T t, U u, K k);
}
