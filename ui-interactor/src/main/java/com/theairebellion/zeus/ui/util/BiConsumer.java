package com.theairebellion.zeus.ui.util;

/**
 * Represents an operation that accepts two input arguments and performs an action without returning a result.
 * <p>
 * This is a functional interface, meaning it can be used as the target for lambda expressions and method references.
 * Similar to {@link java.util.function.BiConsumer}, it allows defining custom two-parameter operations.
 * </p>
 * <p>
 * The first input argument, {@code T}, represents the primary entity on which the operation is performed.
 * The second input argument, {@code U}, provides additional data or context necessary for the operation.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface BiConsumer<T, U> {

    /**
     * Performs the defined operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);
}
