package com.example.project.ui.components.loader;

import com.example.project.ui.types.LoaderFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.components.loader.Loader;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@ImplementationOfType(LoaderFieldTypes.MD_LOADER)
public class LoaderMDImpl extends BaseComponent implements Loader {

    private static final By LOADER_LOCATOR = By.tagName("mat-spinner");

    public LoaderMDImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }

    @Override
    public boolean isVisible(WebElement container) {
        return smartSelenium.checkNoException(() -> smartSelenium.waitAndFindElement(container, LOADER_LOCATOR));
    }

    @Override
    public boolean isVisible(By loaderLocator) {
        return smartSelenium.checkNoException(() -> smartSelenium.waitAndFindElement(LOADER_LOCATOR));
    }

    @Override
    public void waitToBeShown(WebElement container, int secondsShown) {
        WebElement loader = smartSelenium.waitAndFindElement(container, LOADER_LOCATOR);
        waitLoaderToBeShown(loader, secondsShown);
    }

    @Override
    public void waitToBeShown(int secondsShown) {
        waitLoaderToBeShown(LOADER_LOCATOR, secondsShown);
    }

    @Override
    public void waitToBeShown(By loaderLocator, int secondsShown) {
        waitLoaderToBeShown(loaderLocator, secondsShown);
    }

    @Override
    public void waitToBeRemoved(WebElement container, int secondsRemoved) {
        WebElement loader = smartSelenium.waitAndFindElement(container, LOADER_LOCATOR);
        waitLoaderToBeRemoved(loader, secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(int secondsRemoved) {
        waitLoaderToBeRemoved(LOADER_LOCATOR, secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(By loaderLocator, int secondsRemoved) {
        waitLoaderToBeRemoved(loaderLocator, secondsRemoved);
    }

    public void waitLoaderToBeShown(By loaderLocator, int secondsShown) {
        smartSelenium.waitUntilElementIsShown(loaderLocator, secondsShown);
    }

    public void waitLoaderToBeShown(WebElement loader, int secondsShown) {
        smartSelenium.waitUntilElementIsShown(loader, secondsShown);
    }

    public void waitLoaderToBeRemoved(By loaderLocator, int secondsRemoved) {
        smartSelenium.waitUntilElementIsShown(loaderLocator, secondsRemoved);
    }

    public void waitLoaderToBeRemoved(WebElement loader, int secondsRemoved) {
        smartSelenium.waitUntilElementIsShown(loader, secondsRemoved);
    }
}
