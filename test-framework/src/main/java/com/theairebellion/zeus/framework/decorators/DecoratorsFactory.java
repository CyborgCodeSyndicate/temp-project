package com.theairebellion.zeus.framework.decorators;

import org.springframework.stereotype.Component;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Factory for creating and caching decorated instances of objects.
 * <p>
 * This class provides a mechanism to dynamically apply decorators to objects at runtime,
 * ensuring that the same decorated instance is reused whenever possible.
 * </p>
 *
 * <p>
 * The factory maintains a cache of decorated objects using an {@code IdentityHashMap},
 * allowing efficient retrieval of previously created decorators. It uses reflection
 * to instantiate decorators by searching for a constructor that accepts the target
 * object's class or its superclass.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
public class DecoratorsFactory {

    /**
     * Cache for storing decorated instances mapped to their original objects.
     */
    private final Map<Object, Object> cache = new IdentityHashMap<>();

    /**
     * Decorates a given target object with the specified decorator class.
     * <p>
     * If the target object has already been decorated with the requested class,
     * the existing decorated instance is returned from the cache.
     * Otherwise, a new decorator instance is created and stored.
     * </p>
     *
     * @param target         The object to be decorated.
     * @param decoratorClass The class of the decorator to apply.
     * @param <T>            The type of the target object.
     * @param <K>            The type of the decorator class.
     * @return The decorated instance, or {@code null} if the target is {@code null}.
     * @throws IllegalStateException If no suitable constructor is found for the decorator.
     */
    @SuppressWarnings("unchecked")
    public <T, K> K decorate(T target, Class<K> decoratorClass) {
        if (target == null) {
            return null;
        }

        if (cache.containsKey(target) && cache.get(target).getClass().equals(decoratorClass)) {
            return (K) cache.get(target);
        }

        K decorator;
        try {
            decorator = decoratorClass
                    .getDeclaredConstructor(target.getClass())
                    .newInstance(target);
        } catch (NoSuchMethodException e) {
            try {
                decorator = decoratorClass
                        .getDeclaredConstructor(target.getClass().getSuperclass())
                        .newInstance(target);
            } catch (ReflectiveOperationException e2) {
                throw new IllegalStateException(
                        "Failed to create decorator. "
                                + "No matching constructor found for " + decoratorClass.getName()
                                + " with " + target.getClass().getName()
                                + " or its superclass: " + target.getClass().getSuperclass().getName(),
                        e2
                );
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Failed to create decorator " + decoratorClass.getName()
                            + " for object " + target.getClass().getName(),
                    e
            );
        }

        cache.put(target, decorator);
        return decorator;
    }
}