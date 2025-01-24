package com.example.project.ui.components.button;

import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


@ImplementationOfType(ButtonFieldTypes.BOOTSTRAP_INPUT)
public class ButtonBootstrapImpl extends BaseComponent implements Button {

    private static final By BUTTON_TAG_NAME_SELECTOR = By.tagName("button");
    private static final String DISABLED_STATE_INDICATOR = "disabled";
    public static final String NOT_VISIBLE_STATE_INDICATOR = "hidden";


    public ButtonBootstrapImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }


    @Override
    public void click(WebElement container, String buttonText) {
        WebElement button = findButtonInContainer(container, buttonText);
        smartSelenium.smartClick(button);
    }


    @Override
    public void click(WebElement container) {
        WebElement button = findButtonInContainer(container, null);
        smartSelenium.smartClick(button);
    }


    @Override
    public void click(String buttonText) {
        WebElement button = findButtonByText(buttonText);
        smartSelenium.smartClick(button);
    }


    @Override
    public void click(By buttonLocator) {
        WebElement button = smartSelenium.waitAndFindElement(buttonLocator);
        smartSelenium.smartClick(button);
    }


    @Override
    public boolean isEnabled(WebElement container, String buttonText) {
        WebElement button = findButtonInContainer(container, buttonText);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isEnabled(WebElement container) {
        WebElement button = findButtonInContainer(container, null);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isEnabled(String buttonText) {
        WebElement button = findButtonByText(buttonText);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isEnabled(By buttonLocator) {
        WebElement button = smartSelenium.waitAndFindElement(buttonLocator);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isVisible(WebElement container, String buttonText) {
        WebElement button = findButtonInContainer(container, buttonText);
        return isButtonVisible(button);
    }


    @Override
    public boolean isVisible(WebElement container) {
        WebElement button = findButtonInContainer(container, null);
        return isButtonVisible(button);
    }


    @Override
    public boolean isVisible(String buttonText) {
        WebElement button = findButtonByText(buttonText);
        return isButtonVisible(button);
    }


    @Override
    public boolean isVisible(By buttonLocator) {
        WebElement button = smartSelenium.waitAndFindElement(buttonLocator);
        return isButtonVisible(button);
    }


    private WebElement findButtonInContainer(WebElement container, String buttonText) {
        return smartSelenium.waitAndFindElements(container, BUTTON_TAG_NAME_SELECTOR).stream()
                .filter(element -> buttonText == null || smartSelenium.smartGetText(element).contains(buttonText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
    }


    private WebElement findButtonByText(String buttonText) {
        return smartSelenium.waitAndFindElements(BUTTON_TAG_NAME_SELECTOR).stream()
                .filter(element -> smartSelenium.smartGetText(element).contains(buttonText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
    }


    private boolean isButtonEnabled(WebElement button) {
        return !smartSelenium.smartGetAttribute(button, "class").contains(DISABLED_STATE_INDICATOR);
    }


    private boolean isButtonVisible(WebElement button) {
        return !smartSelenium.smartGetAttribute(button, "class").contains(NOT_VISIBLE_STATE_INDICATOR);
    }

}