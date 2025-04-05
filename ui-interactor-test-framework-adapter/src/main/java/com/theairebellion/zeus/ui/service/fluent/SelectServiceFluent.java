package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.SelectUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Fluent service class for interacting with select dropdown components.
 * <p>
 * Provides methods for selecting, validating, and retrieving options from dropdown elements.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * enabling method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class SelectServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final SelectService selectService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code SelectServiceFluent} instance.
     *
     * @param uiServiceFluent The UI service fluent instance.
     * @param storage         The storage instance for storing UI-related data.
     * @param selectService   The select service instance for performing dropdown operations.
     * @param smartWebDriver  The web driver instance used for interacting with UI elements.
     */
    public SelectServiceFluent(T uiServiceFluent, Storage storage, SelectService selectService,
                               SmartWebDriver smartWebDriver) {
        this.selectService = selectService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = smartWebDriver;
    }

    /**
     * Selects multiple options in the specified select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param values  The values to be selected.
     * @return The fluent UI service instance.
     */
    public T selectOptions(final SelectUIElement element, final String... values) {
        Allure.step("[UI - Select] Selecting multiple options in dropdown: " + element.enumImpl() + " (Values: " + Arrays.toString(values) + ")");
        element.before().accept(driver);
        selectService.selectOptions(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Selects a single option in the specified select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The value to be selected.
     * @return The fluent UI service instance.
     */
    public T selectOption(final SelectUIElement element, final String value) {
        Allure.step("[UI - Select] Selecting option in dropdown: " + element.enumImpl() + " (Value: " + value + ")");
        element.before().accept(driver);
        selectService.selectOption(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Selects an option using a defined strategy.
     *
     * @param element  The {@link SelectUIElement} representing the dropdown.
     * @param strategy The strategy for selecting the option.
     * @return The fluent UI service instance.
     */
    public T selectOptions(final SelectUIElement element, final Strategy strategy) {
        Allure.step("[UI - Select] Selecting option in dropdown: " + element.enumImpl() + " (Strategy: " + strategy + ")");
        element.before().accept(driver);
        selectService.selectOptions(element.componentType(), element.locator(), strategy);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Retrieves the available options in the specified select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @return The fluent UI service instance.
     */
    public T getAvailableOptions(final SelectUIElement element) {
        Allure.step("[UI - Select] Retrieving available options in dropdown: " + element.enumImpl());
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);
        return uiServiceFluent;
    }

    /**
     * Validates that the available options in the specified select element match the expected values.
     *
     * @param element        The {@link SelectUIElement} representing the dropdown.
     * @param expectedValues The expected option values.
     * @return The fluent UI service instance.
     */
    public T validateAvailableOptions(final SelectUIElement element, final String... expectedValues) {
        Allure.step("[UI - Select] Validating available options in dropdown: " + element.enumImpl() + " (Expected Values: " + Arrays.toString(expectedValues) + ")");
        return validateAvailableOptions(element, false, expectedValues);
    }

    /**
     * Validates that the available options in the specified select element match the expected values.
     *
     * @param element        The {@link SelectUIElement} representing the dropdown.
     * @param soft           A boolean indicating whether the validation should be performed softly.
     *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValues The expected option values.
     * @return The fluent UI service instance.
     */
    public T validateAvailableOptions(final SelectUIElement element, boolean soft, final String... expectedValues) {
        Allure.step("[UI - Select] Validating available options in dropdown: " + element.enumImpl() + " (Soft: " + soft + ", Expected Values: " + Arrays.toString(expectedValues) + ")");
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(availableOptions)
                            .as("Validating Available Options").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(availableOptions)
                            .as("Validating Available Options").containsAll(Arrays.asList(expectedValues))
            );
        }
    }

    /**
     * Validates that the number of available options in the specified select element matches the expected count.
     *
     * @param element             The {@link SelectUIElement} representing the dropdown.
     * @param expectedValuesCount The expected number of available options.
     * @return The fluent UI service instance.
     */
    public T validateAvailableOptions(final SelectUIElement element, final int expectedValuesCount) {
        Allure.step("[UI - Select] Validating number of available options in dropdown: " + element.enumImpl() + " (Expected Count: " + expectedValuesCount + ")");
        return validateAvailableOptions(element, false, expectedValuesCount);
    }

    /**
     * Validates that the number of available options in the specified select element matches the expected count.
     *
     * @param element             The {@link SelectUIElement} representing the dropdown.
     * @param soft                A boolean indicating whether the validation should be performed softly.
     *                            If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValuesCount The expected number of available options.
     * @return The fluent UI service instance.
     */
    public T validateAvailableOptions(final SelectUIElement element, boolean soft, final int expectedValuesCount) {
        Allure.step("[UI - Select] Validating number of available options in dropdown: " + element.enumImpl() + " (Soft: " + soft + ", Expected Count: " + expectedValuesCount + ")");
        element.before().accept(driver);
        List<String> availableOptions = selectService.getAvailableOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), availableOptions);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(availableOptions.size())
                            .as("Validating Available Options").isEqualTo(expectedValuesCount)
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(availableOptions.size())
                            .as("Validating Available Options").isEqualTo(expectedValuesCount)
            );
        }
    }

    /**
     * Retrieves the selected options in the specified select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @return The fluent UI service instance.
     */
    public T getSelectedOptions(final SelectUIElement element) {
        Allure.step("[UI - Select] Retrieving selected options in dropdown: " + element.enumImpl());
        element.before().accept(driver);
        List<String> selectedOptions = selectService.getSelectedOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedOptions);
        return uiServiceFluent;
    }

    /**
     * Validates that the selected options in the specified select element match the expected values.
     *
     * @param element        The {@link SelectUIElement} representing the dropdown.
     * @param expectedValues The expected selected option values.
     * @return The fluent UI service instance.
     */
    public T validateSelectedOptions(final SelectUIElement element, final String... expectedValues) {
        Allure.step("[UI - Select] Validating selected options in dropdown: " + element.enumImpl() + " (Expected Values: " + Arrays.toString(expectedValues) + ")");
        return validateSelectedOptions(element, false, expectedValues);
    }

    /**
     * Validates that the selected options in the specified select element match the expected values.
     *
     * @param element        The {@link SelectUIElement} representing the dropdown.
     * @param soft           A boolean indicating whether the validation should be performed softly.
     *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValues The expected selected option values.
     * @return The fluent UI service instance.
     */
    public T validateSelectedOptions(final SelectUIElement element, boolean soft, final String... expectedValues) {
        Allure.step("[UI - Select] Validating selected options in dropdown: " + element.enumImpl() + " (Soft: " + soft + ", Expected Values: " + Arrays.toString(expectedValues) + ")");
        element.before().accept(driver);
        List<String> selectedOptions = selectService.getSelectedOptions(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedOptions);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedOptions)
                            .as("Validating Selected Options").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedOptions)
                            .as("Validating Selected Options").containsAll(Arrays.asList(expectedValues))
            );
        }
    }

    /**
     * Checks whether a specific option is visible in the select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value to check for visibility.
     * @return The fluent UI service instance.
     */
    public T isOptionVisible(final SelectUIElement element, final String value) {
        Allure.step("[UI - Select] Checking if option " + value + " is visible in select element " + element);
        element.before().accept(driver);
        boolean visibleOption = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visibleOption);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified option in the select element is visible.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be visible.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionVisible(final SelectUIElement element, final String value) {
        return validateIsVisible(element, true, value, false);
    }

    /**
     * Validates that the specified option in the select element is visible.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be visible.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionVisible(final SelectUIElement element, final String value, boolean soft) {
        return validateIsVisible(element, true, value, soft);
    }

    /**
     * Validates that the specified option in the select element is hidden.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be hidden.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionHidden(final SelectUIElement element, final String value) {
        return validateIsVisible(element, false, value, false);
    }

    /**
     * Validates that the specified option in the select element is hidden.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be hidden.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionHidden(final SelectUIElement element, final String value, boolean soft) {
        return validateIsVisible(element, false, value, soft);
    }

    private T validateIsVisible(final SelectUIElement element, boolean shouldBeVisible, String value,
                                boolean soft) {
        Allure.step("[UI - Select] Validating visibility of option " + value + " in select element " + element);
        element.before().accept(driver);
        boolean visible = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Option is visible"
                : "Validating Option is hidden";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeVisible) {
                            softAssertions.assertThat(visible).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(visible).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeVisible) {
                            Assertions.assertThat(visible).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(visible).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }

    /**
     * Checks whether a specific option is enabled in the select element.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value to check for enabled state.
     * @return The fluent UI service instance.
     */
    public T isOptionEnabled(final SelectUIElement element, final String value) {
        Allure.step("[UI - Select] Checking if option " + value + " is enabled in select element " + element);
        element.before().accept(driver);
        boolean enabledOption = selectService.isOptionEnabled(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabledOption);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified option in the select element is enabled.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be enabled.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionEnabled(final SelectUIElement element, final String value) {
        return validateIsEnabled(element, true, value, false);
    }

    /**
     * Validates that the specified option in the select element is enabled.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be enabled.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionEnabled(final SelectUIElement element, final String value, boolean soft) {
        return validateIsEnabled(element, true, value, soft);
    }

    /**
     * Validates that the specified option in the select element is disabled.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be disabled.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionDisabled(final SelectUIElement element, final String value) {
        return validateIsEnabled(element, false, value, false);
    }

    /**
     * Validates that the specified option in the select element is disabled.
     *
     * @param element The {@link SelectUIElement} representing the dropdown.
     * @param value   The option value that should be disabled.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsOptionDisabled(final SelectUIElement element, final String value, boolean soft) {
        return validateIsEnabled(element, false, value, soft);
    }

    private T validateIsEnabled(final SelectUIElement element, boolean shouldBeEnabled, String value,
                                boolean soft) {
        Allure.step("[UI - Select] Validating whether option " + value + " is " + (shouldBeEnabled ? "enabled" : "disabled") + " in select element " + element);
        element.before().accept(driver);
        boolean enabled = selectService.isOptionVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Option is enabled"
                : "Validating Option is disabled";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeEnabled) {
                            softAssertions.assertThat(enabled).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(enabled).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeEnabled) {
                            Assertions.assertThat(enabled).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(enabled).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }

    /**
     * Inserts values into a select element.
     *
     * @param componentType The component type.
     * @param locator       The locator of the element.
     * @param values        The values to insert.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        Allure.step("[UI - Select] Inserting values into select element with component type " + componentType + " and locator " + locator);
        selectService.insertion(componentType, locator, values);
    }

}
