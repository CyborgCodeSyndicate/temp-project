package com.example.project.ui.components.select;

import com.example.project.ui.types.SelectFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.select.Select;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ImplementationOfType(SelectFieldTypes.VA_SELECT)
public class SelectVAImpl extends BaseComponent implements Select {

    public static final By OPTIONS_CONTAINER_LOCATOR = By.cssSelector("vaadin-combo-box-overlay#overlay");
    public static final By OPTIONS_ROOT_LOCATOR = By.cssSelector("iron-list#selector vaadin-combo-box-item");
    public static final By OPEN_DDL_BUTTON_LOCATOR = By.id("pickupLocation");
    public static final By OPTION_LOCATOR = By.cssSelector("vaadin-combo-box-item");
    public static final By OPTION_TEXT_LOCATOR = By.cssSelector("div#content");


    public static final String DISABLED_CLASS_INDICATOR = "mat-radio-disabled"; //todo

    public SelectVAImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    public void selectOptions(final SmartWebElement container, final String... values) {
        openDdl(container);
        List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
        for (String value : values) {
            SmartWebElement option = findOptionByText(options, value);
            selectIfNotChecked(option);
        }
        closeDdl(container);
        waitForDropDownToBeClosed();
    }

    @Override
    public void selectOptions(final By containerLocator, final String... values) {
        selectOptions(driver.findSmartElement(containerLocator), values);
    }

    @Override
    public List<String> selectOptions(SmartWebElement container, Strategy strategy) {
        return selectItemsWithStrategy(container, strategy);
    }

    @Override
    public List<String> selectOptions(final By containerLocator, Strategy strategy) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return selectOptions(container, strategy);
    }

    @Override
    public List<String> getAvailableOptions(SmartWebElement container) {
        openDdl(container);
        List<SmartWebElement> options = getAllOptionsElements();
        List<String> availableOptions = options.stream()
                .map(option -> option.findSmartElement(OPTION_TEXT_LOCATOR).getText().trim())
                .collect(Collectors.toList());
        closeDdl(container);
        return availableOptions;
    }

    @Override
    public List<String> getAvailableOptions(By containerLocator) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return getAvailableOptions(container);
    }

    @Override
    public List<String> getSelectedOptions(SmartWebElement container) {
        openDdl(container);
        List<String> checkedOptions = getAllOptionsElements().stream()
                .filter(this::checkIfOptionIsSelected)
                .map(SmartWebElement::getText)
                .collect(Collectors.toList());
        closeDdl(container);
        return checkedOptions;
    }

    @Override
    public List<String> getSelectedOptions(By containerLocator) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return getSelectedOptions(container);
    }

    @Override
    public boolean isOptionVisible(SmartWebElement container, String value) {
        openDdl(container);
        List<SmartWebElement> options = getAllOptionsElements();
        try {
            findOptionByText(options, value);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isOptionVisible(By containerLocator, String value) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return isOptionVisible(container, value);
    }

    @Override
    public boolean isOptionEnabled(SmartWebElement container, String value) {
        openDdl(container);
        List<SmartWebElement> options = getAllOptionsElements();
        SmartWebElement option = findOptionByText(options, value);
        return isOptionEnabled(option);
    }

    @Override
    public boolean isOptionEnabled(By containerLocator, String value) {
        SmartWebElement container = driver.findSmartElement(containerLocator);
        return isOptionEnabled(container, value);
    }

    protected void openDdl(SmartWebElement ddlButton) {
        //SmartWebElement ddlButton = findDdlButton(container);
        if (!Objects.requireNonNull(ddlButton.getAttribute("class")).contains("opened")) {
            ddlButton.click();
        }
    }

    protected void closeDdl(SmartWebElement ddlButton) {
        //SmartWebElement ddlButton = findDdlButton(container);
        if (Objects.requireNonNull(ddlButton.getAttribute("class")).contains("opened")) {
            ddlButton.click();
        }
    }

    /*protected SmartWebElement findDdlButton(SmartWebElement ddlRoot) {
        return smartSelenium.smartFindElement(ddlRoot, OPEN_DDL_BUTTON_LOCATOR);
    }*/

    protected List<SmartWebElement> getAllOptionsElements() {
        /*SmartWebElement optionsContainer = smartSelenium.smartFindElement(OPTIONS_ROOT_LOCATOR);
        List<SmartWebElement> options = smartSelenium.smartFindElements(optionsContainer,
                OPTION_LOCATOR);*/
        List<SmartWebElement> options = driver.findSmartElements(OPTIONS_ROOT_LOCATOR);
        return options.stream()
                .filter(element -> !Objects.requireNonNull(element.getAttribute("class")).contains("hidden"))
                .collect(Collectors.toList());
    }

    protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text) {
        return findOptionByText(options, text, OPTION_TEXT_LOCATOR);
    }

    protected boolean checkIfOptionIsSelected(SmartWebElement option) {
        return Objects.requireNonNull(option.getAttribute("class")).contains("selected");
    }

    protected void waitForDropDownToBeClosed() {
        driver.waitUntilElementIsRemoved(OPTIONS_CONTAINER_LOCATOR, 3);
    }

    protected boolean isOptionEnabled(SmartWebElement option) {
        return !Objects.requireNonNull(option.getAttribute("class")).contains(DISABLED_CLASS_INDICATOR); //todo
    }


    protected List<String> selectItemsWithStrategy(final SmartWebElement container, final Strategy strategy) {
        openDdl(container);
        List<SmartWebElement> options = getAllOptionsElements();
        List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
        closeDdl(container);
        return selectedOptionsText;
    }

    protected List<String> selectOptionByStrategy(List<SmartWebElement> options, Strategy strategy) {
        List<SmartWebElement> optionElements = getOptionsByStrategy(options, strategy);
        return optionElements.stream()
                .peek(this::selectIfNotChecked)
                .map(SmartWebElement::getText)
                .collect(Collectors.toList());
    }

    protected List<SmartWebElement> getOptionsByStrategy(List<SmartWebElement> options, Strategy strategy) {
        return switch (strategy) {
            case FIRST -> List.of(StrategyGenerator.getFirstElementFromElements(options));
            case LAST -> List.of(StrategyGenerator.getLastElementFromElements(options));
            case RANDOM -> List.of(StrategyGenerator.getRandomElementFromElements(options));
            case ALL -> new ArrayList<>(options);
        };
    }

    protected SmartWebElement findOptionByText(List<SmartWebElement> options, String text, By locator) {
        return options.stream()
                .filter(option -> option.findSmartElement(locator).getText().trim()
                        .equals(text))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
    }

    protected void selectIfNotChecked(SmartWebElement option) {
        if (!checkIfOptionIsSelected(option)) {
            option.click();
        }
    }
}