package com.example.project.ui.components.select;

import com.example.project.ui.types.SelectFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.select.BaseSelect;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.components.select.DdlMode.*;

@ImplementationOfType(SelectFieldTypes.MD_SELECT)
public class SelectMDImpl extends BaseSelect {

    public static final By OPTIONS_CONTAINER_LOCATOR = By.cssSelector("div.select-menu:not([style*='display: none'])");
    public static final By OPEN_DDL_BUTTON_LOCATOR = By.className("select-input-suffix-button");
    public static final By OPTION_TEXT_LOCATOR = By.tagName("button");
    public static final By SEARCH_INPUT_LOCATOR = By.tagName("input");
    public static final By OPTIONS_ROOT_LOCATOR = By.tagName("mat-tree");
    public static final By OPTION_LOCATOR = By.tagName("mat-tree-node");
    public static final By CLEAR_BUTTON_LOCATOR = By.className("clear-btn");
    public static final String DISABLED_CLASS_INDICATOR = "mat-radio-disabled";

    public SelectMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium, List.of(SINGLE_SELECT_WITH_CHOOSING, SINGLE_SELECT_WITH_SEARCH,
                MULTI_SELECT_WITH_CHOOSING, MULTI_SELECT_WITH_SEARCH));
    }

    @Override
    public List<String> getAvailableItems(WebElement container) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements();
        List<String> availableOptions = options.stream()
                .map(option -> smartSelenium.smartGetText(
                        smartSelenium.smartFindElement(option, OPTION_TEXT_LOCATOR)).trim())
                .collect(Collectors.toList());
        closeDdl(container);
        return availableOptions;
    }

    @Override
    public List<String> getAvailableItems(By containerLocator) {
        WebElement container = smartSelenium.waitAndFindElement(containerLocator);
        return getAvailableItems(container);
    }

    protected void singleSelectWithChoosing(final WebElement container, final String value) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements();
        WebElement option = findOptionByText(options, value);
        selectIfNotChecked(option);
        closeDdl(container);
    }

    protected void singleSelectWithSearch(final WebElement container, final String searchValue) {
        List<WebElement> options = findOptionsWithSearch(container, searchValue);
        options.forEach(this::selectIfNotChecked);
        closeDdl(container);
    }

    protected void multiSelectWithChoosing(final WebElement container, final String... values) {
        openDdl(container);
        List<WebElement> options = getAllOptionsElements();
        for (String value : values) {
            WebElement option = findOptionByText(options, value);
            selectIfNotChecked(option);
        }
        closeDdl(container);
    }

    protected void multiSelectWithSearch(WebElement container, String... searchValues) {
        for (String searchValue : searchValues) {
            List<WebElement> options = findOptionsWithSearch(container, searchValue);
            options.forEach(this::selectIfNotChecked);
            clearSearchInput(container);
        }
        closeDdl(container);
    }

    protected void openDdl(WebElement container) {
        WebElement ddlButton = findDdlButton(container);
        if (!smartSelenium.smartGetAttribute(ddlButton, "class").contains("opened")) {
            smartSelenium.waitAndClickElement(ddlButton);
        }
    }

    protected void closeDdl(WebElement container) {
        WebElement ddlButton = findDdlButton(container);
        if (smartSelenium.smartGetAttribute(ddlButton, "class").contains("opened")) {
            smartSelenium.waitAndClickElement(ddlButton);
        }
    }

    protected WebElement findDdlButton(WebElement ddlRoot) {
        return smartSelenium.smartFindElement(ddlRoot, OPEN_DDL_BUTTON_LOCATOR);
    }

    protected List<WebElement> getAllOptionsElements() {
        WebElement optionsContainer = smartSelenium.smartFindElement(OPTIONS_ROOT_LOCATOR);
        List<WebElement> options = smartSelenium.smartFindElements(optionsContainer,
                OPTION_LOCATOR);
        return options.stream()
                .filter(element -> !smartSelenium.smartGetAttribute(element, "class").contains("hide"))
                .collect(Collectors.toList());
    }

    protected WebElement findOptionByText(List<WebElement> options, String text) {
        return findOptionByText(options, text, OPTION_TEXT_LOCATOR);
    }

    protected List<String> getAvailableItemsWithSearch(final WebElement container, final String search) {
        List<WebElement> options = findOptionsWithSearch(container, search);
        List<String> availableOptions = options.stream()
                .map(option -> smartSelenium.smartGetText(smartSelenium.smartFindElement(option, OPTION_TEXT_LOCATOR)))
                .collect(Collectors.toList());
        closeDdl(container);
        return availableOptions;
    }

    protected List<WebElement> findOptionsWithSearch(WebElement ddlRoot, String value) {
        WebElement searchInput = smartSelenium.smartFindElement(ddlRoot, SEARCH_INPUT_LOCATOR);
        smartSelenium.clearAndSendKeys(searchInput, value);
        return getAllOptionsElements();
    }

    protected boolean checkIfOptionIsChecked(WebElement option) {
        return smartSelenium.smartGetAttribute(option, "class").contains("node-checked");
    }

    protected void waitForDropDownToBeClosed() {
        smartSelenium.waitUntilElementIsRemoved(OPTIONS_CONTAINER_LOCATOR, 3);
    }

    private void clearSearchInput(WebElement container) {
        smartSelenium.waitAndClickElement(
                smartSelenium.smartFindElement(container, CLEAR_BUTTON_LOCATOR));
    }

    protected boolean isOptionEnabled(WebElement option) {
        return !smartSelenium.smartGetAttribute(option, "class").contains(DISABLED_CLASS_INDICATOR);
    }
}