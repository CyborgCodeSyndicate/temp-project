package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.ListUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ListServiceFluent<T extends UIServiceFluent<?>> implements Insertion {

    private final ItemListService itemListService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public ListServiceFluent(T uiServiceFluent, Storage storage, ItemListService itemListService,
                             SmartWebDriver webDriver) {
        this.itemListService = itemListService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T select(final ListUIElement element, final String... values) {
        Allure.step(String.format("Selecting items: '%s' from list component of type: '%s'.", Arrays.toString(values),
                element.componentType().toString()));
        element.before().accept(driver);
        itemListService.select(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T deSelect(final ListUIElement element, final String... values) {
        element.before().accept(driver);
        itemListService.deSelect(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T areSelected(final ListUIElement element, final String... values) {
        element.before().accept(driver);
        boolean selected = itemListService.areSelected(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public T validateAreSelected(final ListUIElement element, final String... values) {
        return validateAreSelected(element, true, false, values);
    }


    public T validateAreSelected(final ListUIElement element, boolean soft, final String... values) {
        return validateAreSelected(element, true, soft, values);
    }


    public T validateAreNotSelected(final ListUIElement element, final String... values) {
        return validateAreSelected(element, false, false, values);
    }


    public T validateAreNotSelected(final ListUIElement element, boolean soft, final String... values) {
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


    public T isSelected(final ListUIElement element, final String value) {
        element.before().accept(driver);
        boolean selected = itemListService.isSelected(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public T validateIsSelected(final ListUIElement element, final String value) {
        return validateAreSelected(element, true, false, value);
    }


    public T validateIsSelected(final ListUIElement element, boolean soft, final String value) {
        return validateAreSelected(element, true, soft, value);
    }


    public T validateIsNotSelected(final ListUIElement element, final String value) {
        return validateAreSelected(element, false, false, value);
    }


    public T validateIsNotSelected(final ListUIElement element, boolean soft, final String value) {
        return validateAreSelected(element, false, soft, value);
    }


    public T areEnabled(final ListUIElement element, final String... values) {
        element.before().accept(driver);
        boolean enabled = itemListService.areEnabled(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateAreEnabled(final ListUIElement element, final String... values) {
        return validateAreEnabled(element, true, false, values);
    }


    public T validateAreEnabled(final ListUIElement element, boolean soft, final String... values) {
        return validateAreEnabled(element, true, soft, values);
    }


    public T validateAreDisabled(final ListUIElement element, final String... values) {
        return validateAreEnabled(element, false, false, values);
    }


    public T validateAreDisabled(final ListUIElement element, boolean soft, final String... values) {
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


    public T isEnabled(final ListUIElement element, final String value) {
        element.before().accept(driver);
        boolean enabled = itemListService.isEnabled(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public T validateIsEnabled(final ListUIElement element, final String value) {
        return validateAreEnabled(element, true, false, value);
    }


    public T validateIsEnabled(final ListUIElement element, boolean soft, final String value) {
        return validateAreEnabled(element, true, soft, value);
    }


    public T validateIsDisabled(final ListUIElement element, final String value) {
        return validateAreEnabled(element, false, false, value);
    }


    public T validateIsDisabled(final ListUIElement element, boolean soft, final String value) {
        return validateAreEnabled(element, false, soft, value);
    }


    public T areVisible(final ListUIElement element, final String... values) {
        element.before().accept(driver);
        boolean visible = itemListService.areVisible(element.componentType(), element.locator(), values);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public T validateAreVisible(final ListUIElement element, final String... values) {
        return validateAreVisible(element, true, false, values);
    }


    public T validateAreVisible(final ListUIElement element, boolean soft, final String... values) {
        return validateAreVisible(element, true, soft, values);
    }


    public T validateAreHidden(final ListUIElement element, final String... values) {
        return validateAreVisible(element, false, false, values);
    }


    public T validateAreHidden(final ListUIElement element, boolean soft, final String... values) {
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


    public T isVisible(final ListUIElement element, final String value) {
        element.before().accept(driver);
        boolean visible = itemListService.isVisible(element.componentType(), element.locator(), value);
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public T validateIsVisible(final ListUIElement element, final String value) {
        return validateAreVisible(element, true, false, value);
    }


    public T validateIsVisible(final ListUIElement element, boolean soft, final String value) {
        return validateAreVisible(element, true, soft, value);
    }


    public T validateIsHidden(final ListUIElement element, final String value) {
        return validateAreVisible(element, false, false, value);
    }


    public T validateIsHidden(final ListUIElement element, boolean soft, final String value) {
        return validateAreVisible(element, false, soft, value);
    }


    public T getSelected(final ListUIElement element) {
        element.before().accept(driver);
        List<String> selectedItems = itemListService.getSelected(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), selectedItems);
        return uiServiceFluent;
    }


    public T validateSelectedItems(final ListUIElement element, final String... expectedValues) {
        return validateSelectedItems(element, true, false, expectedValues);
    }


    public T validateSelectedItems(final ListUIElement element, boolean soft, final String... expectedValues) {
        return validateSelectedItems(element, true, soft, expectedValues);
    }


    public T validateNotSelectedItems(final ListUIElement element, final String... expectedValues) {
        return validateSelectedItems(element, false, false, expectedValues);
    }


    public T validateNotSelectedItems(final ListUIElement element, boolean soft, final String... expectedValues) {
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


    public T getAll(final ListUIElement element) {
        element.before().accept(driver);
        List<String> allItems = itemListService.getAll(element.componentType(), element.locator());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), allItems);
        return uiServiceFluent;
    }


    public T validateAllItems(final ListUIElement element, final String... expectedValues) {
        return validateAllItems(element, false, expectedValues);
    }


    public T validateAllItems(final ListUIElement element, boolean soft, final String... expectedValues) {
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


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        itemListService.insertion(componentType, locator, values);
    }

}
