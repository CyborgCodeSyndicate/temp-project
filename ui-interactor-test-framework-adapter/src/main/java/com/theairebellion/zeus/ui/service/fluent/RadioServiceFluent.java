package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.RadioUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

/**
 * A fluent service class for interacting with radio button components in a UI automation framework.
 * <p>
 * Provides methods for selecting, validating, enabling, and retrieving radio button values.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * enabling method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class RadioServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final RadioService radioService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code RadioServiceFluent} instance.
     *
     * @param uiServiceFluent The parent fluent UI service instance.
     * @param storage         The storage instance for saving UI-related values.
     * @param radioService    The service responsible for handling radio button interactions.
     * @param webDriver       The smart web driver instance.
     */
    public RadioServiceFluent(T uiServiceFluent, Storage storage, RadioService radioService,
                              SmartWebDriver webDriver) {
        this.radioService = radioService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Selects the given radio button element.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T select(final RadioUIElement element) {
        Allure.step(String.format("Selecting Radio Input with locator: '%s' from radio component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        radioService.select(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the specified radio button is enabled.
     *
     * @param element The {@link RadioUIElement} to check.
     * @return The fluent UI service instance.
     */
    public T isEnabled(final RadioUIElement element) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates whether the specified radio button element is enabled.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final RadioUIElement element) {
        return validateIsEnabled(element, true, false);
    }

    /**
     * Validates whether the radio button is enabled.
     *
     * @param element The {@link RadioUIElement} to validate.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final RadioUIElement element, boolean soft) {
        return validateIsEnabled(element, true, soft);
    }

    /**
     * Validates whether the specified radio button element is disabled.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final RadioUIElement element) {
        return validateIsEnabled(element, false, false);
    }

    /**
     * Validates whether the radio button is disabled.
     *
     * @param element The {@link RadioUIElement} to validate.
     * @param soft    Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final RadioUIElement element, boolean soft) {
        return validateIsEnabled(element, false, soft);
    }

    private T validateIsEnabled(final RadioUIElement element, boolean shouldBeEnabled, boolean soft) {
        element.before().accept(driver);
        boolean enabled = radioService.isEnabled(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating Radio Input is enabled"
                : "Validating Radio Input is disabled";

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
     * Checks if the specified radio button is selected.
     *
     * @param element The {@link RadioUIElement} to check.
     * @return The fluent UI service instance.
     */
    public T isSelected(final RadioUIElement element) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    /**
     * Validates whether the specified radio button element is selected.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final RadioUIElement element) {
        return validateIsSelected(element, true, false);
    }

    /**
     * Validates whether the specified radio button element is selected, with an option for soft validation.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final RadioUIElement element, boolean soft) {
        return validateIsSelected(element, true, soft);
    }

    /**
     * Validates whether the specified radio button element is not selected.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final RadioUIElement element) {
        return validateIsSelected(element, false, false);
    }

    /**
     * Validates whether the specified radio button element is not selected, with an option for soft validation.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final RadioUIElement element, boolean soft) {
        return validateIsSelected(element, false, soft);
    }

    private T validateIsSelected(final RadioUIElement element, boolean shouldBeSelected, boolean soft) {
        element.before().accept(driver);
        boolean selected = radioService.isSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating Radio Input is selected"
                : "Validating Radio Input is not selected";

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeSelected) {
                            softAssertions.assertThat(selected).as(assertionMessage).isTrue();
                        } else {
                            softAssertions.assertThat(selected).as(assertionMessage).isFalse();
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeSelected) {
                            Assertions.assertThat(selected).as(assertionMessage).isTrue();
                        } else {
                            Assertions.assertThat(selected).as(assertionMessage).isFalse();
                        }
                    }
            );
        }
    }

    /**
     * Checks whether the specified radio button element is visible.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T isVisible(final RadioUIElement element) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates whether the specified radio button element is visible.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final RadioUIElement element) {
        return validateIsVisible(element, true, false);
    }

    /**
     * Validates whether the specified radio button element is visible, with an option for soft validation.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final RadioUIElement element, boolean soft) {
        return validateIsVisible(element, true, soft);
    }

    /**
     * Validates whether the specified radio button element is hidden.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final RadioUIElement element) {
        return validateIsVisible(element, false, false);
    }

    /**
     * Validates whether the specified radio button element is hidden, with an option for soft validation.
     *
     * @param element The {@link RadioUIElement} representing the radio button.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final RadioUIElement element, boolean soft) {
        return validateIsVisible(element, false, soft);
    }

    private T validateIsVisible(final RadioUIElement element, boolean shouldBeVisible, boolean soft) {
        element.before().accept(driver);
        boolean visible = radioService.isVisible(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating Radio Input is visible"
                : "Validating Radio Input is hidden";

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
     * Retrieves the selected radio button's value.
     *
     * @param element The {@link RadioUIElement} to check.
     * @return The fluent UI service instance.
     */
    public T getSelected(final RadioUIElement element) {
        element.before().accept(driver);
        String selectedValue = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedValue);
        return uiServiceFluent;
    }

    /**
     * Validates whether the specified radio button element has the expected selected value.
     *
     * @param element  The {@link RadioUIElement} representing the radio button.
     * @param expected The expected value of the selected radio button.
     * @return The fluent UI service instance.
     */
    public T validateSelected(final RadioUIElement element, String expected) {
        return validateSelected(element, expected, false);
    }

    /**
     * Validates that the selected radio button matches the expected value.
     *
     * @param element  The {@link RadioUIElement} to validate.
     * @param expected The expected value.
     * @param soft     Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateSelected(final RadioUIElement element, String expected, boolean soft) {
        element.before().accept(driver);
        String selectedRadioInput = radioService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInput);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedRadioInput)
                            .as("Validating Selected Radio Input").isEqualTo(expected)
            );
        }
    }

    /**
     * Retrieves all available radio button options.
     *
     * @param element The {@link RadioUIElement} representing the radio button group.
     * @return The fluent UI service instance.
     */
    public T getAll(final RadioUIElement element) {
        element.before().accept(driver);
        radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Validates that all available radio button inputs within the specified element match the expected values.
     *
     * @param element        The {@link RadioUIElement} representing the radio button group.
     * @param expectedValues The expected values of all selectable radio inputs.
     * @return The fluent UI service instance.
     */
    public T validateAllRadioInputs(final RadioUIElement element, final String... expectedValues) {
        return validateAllRadioInputs(element, false, expectedValues);
    }

    /**
     * Validates that the available radio button options match the expected values.
     *
     * @param element        The {@link RadioUIElement} to validate.
     * @param expectedValues The expected options.
     * @param soft           Whether to perform a soft assertion.
     * @return The fluent UI service instance.
     */
    public T validateAllRadioInputs(final RadioUIElement element, boolean soft,
                                    final String... expectedValues) {
        element.before().accept(driver);
        List<String> selectedRadioInputs = radioService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedRadioInputs);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedRadioInputs)
                            .as("Validating Radio Inputs").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedRadioInputs)
                            .as("Validating Radio Inputs").containsAll(Arrays.asList(expectedValues))
            );
        }
    }

    /**
     * Performs an insertion operation on the specified radio button element.
     *
     * @param componentType The type of the component.
     * @param locator       The locator of the radio button.
     * @param values        The values to insert.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        radioService.insertion(componentType, locator, values);
    }

}
