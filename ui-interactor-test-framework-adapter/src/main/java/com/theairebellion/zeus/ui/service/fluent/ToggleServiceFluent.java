package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.selenium.ToggleUIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Provides fluent methods for interacting with toggle UI elements.
 * This service allows activation, deactivation, and validation of toggles.
 *
 * @author Cyborg Code Syndicate
 */
public class ToggleServiceFluent<T extends UIServiceFluent<?>> {

    private final ToggleService toggleService;
    private final T uiServiceFluent;
    private final Storage storage;

    /**
     * Constructs a {@code ToggleServiceFluent} instance.
     *
     * @param uiServiceFluent The fluent UI service instance.
     * @param storage         The storage instance for storing toggle states.
     * @param toggleService   The toggle service responsible for interacting with toggle components.
     */
    public ToggleServiceFluent(T uiServiceFluent, Storage storage, ToggleService toggleService) {
        this.uiServiceFluent = uiServiceFluent;
        this.toggleService = toggleService;
        this.storage = storage;
    }

    /**
     * Activates the specified toggle element.
     *
     * @param element The {@link ToggleUIElement} representing the toggle component.
     * @return The fluent UI service instance.
     */
    public T activate(final ToggleUIElement element) {
        Allure.step("[UI - Toggle] Activating the toggle element: " + element);
        toggleService.activate(element.componentType(), element.locator());
        return uiServiceFluent;
    }

    /**
     * Deactivates the specified toggle element.
     *
     * @param element The {@link ToggleUIElement} representing the toggle component.
     * @return The fluent UI service instance.
     */
    public T deactivate(final ToggleUIElement element) {
        Allure.step("[UI - Toggle] Deactivating the toggle element: " + element);
        toggleService.deactivate(element.componentType(), element.locator());
        return uiServiceFluent;
    }

    /**
     * Checks if the specified toggle element is activated.
     *
     * @param element The {@link ToggleUIElement} representing the toggle component.
     * @return The fluent UI service instance.
     */
    public T isActivated(final ToggleUIElement element) {
        Allure.step("[UI - Toggle] Checking if the toggle element is activated: " + element);
        boolean enabled = toggleService.isActivated(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Checks if the specified toggle element is enabled.
     *
     * @param element The {@link ToggleUIElement} representing the toggle component.
     * @return The fluent UI service instance.
     */
    public T isEnabled(final ToggleUIElement element) {
        Allure.step("[UI - Toggle] Checking if the toggle element is enabled: " + element);
        boolean enabled = toggleService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

}
