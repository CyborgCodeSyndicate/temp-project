package com.example.project.ui.components.toggle;

import com.example.project.ui.types.ToggleFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.toggle.Toggle;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;


@ImplementationOfType(ToggleFieldTypes.MD_TOGGLE)
public class ToggleMDImpl extends BaseComponent implements Toggle {

    private static final By TOGGLE_ELEMENT_SELECTOR = By.tagName("mat-slide-toggle");
    public static final By LABEL_LOCATOR = By.className("mat-slide-toggle-content");
    public static final String CHECKED_CLASS_INDICATOR = "mat-checked";
    public static final String DISABLED_CLASS_INDICATOR = "mat-disabled";


    public ToggleMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public void activate(final WebElement container, final String toggleText) {
        WebElement toggle = findToggle(container, toggleText);
        switchToggle(toggle, true);
    }

    @Override
    public void activate(final String toggleText) {
        WebElement toggle = findToggle(null, toggleText);
        switchToggle(toggle, true);
    }

    @Override
    public void activate(final By toggleLocator) {
        WebElement toggle = smartSelenium.waitAndFindElement(toggleLocator);
        switchToggle(toggle, true);
    }

    @Override
    public void deactivate(final WebElement container, final String toggleText) {
        WebElement toggle = findToggle(container, toggleText);
        switchToggle(toggle, false);
    }

    @Override
    public void deactivate(final String toggleText) {
        WebElement toggle = findToggle(null, toggleText);
        switchToggle(toggle, false);
    }

    @Override
    public void deactivate(final By toggleLocator) {
        WebElement toggle = smartSelenium.waitAndFindElement(toggleLocator);
        switchToggle(toggle, false);
    }

    @Override
    public boolean isEnabled(final WebElement container, final String toggleText) {
        WebElement toggle = findToggle(container, toggleText);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isEnabled(final String toggleText) {
        WebElement toggle = findToggle(null, toggleText);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isEnabled(final By toggleLocator) {
        WebElement toggle = smartSelenium.waitAndFindElement(toggleLocator);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isActivated(final WebElement container, final String toggleText) {
        WebElement toggle = findToggle(container, toggleText);
        return isElementActivated(toggle);
    }

    @Override
    public boolean isActivated(final String toggleText) {
        WebElement toggle = findToggle(null, toggleText);
        return isElementActivated(toggle);
    }

    @Override
    public boolean isActivated(final By toggleLocator) {
        WebElement toggle = smartSelenium.waitAndFindElement(toggleLocator);
        return isElementActivated(toggle);
    }

    private WebElement findToggle(WebElement container, String label) {
        List<WebElement> toggles = Objects.nonNull(container)
                ? smartSelenium.waitAndFindElements(container, TOGGLE_ELEMENT_SELECTOR)
                : smartSelenium.waitAndFindElements(TOGGLE_ELEMENT_SELECTOR);

        WebElement targetedToggle = null;

        if (Objects.nonNull(label)) {
            targetedToggle = toggles.stream().filter(
                            toggle -> getElementText(toggle)
                                    .equalsIgnoreCase(label.trim())).findFirst()
                    .orElseThrow(() -> new NotFoundException("Toggle with text: " + label + " can't be found"));
        }
        return targetedToggle;
    }

    private boolean isElementEnabled(WebElement toggleElement) {
        return !smartSelenium.smartGetAttribute(toggleElement, "class").contains(DISABLED_CLASS_INDICATOR);
    }

    private boolean isElementActivated(WebElement toggleElement) {
        return smartSelenium.smartGetAttribute(toggleElement, "class").contains(CHECKED_CLASS_INDICATOR);
    }

    private String getElementText(WebElement toggleElement) {
        return smartSelenium.smartGetText(smartSelenium.smartFindElement(toggleElement, LABEL_LOCATOR)).trim();
    }

    private void switchToggle(WebElement toggleElement, boolean activate) {
        String toggleLabel = getElementText(toggleElement);
        String toggleClass = smartSelenium.smartGetAttribute(toggleElement, "class");

        if (activate && isElementEnabled(toggleElement) && !isElementActivated(toggleElement)) {
            smartSelenium.smartClick(toggleElement);
            //info("Activate toggle with label: " + toggleLabel);
            smartSelenium.waitUntilAttributeValueIsChanged(toggleElement, "class", toggleClass);

        }
        if (!activate && isElementEnabled(toggleElement) && isElementActivated(toggleElement)) {
            smartSelenium.smartClick(toggleElement);
            //info("Deactivate toggle with label: " + toggleLabel);
            smartSelenium.waitUntilAttributeValueIsChanged(toggleElement, "class", toggleClass);
        }
    }
}
