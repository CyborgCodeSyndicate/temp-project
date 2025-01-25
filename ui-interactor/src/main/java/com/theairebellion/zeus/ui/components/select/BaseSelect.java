package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.ui.util.strategy.StrategyGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseSelect extends BaseComponent implements Select {

    private final List<DdlMode> supportedDDLModes;

    public BaseSelect(SmartSelenium smartSelenium, List<DdlMode> supportedDDLModes) {
        super(smartSelenium);
        this.supportedDDLModes = supportedDDLModes;
    }

    @Override
    public void selectItems(final DdlMode mode, final WebElement container, final String... values) {
        validateDDLMode(mode);
        switch (mode) {
            case SINGLE_SELECT_WITH_CHOOSING -> singleSelectWithChoosing(container, values[0]);
            case SINGLE_SELECT_WITH_SEARCH -> singleSelectWithSearch(container, values[0]);
            case MULTI_SELECT_WITH_CHOOSING -> multiSelectWithChoosing(container, values);
            case MULTI_SELECT_WITH_SEARCH -> multiSelectWithSearch(container, values);
        }
        waitForDropDownToBeClosed();
    }

    @Override
    public void selectItems(final DdlMode mode, final By containerLocator, final String... values) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        selectItems(mode, container, values);
    }

    @Override
    public List<String> selectItems(DdlMode mode, WebElement container, Strategy strategy) {
        validateDDLMode(mode);
        return selectItemsWithStrategy(container, strategy);
    }

    @Override
    public List<String> selectItems(DdlMode mode, final By containerLocator, Strategy strategy) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return selectItems(mode, container, strategy);
    }

    @Override
    public List<String> getAvailableItems(WebElement container, String search) {
        if (!supportedDDLModes.contains(DdlMode.SINGLE_SELECT_WITH_SEARCH) && !supportedDDLModes.contains(
                DdlMode.MULTI_SELECT_WITH_SEARCH)) {
            throw new IllegalArgumentException(
                    "This dropdown: " + this.getClass().getSimpleName() + " is not supporting search");
        }
        List<String> availableItemsWithSearch = getAvailableItemsWithSearch(container, search);
        waitForDropDownToBeClosed();
        return availableItemsWithSearch;
    }

    @Override
    public List<String> getAvailableItems(By containerLocator, String search) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAvailableItems(container, search);
    }

    @Override
    public List<String> getSelectedItems(WebElement container) {
        openDdl(container);
        List<String> checkedOptions = getAllOptionsElements().stream()
                .filter(this::checkIfOptionIsChecked)
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
        List<WebElement> options = getAllOptionsElements();
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
        List<WebElement> options = getAllOptionsElements();
        WebElement option = findOptionByText(options, value);
        return isOptionEnabled(option);
    }

    @Override
    public boolean isOptionEnabled(By containerLocator, String value) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return isOptionEnabled(container, value);
    }

    protected void singleSelectWithChoosing(final WebElement container, final String value) {
    }

    protected void singleSelectWithSearch(final WebElement container, final String searchValue) {
    }

    protected void multiSelectWithChoosing(final WebElement container, final String... values) {
    }

    protected void multiSelectWithSearch(WebElement container, String... searchValues) {
    }

    protected List<String> selectItemsWithStrategy(final WebElement container, final Strategy strategy) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements();
        List<String> selectedOptionsText = selectOptionByStrategy(options, strategy);
        closeDdl(container);
        return selectedOptionsText;
    }

    protected List<String> selectOptionByStrategy(List<WebElement> options, Strategy strategy) {
        List<WebElement> optionElements = getOptionsByStrategy(options, strategy);
        return optionElements.stream()
                .peek(this::selectIfNotChecked)
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

    protected void validateDDLMode(DdlMode ddlMode) {
        if (!supportedDDLModes.contains(ddlMode)) {
            throw new IllegalArgumentException("DDL mode: " + ddlMode + " is not supported for ddl: " + this.getClass()
                    .getSimpleName() + ". This ddl type supports: " + supportedDDLModes);
        }
    }

    protected WebElement findOptionByText(List<WebElement> options, String text, By locator) {
        return options.stream()
                .filter(option -> smartSelenium.smartGetText(smartSelenium.smartFindElement(option, locator)).trim()
                        .equals(text))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Option with text '" + text + "' not found"));
    }

    protected void selectIfNotChecked(WebElement option) {
        if (!checkIfOptionIsChecked(option)) {
            smartSelenium.waitAndClickElement(option);
        }
    }

    protected abstract void openDdl(WebElement container);

    protected abstract void closeDdl(WebElement container);

    protected abstract WebElement findDdlButton(WebElement ddlRoot);

    protected abstract List<WebElement> getAllOptionsElements();

    protected abstract WebElement findOptionByText(List<WebElement> options, String text);

    protected abstract List<String> getAvailableItemsWithSearch(final WebElement container, final String search);

    protected abstract List<WebElement> findOptionsWithSearch(WebElement ddlRoot, String value);

    protected abstract boolean isOptionEnabled(WebElement option);

    protected abstract boolean checkIfOptionIsChecked(WebElement option);

    protected abstract void waitForDropDownToBeClosed();
}