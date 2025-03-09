package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

public class LoaderServiceImpl extends AbstractComponentService<LoaderComponentType, Loader> implements LoaderService {

    public LoaderServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Loader createComponent(final LoaderComponentType componentType) {
        return ComponentFactory.getLoaderComponent(componentType, driver);
    }

    @Override
    public boolean isVisible(final LoaderComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Loader] Checking visibility of loader %s using container", componentType));
        LogUI.step("Checking visibility of loader " + componentType + " using container");
        return loaderComponent(componentType).isVisible(container);
    }

    @Override
    public boolean isVisible(final LoaderComponentType componentType, final By loaderLocator) {
        Allure.step(String.format("[UI - Loader] Checking visibility of loader %s using locator %s", componentType, loaderLocator));
        LogUI.step("Checking visibility of loader " + componentType + " using locator " + loaderLocator);
        return loaderComponent(componentType).isVisible(loaderLocator);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final SmartWebElement container,
                              final int secondsShown) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be shown using container for %d seconds", componentType, secondsShown));
        LogUI.step("Waiting for loader " + componentType + " to be shown using container for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(container, secondsShown);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final int secondsShown) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be shown for %d seconds", componentType, secondsShown));
        LogUI.step("Waiting for loader " + componentType + " to be shown for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(secondsShown);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final By loaderLocator, final int secondsShown) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be shown using locator %s for %d seconds", componentType, loaderLocator, secondsShown));
        LogUI.step("Waiting for loader " + componentType + " to be shown using locator " + loaderLocator + " for " + secondsShown + " seconds");
        loaderComponent(componentType).waitToBeShown(loaderLocator, secondsShown);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final SmartWebElement container,
                                final int secondsRemoved) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be removed using container for %d seconds", componentType, secondsRemoved));
        LogUI.step("Waiting for loader " + componentType + " to be removed using container for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(container, secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final int secondsRemoved) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be removed for %d seconds", componentType, secondsRemoved));
        LogUI.step("Waiting for loader " + componentType + " to be removed for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final By loaderLocator,
                                final int secondsRemoved) {
        Allure.step(String.format("[UI - Loader] Waiting for loader %s to be removed using locator %s for %d seconds", componentType, loaderLocator, secondsRemoved));
        LogUI.step("Waiting for loader " + componentType + " to be removed using locator " + loaderLocator + " for " + secondsRemoved + " seconds");
        loaderComponent(componentType).waitToBeRemoved(loaderLocator, secondsRemoved);
    }

    private Loader loaderComponent(final LoaderComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}