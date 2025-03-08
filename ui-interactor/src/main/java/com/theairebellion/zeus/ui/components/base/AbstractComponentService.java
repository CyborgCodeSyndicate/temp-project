package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Provides an abstract base class for managing UI component services.
 * <p>
 * This class serves as a generic service layer for handling UI components of various types.
 * It maintains a registry of component instances and ensures efficient reuse.
 * </p>
 * <p>
 * Implementing classes must define how components of type {@code C} are created based on a {@code ComponentType}.
 * </p>
 *
 * @param <T> The component type enumeration implementing {@link ComponentType}.
 * @param <C> The concrete UI component implementation.
 * @author Cyborg Code Syndicate
 */
public abstract class AbstractComponentService<T extends ComponentType, C> {

    /**
     * The {@code SmartWebDriver} instance used for UI interactions.
     */
    protected final SmartWebDriver driver;

    /**
     * A registry that stores component instances mapped to their types.
     */
    protected final Map<T, C> components;

    /**
     * Constructs an {@code AbstractComponentService} with the given WebDriver.
     *
     * @param driver The {@code SmartWebDriver} instance used for component interactions.
     * @throws NullPointerException if the provided driver is {@code null}.
     */
    protected AbstractComponentService(SmartWebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "Driver must not be null");
        this.components = new HashMap<>();
    }

    /**
     * Retrieves a component instance for the specified type.
     * <p>
     * If a component of the given type does not exist, it is created and stored.
     * </p>
     *
     * @param componentType The type of component to retrieve.
     * @return The corresponding UI component instance.
     */
    protected C getOrCreateComponent(T componentType) {
        return components.computeIfAbsent(componentType, this::createComponent);
    }

    /**
     * Creates a new UI component instance for the specified type.
     * <p>
     * Subclasses must provide an implementation to initialize components of type {@code C}.
     * </p>
     *
     * @param componentType The type of component to create.
     * @return The newly created UI component instance.
     */
    protected abstract C createComponent(T componentType);

}
