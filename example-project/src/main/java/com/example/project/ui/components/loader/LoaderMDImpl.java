package com.example.project.ui.components.loader;

import com.example.project.ui.types.LoaderFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.loader.Loader;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

@ImplementationOfType(LoaderFieldTypes.MD_LOADER)
public class LoaderMDImpl extends BaseComponent implements Loader {

    private static final By LOADER_LOCATOR = By.tagName("mat-spinner");


    public LoaderMDImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    public boolean isVisible(final SmartWebElement container) {
        return driver.checkNoException(() -> container.findSmartElement(LOADER_LOCATOR));
    }


    @Override
    public boolean isVisible(final By loaderLocator) {
        return driver.checkNoException(() -> driver.findSmartElement(LOADER_LOCATOR));
    }


    @Override
    public void waitToBeShown(final SmartWebElement container, final int secondsShown) {
        SmartWebElement loader = container.findSmartElement(LOADER_LOCATOR);
        waitLoaderToBeShown(loader, secondsShown);
    }


    @Override
    public void waitToBeShown(final int secondsShown) {
        waitLoaderToBeShown(LOADER_LOCATOR, secondsShown);
    }


    @Override
    public void waitToBeShown(final By loaderLocator, final int secondsShown) {
        waitLoaderToBeShown(loaderLocator, secondsShown);
    }


    @Override
    public void waitToBeRemoved(final SmartWebElement container, final int secondsRemoved) {
        SmartWebElement loader = container.findSmartElement(LOADER_LOCATOR);
        waitLoaderToBeRemoved(loader, secondsRemoved);
    }


    @Override
    public void waitToBeRemoved(final int secondsRemoved) {
        waitLoaderToBeRemoved(LOADER_LOCATOR, secondsRemoved);
    }


    @Override
    public void waitToBeRemoved(final By loaderLocator, final int secondsRemoved) {
        waitLoaderToBeRemoved(loaderLocator, secondsRemoved);
    }


    public void waitLoaderToBeShown(final By loaderLocator, final int secondsShown) {
        driver.waitUntilElementIsShown(loaderLocator, secondsShown);
    }


    public void waitLoaderToBeShown(final SmartWebElement loader, final int secondsShown) {
        driver.waitUntilElementIsShown(loader, secondsShown);
    }


    public void waitLoaderToBeRemoved(final By loaderLocator, final int secondsRemoved) {
        driver.waitUntilElementIsRemoved(loaderLocator, secondsRemoved);
    }


    public void waitLoaderToBeRemoved(final SmartWebElement loader, final int secondsRemoved) {
        driver.waitUntilElementIsRemoved(loader, secondsRemoved);
    }
}
