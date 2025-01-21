package com.example.project.ui.components.radio;

import com.example.project.ui.types.RadioFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.radio.Radio;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ImplementationOfType(RadioFieldTypes.MD_RADIO)
public class RadioMDImpl extends BaseComponent implements Radio {

    private static final By RADIO_ELEMENT_SELECTOR = By.tagName("mat-radio-button");
    private static final By RADIO_ELEMENT_CONTENT_LOCATOR = By.className("mat-radio-label-content");
    public static final String CHECKED_CLASS_INDICATOR = "mat-radio-checked";
    public static final String DISABLED_CLASS_INDICATOR = "mat-radio-disabled";
    public static final String VISIBLE_CLASS_INDICATOR = "hidden";


    public RadioMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public void select(WebElement container, String radioButtonText) {
        WebElement radioButton = findRadioButton(container, radioButtonText, null);
        selectElement(radioButton);
    }

    @Override
    public String select(WebElement container, Strategy strategy) {
        WebElement radioButton = findRadioButton(container, null, strategy);
        return selectElement(radioButton);
    }

    @Override
    public void select(String radioButtonText) {
        WebElement radioButton = findRadioButton(null, radioButtonText, null);
        selectElement(radioButton);
    }

    @Override
    public void select(By radioButtonLocator) {
        WebElement radioButton = smartSelenium.waitAndFindElement(radioButtonLocator);
        selectElement(radioButton);
    }

    @Override
    public boolean isEnabled(WebElement container, String radioButtonText) {
        WebElement radioButton = findRadioButton(container, radioButtonText, null);
        return isElementEnabled(radioButton);
    }

    @Override
    public boolean isEnabled(String radioButtonText) {
        WebElement radioButton = findRadioButton(null, radioButtonText, null);
        return isElementEnabled(radioButton);
    }

    @Override
    public boolean isEnabled(By radioButtonLocator) {
        WebElement radioButton = smartSelenium.waitAndFindElement(radioButtonLocator);
        return isElementEnabled(radioButton);
    }

    @Override
    public boolean isSelected(WebElement container, String radioButtonText) {
        WebElement radioButton = findRadioButton(container, radioButtonText, null);
        return isElementSelected(radioButton);
    }

    @Override
    public boolean isSelected(String radioButtonText) {
        WebElement radioButton = findRadioButton(null, radioButtonText, null);
        return isElementSelected(radioButton);
    }

    @Override
    public boolean isSelected(By radioButtonLocator) {
        WebElement radioButton = smartSelenium.waitAndFindElement(radioButtonLocator);
        return isElementEnabled(radioButton);
    }

    @Override
    public boolean isVisible(WebElement container, String radioButtonText) {
        WebElement radioButton = findRadioButton(container, radioButtonText, null);
        return isElementVisible(radioButton);
    }

    @Override
    public boolean isVisible(String radioButtonText) {
        WebElement radioButton = findRadioButton(null, radioButtonText, null);
        return isElementVisible(radioButton);
    }

    @Override
    public boolean isVisible(By radioButtonLocator) {
        WebElement radioButton = smartSelenium.waitAndFindElement(radioButtonLocator);
        return isElementVisible(radioButton);
    }

    @Override
    public String getSelected(WebElement container) {
        WebElement selectedRadioElement = smartSelenium.waitAndFindElements(container, RADIO_ELEMENT_SELECTOR)
                .stream().filter(this::isElementSelected)
                .findFirst().orElseThrow(() -> new NoSuchElementException("There is not radio element in the container"));
        return getElementText(selectedRadioElement);
    }

    @Override
    public String getSelected(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getSelected(container);
    }

    @Override
    public List<String> getAll(WebElement container) {
        return smartSelenium.waitAndFindElements(container, RADIO_ELEMENT_SELECTOR)
                .stream().map(this::getElementText)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAll(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAll(container);
    }


    private WebElement findRadioButton(WebElement container, String value, Strategy strategy) {

        List<WebElement> radioButtons = Objects.nonNull(container)
                ? smartSelenium.waitAndFindElements(container, RADIO_ELEMENT_SELECTOR)
                : smartSelenium.waitAndFindElements(RADIO_ELEMENT_SELECTOR);

        WebElement targetedRadioButton = null;

        if (Objects.nonNull(value)) {
            targetedRadioButton = radioButtons.stream().filter(
                            radio -> getElementText(radio)
                                    .equalsIgnoreCase(value.trim())).findFirst()
                    .orElseThrow(() -> new NotFoundException("Element with text: " + value + " can't be found"));
        }
        if (Objects.nonNull(strategy)) {

            switch (strategy) {
                case RANDOM:
                    targetedRadioButton = StrategyGenerator.getRandomElementFromElements(radioButtons);
                    break;
                case FIRST:
                    targetedRadioButton = StrategyGenerator.getFirstElementFromElements(radioButtons);
                    break;
                case LAST:
                    targetedRadioButton = StrategyGenerator.getLastElementFromElements(radioButtons);
                    break;
                case ALL:
                    throw new IllegalArgumentException("Only single radio button can be selected");
            }
        }

        return targetedRadioButton;
    }

    private boolean isElementEnabled(WebElement radioButtonElement) {
        return !smartSelenium.smartGetAttribute(radioButtonElement, "class").contains(DISABLED_CLASS_INDICATOR);
    }


    private boolean isElementSelected(WebElement radioButtonElement) {
        return smartSelenium.smartGetAttribute(radioButtonElement, "class").contains(CHECKED_CLASS_INDICATOR);
    }


    private boolean isElementVisible(WebElement radioButtonElement) {
        return !smartSelenium.smartGetAttribute(radioButtonElement, "class").contains(VISIBLE_CLASS_INDICATOR);
    }


    private String getElementText(WebElement radioButtonElement) {
        return smartSelenium.smartGetText(
                smartSelenium.smartFindElement(radioButtonElement, RADIO_ELEMENT_CONTENT_LOCATOR)).trim();
    }

    private String selectElement(WebElement radioButtonElement) {
        String elementText = null;
        if (isElementSelected(radioButtonElement)) {
            elementText = getElementText(radioButtonElement);
        }
        if (isElementVisible(radioButtonElement) && isElementEnabled(radioButtonElement) &&
                !isElementSelected(radioButtonElement)) {
            elementText = getElementText(radioButtonElement);
            String radioButtonClass = smartSelenium.smartGetAttribute(radioButtonElement, "class");
            smartSelenium.smartClick(radioButtonElement);
            smartSelenium.waitUntilAttributeValueIsChanged(radioButtonElement, "class", radioButtonClass);
        }
        return elementText;
    }
}