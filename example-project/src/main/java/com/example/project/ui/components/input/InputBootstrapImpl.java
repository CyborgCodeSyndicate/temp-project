package com.example.project.ui.components.input;

import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.input.Input;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;


@ImplementationOfType(InputFieldTypes.BOOTSTRAP_INPUT)
public class InputBootstrapImpl extends BaseComponent implements Input {

    private static final By INPUT_FIELD_CONTAINER = By.tagName("form");
    private static final By INPUT_FIELD_ERROR_MESSAGE_LOCATOR = By.className("alert-error");
    private static final By INPUT_FIELD_LABEL_LOCATOR = By.tagName("../label");
    public static final String ELEMENT_VALUE_ATTRIBUTE = "value";
    public static final String FIELD_DISABLE_CLASS_INDICATOR = "disabled";


    public InputBootstrapImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }


    @Override
    public void insert(final WebElement container, final String value) {
        WebElement inputFieldContainer = findInputField(container, null);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final WebElement container, final String inputFieldLabel, final String value) {
        WebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final String inputFieldLabel, final String value) {
        WebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void insert(final By inputFieldContainerLocator, final String value) {
        WebElement inputFieldContainer = smartSelenium.waitAndFindElement(inputFieldContainerLocator);
        insertIntoInputField(inputFieldContainer, value);
    }


    @Override
    public void clear(final WebElement container) {
        WebElement inputFieldContainer = findInputField(container, null);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final WebElement container, final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        clearInputField(inputFieldContainer);
    }


    @Override
    public void clear(final By inputFieldContainerLocator) {
        WebElement inputFieldContainer = smartSelenium.waitAndFindElement(inputFieldContainerLocator);
        clearInputField(inputFieldContainer);
    }


    @Override
    public String getValue(final WebElement container) {
        WebElement inputFieldContainer = findInputField(container, null);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final WebElement container, final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public String getValue(final By inputFieldContainerLocator) {
        WebElement inputFieldContainer = smartSelenium.waitAndFindElement(inputFieldContainerLocator);
        return getInputFieldValue(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final WebElement container) {
        WebElement inputFieldContainer = findInputField(container, null);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final WebElement container, final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public boolean isEnabled(final By inputFieldContainerLocator) {
        WebElement inputFieldContainer = smartSelenium.waitAndFindElement(inputFieldContainerLocator);
        return isInputFieldEnabled(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final WebElement container) {
        WebElement inputFieldContainer = findInputField(container, null);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final WebElement container, final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(container, inputFieldLabel);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final String inputFieldLabel) {
        WebElement inputFieldContainer = findInputField(null, inputFieldLabel);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    @Override
    public String getErrorMessage(final By inputFieldContainerLocator) {
        WebElement inputFieldContainer = smartSelenium.waitAndFindElement(inputFieldContainerLocator);
        return getInputFieldErrorMessage(inputFieldContainer);
    }


    private WebElement findInputField(WebElement container, String label) {
        List<WebElement> fieldElements = Objects.nonNull(container)
                ? smartSelenium.waitAndFindElements(container, INPUT_FIELD_CONTAINER)
                : smartSelenium.waitAndFindElements(INPUT_FIELD_CONTAINER);

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


    private void insertIntoInputField(WebElement inputField, String value) {
        if (isInputFieldEnabled(inputField)) {
            smartSelenium.clearAndSendKeys(inputField, value);
        }
    }


    private void clearInputField(WebElement inputField) {
        if (isInputFieldEnabled(inputField)) {
            smartSelenium.smartClear(inputField);
        }
    }


    private String getInputFieldValue(WebElement inputField) {
        return smartSelenium.smartGetAttribute(inputField, ELEMENT_VALUE_ATTRIBUTE);
    }


    private String getInputFieldLabel(WebElement fieldElement) {
        WebElement labelElement = smartSelenium.waitAndFindElement(fieldElement, INPUT_FIELD_LABEL_LOCATOR);
        return smartSelenium.smartGetText(labelElement).trim();
    }


    private String getInputFieldErrorMessage(WebElement fieldElement) {
        WebElement errorMessageElement = smartSelenium.waitAndFindElement(fieldElement,
                INPUT_FIELD_ERROR_MESSAGE_LOCATOR);
        return smartSelenium.smartGetText(errorMessageElement).trim();
    }


    private boolean isInputFieldEnabled(WebElement fieldElement) {
        String classAttribute = smartSelenium.smartGetAttribute(fieldElement, FIELD_DISABLE_CLASS_INDICATOR);
        return classAttribute == null;
    }

}
