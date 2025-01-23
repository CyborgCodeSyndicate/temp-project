package com.example.project.ui.components.button;

import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


@ImplementationOfType(ButtonFieldTypes.MD_BUTTON)
public class ButtonMDImpl extends BaseComponent implements Button {

    private static final By BUTTON_CLASS_NAME_SELECTOR = By.className("mat-button-base");
    private static final String DISABLED_STATE = "mat-button-disabled";


    public ButtonMDImpl(SmartSelenium smartSelenium) {
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
    public boolean isPresent(WebElement container, String buttonText) {
        WebElement button = findButtonInContainer(container, buttonText);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isPresent(WebElement container) {
        WebElement button = findButtonInContainer(container, null);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isPresent(String buttonText) {
        WebElement button = findButtonByText(buttonText);
        return isButtonEnabled(button);
    }


    @Override
    public boolean isPresent(By buttonLocator) {
        WebElement button = smartSelenium.waitAndFindElement(buttonLocator);
        return isButtonEnabled(button);
    }


    private WebElement findButtonInContainer(WebElement container, String buttonText) {
        return smartSelenium.waitAndFindElements(container, BUTTON_CLASS_NAME_SELECTOR).stream()
                .filter(element -> buttonText == null || smartSelenium.smartGetText(element).contains(buttonText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
    }


    private WebElement findButtonByText(String buttonText) {
        return smartSelenium.waitAndFindElements(BUTTON_CLASS_NAME_SELECTOR).stream()
                .filter(element -> smartSelenium.smartGetText(element).contains(buttonText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Button with text %s not found", buttonText)));
    }


    private boolean isButtonEnabled(WebElement button) {
        return !smartSelenium.smartGetAttribute(button, "class").contains(DISABLED_STATE);
    }

}