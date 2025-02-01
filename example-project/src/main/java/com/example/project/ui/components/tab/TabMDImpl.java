package com.example.project.ui.components.tab;

import com.example.project.ui.types.TabFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.tab.Tab;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


@ImplementationOfType(TabFieldTypes.MD_TAB)
public class TabMDImpl extends BaseComponent implements Tab {

    private static final By TAB_CLASS_NAME_SELECTOR = By.className("mat-tab-link");
    private static final String DISABLED_STATE = "mat-tab-disabled";
    private static final String SELECTED_STATE = "mat-tab-label-active";


    public TabMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }


    @Override
    public void click(WebElement container, String tabText) {
        WebElement tab = findTabInContainer(container, tabText);
        smartSelenium.smartClick(tab);
    }


    @Override
    public void click(WebElement container) {
        WebElement tab = findTabInContainer(container, null);
        smartSelenium.smartClick(tab);
    }


    @Override
    public void click(String tabText) {
        WebElement tab = findTabByText(tabText);
        smartSelenium.smartClick(tab);
    }


    @Override
    public void click(By tabLocator) {
        WebElement tab = smartSelenium.waitAndFindElement(tabLocator);
        smartSelenium.smartClick(tab);
    }


    @Override
    public boolean isEnabled(WebElement container, String tabText) {
        WebElement tab = findTabInContainer(container, tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(WebElement container) {
        WebElement tab = findTabInContainer(container, null);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(String tabText) {
        WebElement tab = findTabByText(tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(By tabLocator) {
        WebElement tab = smartSelenium.waitAndFindElement(tabLocator);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(WebElement container, String tabText) {
        WebElement tab = findTabInContainer(container, tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(WebElement container) {
        WebElement tab = findTabInContainer(container, null);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(String tabText) {
        WebElement tab = findTabByText(tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(By tabLocator) {
        WebElement tab = smartSelenium.waitAndFindElement(tabLocator);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isSelected(WebElement container, String tabText) {
        WebElement tab = findTabInContainer(container, tabText);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(WebElement container) {
        WebElement tab = findTabInContainer(container, null);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(String tabText) {
        WebElement tab = findTabByText(tabText);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(By tabLocator) {
        WebElement tab = smartSelenium.waitAndFindElement(tabLocator);
        return isTabSelected(tab);
    }


    private WebElement findTabInContainer(WebElement container, String tabText) {
        return smartSelenium.waitAndFindElements(container, TAB_CLASS_NAME_SELECTOR).stream()
                .filter(element -> tabText == null || smartSelenium.smartGetText(element).contains(tabText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Tab with text %s not found", tabText)));
    }


    private WebElement findTabByText(String tabText) {
        return smartSelenium.waitAndFindElements(TAB_CLASS_NAME_SELECTOR).stream()
                .filter(element -> smartSelenium.smartGetText(element).contains(tabText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Tab with text %s not found", tabText)));
    }


    private boolean isTabEnabled(WebElement tab) {
        return !smartSelenium.smartGetAttribute(tab, "class").contains(DISABLED_STATE);
    }


    private boolean isTabSelected(WebElement tab) {
        return !smartSelenium.smartGetAttribute(tab, "class").contains(SELECTED_STATE);
    }
}