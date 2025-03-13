package com.theairebellion.zeus.framework.parameters;

/**
 * Represents a deferred or lazy evaluation of an object.
 * <p>
 * Implementations of this interface provide a mechanism for delaying
 * the creation of an object until it is explicitly needed.
 * This is particularly useful for scenarios where test data or
 * dependencies should not be instantiated at the beginning of a test
 * but rather at a specific point during execution.
 * </p>
 *
 * <p>The generic type {@code T} represents the type of object that will be
 * lazily instantiated.</p>
 *
 * @author Cyborg Code Syndicate
 */
@FunctionalInterface
public interface Late<T> {

    /**
     * Retrieves or initializes the object when required.
     * <p>
     * The actual instantiation logic is deferred until this method is invoked.
     * The returned object should be fully constructed and ready for use.
     * </p>
     *
     * @return The instantiated object of type {@code T}.
     */
    T join();

}
