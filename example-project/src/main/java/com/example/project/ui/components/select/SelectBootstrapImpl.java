package com.example.project.ui.components.select;

import com.example.project.ui.types.SelectFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.select.Select;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ImplementationOfType(SelectFieldTypes.BOOTSTRAP_SELECT)
public class SelectBootstrapImpl extends BaseComponent implements Select {

    public static final By OPTION_LOCATOR = By.tagName("option");
    public static final String DISABLED_CLASS_INDICATOR = "disabled";
    public static final String SELECTED_ATTRIBUTE_INDICATOR = "selected";
    public static final String HIDDEN_ATTRIBUTE_INDICATOR = "hidden";

    public SelectBootstrapImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public void selectItems(final WebElement container, final String... values) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements(container);
        for (String value : values) {
            WebElement option = findOptionByText(options, value);
            smartSelenium.smartClick(option);
        }
        closeDdl(container);
    }

    @Override
    public void selectItems(final By containerLocator, final String... values) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        selectItems(container, values);
    }

    @Override
    public List<String> selectItems(WebElement container, Strategy strategy) {
        return selectItemsWithStrategy(container, strategy);
    }

    @Override
    public List<String> selectItems(final By containerLocator, Strategy strategy) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return selectItems(container, strategy);
    }

    @Override
    public List<String> getAvailableItems(WebElement container) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements(container);
        List<String> availableOptions = options.stream()
                .map(option -> smartSelenium.smartGetText(option).trim())
                .collect(Collectors.toList());
        closeDdl(container);
        return availableOptions;
    }

    @Override
    public List<String> getAvailableItems(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAvailableItems(container);
    }

    @Override
    public List<String> getSelectedItems(WebElement container) {
        openDdl(container);
        List<String> checkedOptions = getAllOptionsElements(container).stream()
                .filter(this::checkIfOptionIsSelected)
                .map(smartSelenium::smartGetText)
                .collect(Collectors.toList());
        closeDdl(container);
        return checkedOptions;
    }

    @Override
    public List<String> getSelectedItems(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getSelectedItems(container);
    }

    @Override
    public boolean isOptionVisible(WebElement container, String value) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements(container);
        try {
            findOptionByText(options, value);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isOptionVisible(By containerLocator, String value) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return isOptionVisible(container, value);
    }

    @Override
    public boolean isOptionEnabled(WebElement container, String value) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements(container);
        WebElement option = findOptionByText(options, value);
        return isOptionEnabled(option);
    }

    @Override
    public boolean isOptionEnabled(By containerLocator, String value) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return isOptionEnabled(container, value);
    }

    protected List<String> selectItemsWithStrategy(final WebElement container, final Strategy strategy) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements(container);
        List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
        closeDdl(container);
        return selectedOptionsText;
    }

    protected List<String> selectOptionByStrategy(List<WebElement> options, Strategy strategy) {
        List<WebElement> optionElements = getOptionsByStrategy(options, strategy);
        return optionElements.stream()
                .peek(smartSelenium::smartClick)
                .map(smartSelenium::smartGetText)
                .collect(Collectors.toList());
    }

    protected List<WebElement> getOptionsByStrategy(List<WebElement> options, Strategy strategy) {
        return switch (strategy) {
            case FIRST -> List.of(StrategyGenerator.getFirstElementFromElements(options));
            case LAST -> List.of(StrategyGenerator.getLastElementFromElements(options));
            case RANDOM -> List.of(StrategyGenerator.getRandomElementFromElements(options));
            case ALL -> new ArrayList<>(options);
        };
    }

    protected WebElement findOptionByText(List<WebElement> options, String text) {
        return options.stream()
                .filter(option -> smartSelenium.smartGetText(option).trim()
                        .equals(text))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
    }

    protected void openDdl(WebElement container) {
        smartSelenium.waitAndClickElement(container);
    }

    protected void closeDdl(WebElement container) {
        openDdl(container);
    }

    protected List<WebElement> getAllOptionsElements(WebElement container) {
        List<WebElement> options = smartSelenium.smartFindElements(container, OPTION_LOCATOR);
        return options.stream()
                .filter(option -> !checkIfOptionIsHidden(option))
                .collect(Collectors.toList());
    }

    protected boolean isOptionEnabled(WebElement option) {
        return smartSelenium.smartGetAttribute(option, DISABLED_CLASS_INDICATOR) != null;
    }

    protected boolean checkIfOptionIsSelected(WebElement option) {
        return smartSelenium.smartGetAttribute(option, SELECTED_ATTRIBUTE_INDICATOR) != null;
    }

    protected boolean checkIfOptionIsHidden(WebElement option) {
        return smartSelenium.smartGetAttribute(option, HIDDEN_ATTRIBUTE_INDICATOR) != null;
    }
}