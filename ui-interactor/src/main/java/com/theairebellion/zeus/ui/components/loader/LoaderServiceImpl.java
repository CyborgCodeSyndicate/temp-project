package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

public class LoaderServiceImpl extends AbstractComponentService<LoaderComponentType, Loader> implements LoaderService {

    public LoaderServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Loader createComponent(final LoaderComponentType componentType) {
        return ComponentFactory.getLoaderComponent(componentType, driver);
    }

    @Step("Checking visibility of loader {componentType} using container")
    @Override
    public boolean isVisible(final LoaderComponentType componentType, final SmartWebElement container) {
        LogUI.step("Checking visibility of loader " + componentType + " using container");
        return loaderComponent(componentType).isVisible(container);
    }

    @Step("Checking visibility of loader {componentType} using locator {loaderLocator}")
    @Override
    public boolean isVisible(final LoaderComponentType componentType, final By loaderLocator) {
        LogUI.step("Checking visibility of loader " + componentType + " using locator " + loaderLocator);
        return loaderComponent(componentType).isVisible(loaderLocator);
    }

    @Step("Waiting for loader {componentType} to be shown using container for {secondsShown} seconds")
    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final SmartWebElement container,
                              final int secondsShown) {
        LogUI.step("Waiting for loader " + componentType + " to be shown using container for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(container, secondsShown);
    }

    @Step("Waiting for loader {componentType} to be shown for {secondsShown} seconds")
    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final int secondsShown) {
        LogUI.step("Waiting for loader " + componentType + " to be shown for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(secondsShown);
    }

    @Step("Waiting for loader {componentType} to be shown using locator {loaderLocator} for {secondsShown} seconds")
    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final By loaderLocator, final int secondsShown) {
        LogUI.step("Waiting for loader " + componentType + " to be shown using locator " + loaderLocator + " for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(loaderLocator, secondsShown);
    }

    @Step("Waiting for loader {componentType} to be removed using container for {secondsRemoved} seconds")
    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final SmartWebElement container,
                                final int secondsRemoved) {
        LogUI.step("Waiting for loader " + componentType + " to be removed using container for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(container, secondsRemoved);
    }

    @Step("Waiting for loader {componentType} to be removed for {secondsRemoved} seconds")
    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final int secondsRemoved) {
        LogUI.step("Waiting for loader " + componentType + " to be removed for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(secondsRemoved);
    }

    @Step("Waiting for loader {componentType} to be removed using locator {loaderLocator} for {secondsRemoved} seconds")
    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final By loaderLocator,
                                final int secondsRemoved) {
        LogUI.step("Waiting for loader " + componentType + " to be removed using locator " + loaderLocator + " for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(loaderLocator, secondsRemoved);
    }

    private Loader loaderComponent(final LoaderComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
