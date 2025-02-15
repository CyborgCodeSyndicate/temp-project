package com.example.project.ui.components.toggle;

import com.example.project.ui.types.ToggleFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.toggle.Toggle;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

import java.util.List;
import java.util.Objects;


@ImplementationOfType(ToggleFieldTypes.MD_TOGGLE)
public class ToggleMDImpl extends BaseComponent implements Toggle {

    private static final By TOGGLE_ELEMENT_SELECTOR = By.tagName("mat-slide-toggle");
    public static final By LABEL_LOCATOR = By.className("mat-slide-toggle-content");
    public static final String CHECKED_CLASS_INDICATOR = "mat-checked";
    public static final String DISABLED_CLASS_INDICATOR = "mat-disabled";


    public ToggleMDImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    public void activate(final SmartWebElement container, final String toggleText) {
        SmartWebElement toggle = findToggle(container, toggleText);
        switchToggle(toggle, true);
    }

    @Override
    public void activate(final String toggleText) {
        SmartWebElement toggle = findToggle(null, toggleText);
        switchToggle(toggle, true);
    }

    @Override
    public void activate(final By toggleLocator) {
        SmartWebElement toggle = driver.findSmartElement(toggleLocator);
        switchToggle(toggle, true);
    }

    @Override
    public void deactivate(final SmartWebElement container, final String toggleText) {
        SmartWebElement toggle = findToggle(container, toggleText);
        switchToggle(toggle, false);
    }

    @Override
    public void deactivate(final String toggleText) {
        SmartWebElement toggle = findToggle(null, toggleText);
        switchToggle(toggle, false);
    }

    @Override
    public void deactivate(final By toggleLocator) {
        SmartWebElement toggle = driver.findSmartElement(toggleLocator);
        switchToggle(toggle, false);
    }

    @Override
    public boolean isEnabled(final SmartWebElement container, final String toggleText) {
        SmartWebElement toggle = findToggle(container, toggleText);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isEnabled(final String toggleText) {
        SmartWebElement toggle = findToggle(null, toggleText);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isEnabled(final By toggleLocator) {
        SmartWebElement toggle = driver.findSmartElement(toggleLocator);
        return isElementEnabled(toggle);
    }

    @Override
    public boolean isActivated(final SmartWebElement container, final String toggleText) {
        SmartWebElement toggle = findToggle(container, toggleText);
        return isElementActivated(toggle);
    }

    @Override
    public boolean isActivated(final String toggleText) {
        SmartWebElement toggle = findToggle(null, toggleText);
        return isElementActivated(toggle);
    }

    @Override
    public boolean isActivated(final By toggleLocator) {
        SmartWebElement toggle = driver.findSmartElement(toggleLocator);
        return isElementActivated(toggle);
    }

    private SmartWebElement findToggle(SmartWebElement container, String label) {
        List<SmartWebElement> toggles = Objects.nonNull(container)
                ? container.findSmartElements(TOGGLE_ELEMENT_SELECTOR)
                : driver.findSmartElements(TOGGLE_ELEMENT_SELECTOR);

        SmartWebElement targetedToggle = null;

        if (Objects.nonNull(label)) {
            targetedToggle = toggles.stream().filter(
                            toggle -> getElementText(toggle)
                                    .equalsIgnoreCase(label.trim())).findFirst()
                    .orElseThrow(() -> new NotFoundException("Toggle with text: " + label + " can't be found"));
        }
        return targetedToggle;
    }

    private boolean isElementEnabled(SmartWebElement toggleElement) {
        return !Objects.requireNonNull(toggleElement.getAttribute("class")).contains(DISABLED_CLASS_INDICATOR);
    }

    private boolean isElementActivated(SmartWebElement toggleElement) {
        return Objects.requireNonNull(toggleElement.getAttribute("class")).contains(CHECKED_CLASS_INDICATOR);
    }

    private String getElementText(SmartWebElement toggleElement) {
        return toggleElement.findSmartElement(LABEL_LOCATOR).getText().trim();
    }

    private void switchToggle(SmartWebElement toggleElement, boolean activate) {
        String toggleLabel = getElementText(toggleElement);
        String toggleClass = toggleElement.getAttribute("class");

        if (activate && isElementEnabled(toggleElement) && !isElementActivated(toggleElement)) {
            toggleElement.click();
            //info("Activate toggle with label: " + toggleLabel);
            toggleElement.waitUntilAttributeValueIsChanged("class", toggleClass);

        }
        if (!activate && isElementEnabled(toggleElement) && isElementActivated(toggleElement)) {
            toggleElement.click();
            //info("Deactivate toggle with label: " + toggleLabel);
            toggleElement.waitUntilAttributeValueIsChanged("class", toggleClass);
        }
    }
}
