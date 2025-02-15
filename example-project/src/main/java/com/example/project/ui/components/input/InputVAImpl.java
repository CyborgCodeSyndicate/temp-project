package com.example.project.ui.components.input;

import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

import java.util.List;
import java.util.Objects;


@ImplementationOfType(InputFieldTypes.Data.VA_INPUT)
public class InputVAImpl extends BaseComponent implements Input {

    private static final By INPUT_FIELD_CONTAINER = By.tagName("vaadin-text-field");
    private static final By INPUT_LOCATOR = By.tagName("input");

    private static final By INPUT_FIELD_ERROR_MESSAGE_LOCATOR = By.cssSelector("div[part='error-message']");
    private static final By INPUT_FIELD_LABEL_LOCATOR = By.cssSelector("div[part='label']");
    public static final String ELEMENT_VALUE_ATTRIBUTE = "value";
    public static final String FIELD_DISABLE_CLASS_INDICATOR = "disabled";


    public InputVAImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    public void insert(final SmartWebElement container, final String value) {
        SmartWebElement inputFieldContainer = findInputField(container, null);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final SmartWebElement container, final String inputFieldLabel, final String value) {
        SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final String inputFieldLabel, final String value) {
        SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final By inputFieldContainerLocator, final String value) {
        SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void clear(final SmartWebElement container) {
        SmartWebElement inputFieldContainer = findInputField(container, null);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final SmartWebElement container, final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final By inputFieldContainerLocator) {
        SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
        clearInputField(inputFieldContainer);
    }


    @Override
    public String getValue(final SmartWebElement container) {
        SmartWebElement inputFieldContainer = findInputField(container, null);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final SmartWebElement container, final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final By inputFieldContainerLocator) {
        SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final SmartWebElement container) {
        SmartWebElement inputFieldContainer = findInputField(container, null);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final SmartWebElement container, final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final By inputFieldContainerLocator) {
        SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final SmartWebElement container) {
        SmartWebElement inputFieldContainer = findInputField(container, null);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final SmartWebElement container, final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final String inputFieldLabel) {
        SmartWebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final By inputFieldContainerLocator) {
        SmartWebElement inputFieldContainer = driver.findSmartElement(inputFieldContainerLocator);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    private SmartWebElement findInputField(SmartWebElement container, String label) {
        List<SmartWebElement> fieldElements = Objects.nonNull(container)
                                             ? container.findSmartElements(INPUT_FIELD_CONTAINER)
                                             : driver.findSmartElements(INPUT_FIELD_CONTAINER);

        if (fieldElements.isEmpty()) {
            throw new NotFoundException("There is no input element");
        }

        return Objects.nonNull(label)
                   ? fieldElements.stream()
                         .filter(fieldElement -> label.trim().equalsIgnoreCase(getInputFieldLabel(fieldElement)))
                         .findFirst()
                         .orElseThrow(() -> new NotFoundException("There is no input element with label: " + label))
                   : fieldElements.get(0);
    }


    private void insertIntoInputField(SmartWebElement inputFieldContainer, String value) {
        if (isInputFieldEnabled(inputFieldContainer)) {
            SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
            inputElement.clearAndSendKeys(value);
            //inputElement.sendKeys(Keys.ENTER);
        }
    }


    private void clearInputField(SmartWebElement inputFieldContainer) {
        if (isInputFieldEnabled(inputFieldContainer)) {
            SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
            inputElement.clear();
            //inputElement.sendKeys(Keys.ENTER);
        }
    }


    private String getInputFieldValue(SmartWebElement inputFieldContainer) {
        SmartWebElement inputElement = inputFieldContainer.findSmartElement(INPUT_LOCATOR);
        return inputElement.getAttribute(ELEMENT_VALUE_ATTRIBUTE);
    }


    private String getInputFieldLabel(SmartWebElement fieldElement) {
        SmartWebElement labelElement = fieldElement.findSmartElement(INPUT_FIELD_LABEL_LOCATOR);
        return labelElement.getText().trim();
    }


    private String getInputFieldErrorMessage(SmartWebElement fieldElement) {
        SmartWebElement errorMessageElement = fieldElement.findSmartElement(INPUT_FIELD_ERROR_MESSAGE_LOCATOR);
        return errorMessageElement.getText().trim();
    }


    private boolean isInputFieldEnabled(SmartWebElement fieldElement) {
        String classAttribute = fieldElement.getAttribute(FIELD_DISABLE_CLASS_INDICATOR);
        return classAttribute == null;
    }

}
