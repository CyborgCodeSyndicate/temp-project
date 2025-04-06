package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.ListUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.storage.StorageKeysUi.UI;

/**
 * Fluent service for interacting with list UI elements.
 * <p>
 * Provides methods for selecting, validating, and interacting with lists in a structured manner.
 * </p>
 *
 * The generic type {@code T} represents the UI service fluent implementation that extends {@link UIServiceFluent},
 * allowing method chaining for seamless interaction.
 *
 * @author Cyborg Code Syndicate
 */
public class ListServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final ItemListService itemListService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;

    /**
     * Constructs a new ListServiceFluent instance.
     *
     * @param uiServiceFluent The UI service fluent instance.
     * @param storage         The storage instance for UI states.
     * @param itemListService The list service for interacting with UI elements.
     * @param webDriver       The smart web driver instance.
     */
    public ListServiceFluent(T uiServiceFluent, Storage storage, ItemListService itemListService,
                             SmartWebDriver webDriver) {
        this.itemListService = itemListService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }

    /**
     * Selects items from the list.
     *
     * @param element The list UI element.
     * @param values  The values to select.
     * @return The fluent UI service instance.
     */
    public T select(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] [UI - List] Select values from list: " + Arrays.toString(values)); // Allure step added
        element.before().accept(driver);
        itemListService.select(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Deselects items from the list.
     *
     * @param element The list UI element.
     * @param values  The values to deselect.
     * @return The fluent UI service instance.
     */
    public T deSelect(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Deselect values from list: " + Arrays.toString(values)); // Allure step added
        element.before().accept(driver);
        itemListService.deSelect(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }

    /**
     * Checks if the given values are selected in the list.
     *
     * @param element The list UI element.
     * @param values  The values to check.
     * @return The fluent UI service instance.
     */
    public T areSelected(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Check if values are selected in the list: " + Arrays.toString(values)); // Allure step added
        element.before().accept(driver);
        boolean selected = itemListService.areSelected(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    /**
     * Validates that the given values are selected in the list.
     *
     * @param element The list UI element.
     * @param values  The expected selected values.
     * @return The fluent UI service instance.
     */
    public T validateAreSelected(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate if values are selected in the list: " + Arrays.toString(values)); // Allure step added
        return validateAreSelected(element, true, false, values);
    }

    /**
     * Validates that the specified values are selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The expected values that should be selected in the list.
     * @return The fluent UI service instance.
     */
    public T validateAreSelected(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate if values are selected (soft: " + soft + "): " + Arrays.toString(values)); // Allure step added
        return validateAreSelected(element, true, soft, values);
    }

    /**
     * Validates that the given values are not selected in the list.
     *
     * @param element The list UI element.
     * @param values  The expected unselected values.
     * @return The fluent UI service instance.
     */
    public T validateAreNotSelected(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate if values are not selected in the list: " + Arrays.toString(values)); // Allure step added
        return validateAreSelected(element, false, false, values);
    }

    /**
     * Validates that the specified values are not selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The values that should not be selected in the list.
     * @return The fluent UI service instance.
     */
    public T validateAreNotSelected(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate if values are not selected (soft: " + soft + "): " + Arrays.toString(values)); // Allure step added
        return validateAreSelected(element, false, soft, values);
    }

    private T validateAreSelected(final ListUIElement element, boolean shouldBeSelected, boolean soft,
                                  final String... values) {
        element.before().accept(driver);
        boolean selected = itemListService.areSelected(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);

        String assertionMessage = shouldBeSelected
                ? "Validating List Items are selected"
                : "Validating List Items are not selected";

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
     * Checks if the specified value is selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The value to check for selection.
     * @return The fluent UI service instance.
     */
    public T isSelected(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Check if value is selected in the list: " + value); // Allure step added
        element.before().accept(driver);
        boolean selected = itemListService.isSelected(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified value is selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The value that should be selected.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate if value is selected in the list: " + value); // Allure step added
        return validateAreSelected(element, true, false, value);
    }

    /**
     * Validates that the specified value is selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The value that should be selected.
     * @return The fluent UI service instance.
     */
    public T validateIsSelected(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate if value is selected (soft: " + soft + "): " + value); // Allure step added
        return validateAreSelected(element, true, soft, value);
    }

    /**
     * Validates that the specified value is not selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The value that should not be selected.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate if value is not selected in the list: " + value); // Allure step added
        return validateAreSelected(element, false, false, value);
    }

    /**
     * Validates that the specified value is not selected in the given list element.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The value that should not be selected.
     * @return The fluent UI service instance.
     */
    public T validateIsNotSelected(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate if value is not selected (soft: " + soft + "): " + value); // Allure step added
        return validateAreSelected(element, false, soft, value);
    }

    /**
     * Checks if the given values are enabled in the list.
     *
     * @param element The list UI element.
     * @param values  The values to check.
     * @return The fluent UI service instance.
     */
    public T areEnabled(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Check if values are enabled in the list: " + Arrays.toString(values)); // Allure step added
        element.before().accept(driver);
        boolean enabled = itemListService.areEnabled(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates that the list items are enabled.
     *
     * @param element The list UI element.
     * @param values  The values to validate.
     * @return The fluent UI service instance.
     */
    public T validateAreEnabled(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate if values are enabled in the list: " + Arrays.toString(values)); // Allure step added
        return validateAreEnabled(element, true, false, values);
    }

    /**
     * Validates that the specified values in the given list element are enabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The values that should be enabled.
     * @return The fluent UI service instance.
     */
    public T validateAreEnabled(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate if values are enabled (soft: " + soft + "): " + Arrays.toString(values)); // Allure step added
        return validateAreEnabled(element, true, soft, values);
    }

    /**
     * Validates that the specified values in the given list element are disabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param values  The values that should be disabled.
     * @return The fluent UI service instance.
     */
    public T validateAreDisabled(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate that the specified values in the list are disabled");
        return validateAreEnabled(element, false, false, values);
    }

    /**
     * Validates that the specified values in the given list element are disabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The values that should be disabled.
     * @return The fluent UI service instance.
     */
    public T validateAreDisabled(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate that the specified values in the list are disabled");
        return validateAreEnabled(element, false, soft, values);
    }

    private T validateAreEnabled(final ListUIElement element, boolean shouldBeEnabled, boolean soft,
                                 final String... values) {
        element.before().accept(driver);
        boolean enabled = itemListService.areEnabled(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);

        String assertionMessage = shouldBeEnabled
                ? "Validating List Items are enabled"
                : "Validating List Items are disabled";

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
     * Checks if the specified value in the given list element is enabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value to check for being enabled.
     * @return The fluent UI service instance.
     */
    public T isEnabled(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Check if the specified value is enabled");

        element.before().accept(driver);
        boolean enabled = itemListService.isEnabled(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified value in the given list element is enabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value that should be enabled.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate that the specified value is enabled");
        return validateAreEnabled(element, true, false, value);
    }

    /**
     * Validates that the specified value in the given list element is enabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The specific value that should be enabled.
     * @return The fluent UI service instance.
     */
    public T validateIsEnabled(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate that the specified value is enabled");
        return validateAreEnabled(element, true, soft, value);
    }

    /**
     * Validates that the specified value in the given list element is disabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value that should be disabled.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate that the specified value is disabled");
        return validateAreEnabled(element, false, false, value);
    }

    /**
     * Validates that the specified value in the given list element is disabled.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The specific value that should be disabled.
     * @return The fluent UI service instance.
     */
    public T validateIsDisabled(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate that the specified value is disabled");
        return validateAreEnabled(element, false, soft, value);
    }

    /**
     * Checks if the given values are visible in the list.
     *
     * @param element The list UI element.
     * @param values  The values to check.
     * @return The fluent UI service instance.
     */
    public T areVisible(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Check if the specified values are visible");

        element.before().accept(driver);
        boolean visible = itemListService.areVisible(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates that the given values are visible in the list.
     *
     * @param element The list UI element.
     * @param values  The expected visible values.
     * @return The fluent UI service instance.
     */
    public T validateAreVisible(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate that the specified values are visible in the list");
        return validateAreVisible(element, true, false, values);
    }

    /**
     * Validates that the specified values in the given list element are visible.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The specific values that should be visible.
     * @return The fluent UI service instance.
     */
    public T validateAreVisible(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate that the specified values are visible in the list");
        return validateAreVisible(element, true, soft, values);
    }

    /**
     * Validates that the specified values in the given list element are hidden.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param values  The specific values that should be hidden.
     * @return The fluent UI service instance.
     */
    public T validateAreHidden(final ListUIElement element, final String... values) {
        Allure.step("[UI - List] Validate that the specified values are hidden in the list");
        return validateAreVisible(element, false, false, values);
    }

    /**
     * Validates that the specified values in the given list element are hidden.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param values  The specific values that should be hidden.
     * @return The fluent UI service instance.
     */
    public T validateAreHidden(final ListUIElement element, boolean soft, final String... values) {
        Allure.step("[UI - List] Validate that the specified values are hidden in the list");
        return validateAreVisible(element, false, soft, values);
    }

    private T validateAreVisible(final ListUIElement element, boolean shouldBeVisible, boolean soft,
                                 final String... values) {
        element.before().accept(driver);
        boolean visible = itemListService.areVisible(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);

        String assertionMessage = shouldBeVisible
                ? "Validating List Items are visible"
                : "Validating List Items are hidden";

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
     * Checks if the specified value in the given list element is visible.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value to check for visibility.
     * @return The fluent UI service instance.
     */
    public T isVisible(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Check if the specified value is visible");

        element.before().accept(driver);
        boolean visible = itemListService.isVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified value in the given list element is visible.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value that should be visible.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate that the specified value is visible");
        return validateAreVisible(element, true, false, value);
    }

    /**
     * Validates that the specified value in the given list element is visible.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The specific value that should be visible.
     * @return The fluent UI service instance.
     */
    public T validateIsVisible(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate that the specified value is visible");
        return validateAreVisible(element, true, soft, value);
    }

    /**
     * Validates that the specified value in the given list element is hidden.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param value   The specific value that should be hidden.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final ListUIElement element, final String value) {
        Allure.step("[UI - List] Validate that the specified value is hidden");
        return validateAreVisible(element, false, false, value);
    }

    /**
     * Validates that the specified value in the given list element is hidden.
     *
     * @param element The {@link ListUIElement} representing the list UI component.
     * @param soft    A boolean indicating whether the validation should be performed softly.
     *                If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param value   The specific value that should be hidden.
     * @return The fluent UI service instance.
     */
    public T validateIsHidden(final ListUIElement element, boolean soft, final String value) {
        Allure.step("[UI - List] Validate that the specified value is hidden");
        return validateAreVisible(element, false, soft, value);
    }

    /**
     * Retrieves the selected items from the list.
     *
     * @param element The list UI element.
     * @return The fluent UI service instance.
     */
    public T getSelected(final ListUIElement element) {
        Allure.step("[UI - List] Retrieve selected items from the list");
        element.before().accept(driver);
        List<String> selectedItems = itemListService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedItems);
        return uiServiceFluent;
    }

    /**
     * Validates that the specified items are selected within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param expectedValues The expected values that should be selected.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateSelectedItems(final ListUIElement element, final String... expectedValues) {
        Allure.step("[UI - List] Validate that the specified items are selected");
        return validateSelectedItems(element, true, false, expectedValues);
    }

    /**
     * Validates that the specified items are selected within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param soft           A boolean indicating whether the validation should be performed softly.
     *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValues The expected values that should be selected.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateSelectedItems(final ListUIElement element, boolean soft, final String... expectedValues) {
        Allure.step("[UI - List] Validate that the specified items are selected");
        return validateSelectedItems(element, true, soft, expectedValues);
    }

    /**
     * Validates that the specified items are not selected within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param expectedValues The expected values that should not be selected.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateNotSelectedItems(final ListUIElement element, final String... expectedValues) {
        Allure.step("[UI - List] Validate that the specified items are not selected");
        return validateSelectedItems(element, false, false, expectedValues);
    }

    /**
     * Validates that the specified items are not selected within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param soft           A boolean indicating whether the validation should be performed softly.
     *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValues The expected values that should not be selected.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateNotSelectedItems(final ListUIElement element, boolean soft, final String... expectedValues) {
        Allure.step("[UI - List] Validate that the specified items are not selected");
        return validateSelectedItems(element, false, soft, expectedValues);
    }


    private T validateSelectedItems(final ListUIElement element, boolean shouldBeSelected, boolean soft,
                                    final String... expectedValues) {
        element.before().accept(driver);
        List<String> selectedItems = itemListService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedItems);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> {
                        if (shouldBeSelected) {
                            softAssertions.assertThat(selectedItems).as("Validating Selected Items")
                                    .containsAll(Arrays.asList(expectedValues));
                        } else {
                            softAssertions.assertThat(selectedItems).as("Validating Selected Items")
                                    .doesNotContainAnyElementsOf(Arrays.asList(expectedValues));
                        }
                    }
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> {
                        if (shouldBeSelected) {
                            Assertions.assertThat(selectedItems).as("Validating Selected Items")
                                    .containsAll(Arrays.asList(expectedValues));
                        } else {
                            Assertions.assertThat(selectedItems).as("Validating Selected Items")
                                    .doesNotContainAnyElementsOf(Arrays.asList(expectedValues));
                        }
                    }
            );
        }
    }

    /**
     * Retrieves all available items in the list.
     *
     * @param element The list UI element.
     * @return The fluent UI service instance.
     */
    public T getAll(final ListUIElement element) {
        Allure.step("[UI - List] Retrieve all items from the list");

        element.before().accept(driver);
        List<String> allItems = itemListService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), allItems);
        return uiServiceFluent;
    }

    /**
     * Validates that all expected items are present within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param expectedValues The expected values that should be present in the list.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateAllItems(final ListUIElement element, final String... expectedValues) {
        Allure.step("[UI - List] Validate that all expected items are present in the list");
        return validateAllItems(element, false, expectedValues);
    }

    /**
     * Validates that all expected items are present within the given list UI element.
     *
     * @param element        The {@link ListUIElement} representing the list UI component.
     * @param soft           A boolean indicating whether the validation should be performed softly.
     *                       If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @param expectedValues The expected values that should be present in the list.
     * @return The fluent UI service instance, allowing for method chaining.
     */
    public T validateAllItems(final ListUIElement element, boolean soft, final String... expectedValues) {
        Allure.step("[UI - List] Validate that all expected items are present in the list");
        element.before().accept(driver);
        List<String> selectedItems = itemListService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedItems);

        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(selectedItems)
                            .as("Validating Items").containsAll(Arrays.asList(expectedValues))
            );
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(selectedItems)
                            .as("Validating Items").containsAll(Arrays.asList(expectedValues))
            );
        }
    }

    /**
     * Inserts values into the list.
     *
     * @param componentType The type of the list component.
     * @param locator       The locator of the list element.
     * @param values        The values to insert.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        Allure.step("[UI - List] Insert values into the list");
        itemListService.insertion(componentType, locator, values);
    }

}
