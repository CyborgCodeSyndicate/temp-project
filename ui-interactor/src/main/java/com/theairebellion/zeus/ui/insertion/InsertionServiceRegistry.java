package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing {@link Insertion} services for various UI components.
 * <p>
 * This class provides a centralized way to register and retrieve insertion services
 * based on specific {@link ComponentType} implementations. It ensures that the correct
 * service is used for handling UI interactions related to data insertion.
 * </p>
 *
 * <p>Uses a {@link ConcurrentHashMap} to support thread-safe access and modifications.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class InsertionServiceRegistry {

    /**
     * Thread-safe registry mapping component types to their respective insertion services.
     */
    private final Map<Class<? extends ComponentType>, Insertion> registry = new ConcurrentHashMap<>();

    /**
     * Registers an {@link Insertion} service for a specific component type.
     *
     * @param type    The class representing the {@link ComponentType}.
     * @param service The {@link Insertion} service to associate with the component type.
     */
    public void registerService(Class<? extends ComponentType> type, Insertion service) {
        registry.put(type, service);
    }

    /**
     * Retrieves the registered {@link Insertion} service for a given component type.
     *
     * @param type The class representing the {@link ComponentType}.
     * @return The corresponding {@link Insertion} service, or {@code null} if none is registered.
     */
    public Insertion getService(Class<? extends ComponentType> type) {
        return registry.get(type);
    }
}
