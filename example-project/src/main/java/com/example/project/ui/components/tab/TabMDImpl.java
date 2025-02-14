package com.example.project.ui.components.tab;

import com.example.project.ui.types.TabFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.tab.Tab;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.Objects;


@ImplementationOfType(TabFieldTypes.MD_TAB)
public class TabMDImpl extends BaseComponent implements Tab {

    private static final By TAB_CLASS_NAME_SELECTOR = By.className("mat-tab-link");
    private static final String DISABLED_STATE = "mat-tab-disabled";
    private static final String SELECTED_STATE = "mat-tab-label-active";


    public TabMDImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    public void click(final SmartWebElement container, final String tabText) {
        SmartWebElement tab = findTabInContainer(container, tabText);
        tab.click();
    }


    @Override
    public void click(final SmartWebElement container) {
        SmartWebElement tab = findTabInContainer(container, null);
        tab.click();
    }


    @Override
    public void click(final String tabText) {
        SmartWebElement tab = findTabByText(tabText);
        tab.click();
    }


    @Override
    public void click(final By tabLocator) {
        SmartWebElement tab = driver.findSmartElement(tabLocator);
        tab.click();
    }


    @Override
    public boolean isEnabled(final SmartWebElement container, final String tabText) {
        SmartWebElement tab = findTabInContainer(container, tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(final SmartWebElement container) {
        SmartWebElement tab = findTabInContainer(container, null);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(final String tabText) {
        SmartWebElement tab = findTabByText(tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isEnabled(final By tabLocator) {
        SmartWebElement tab = driver.findSmartElement(tabLocator);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(final SmartWebElement container, final String tabText) {
        SmartWebElement tab = findTabInContainer(container, tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(final SmartWebElement container) {
        SmartWebElement tab = findTabInContainer(container, null);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(final String tabText) {
        SmartWebElement tab = findTabByText(tabText);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isVisible(final By tabLocator) {
        SmartWebElement tab = driver.findSmartElement(tabLocator);
        return isTabEnabled(tab);
    }


    @Override
    public boolean isSelected(final SmartWebElement container, final String tabText) {
        SmartWebElement tab = findTabInContainer(container, tabText);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(final SmartWebElement container) {
        SmartWebElement tab = findTabInContainer(container, null);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(final String tabText) {
        SmartWebElement tab = findTabByText(tabText);
        return isTabSelected(tab);
    }


    @Override
    public boolean isSelected(final By tabLocator) {
        SmartWebElement tab = driver.findSmartElement(tabLocator);
        return isTabSelected(tab);
    }


    private SmartWebElement findTabInContainer(SmartWebElement container, String tabText) {
        return container.findSmartElements(TAB_CLASS_NAME_SELECTOR).stream()
                .filter(element -> tabText == null || element.getText().contains(tabText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Tab with text %s not found", tabText)));
    }


    private SmartWebElement findTabByText(String tabText) {
        return driver.findSmartElements(TAB_CLASS_NAME_SELECTOR).stream()
                .filter(element -> element.getText().contains(tabText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Tab with text %s not found", tabText)));
    }


    private boolean isTabEnabled(SmartWebElement tab) {
        return !Objects.requireNonNull(tab.getAttribute("class")).contains(DISABLED_STATE);
    }


    private boolean isTabSelected(SmartWebElement tab) {
        return !Objects.requireNonNull(tab.getAttribute("class")).contains(SELECTED_STATE);
    }
}