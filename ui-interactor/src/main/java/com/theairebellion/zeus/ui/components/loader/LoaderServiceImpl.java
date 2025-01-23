package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoaderServiceImpl implements LoaderService {

    protected SmartSelenium smartSelenium;
    private static Map<LoaderComponentType, Loader> components;

    public LoaderServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public LoaderServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public boolean isPresent(final LoaderComponentType componentType, final WebElement container) {
        return loaderComponent(componentType).isPresent(container);
    }

    @Override
    public boolean isPresent(final LoaderComponentType componentType, final By loaderLocator) {
        return loaderComponent(componentType).isPresent(loaderLocator);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final WebElement container,
                              final int secondsShown) {
        loaderComponent(componentType).waitToBeShown(container, secondsShown);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final int secondsShown) {
        loaderComponent(componentType).waitToBeShown(secondsShown);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final By loaderLocator, final int secondsShown) {
        loaderComponent(componentType).waitToBeShown(loaderLocator, secondsShown);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final WebElement container,
                                final int secondsRemoved) {
        loaderComponent(componentType).waitToBeShown(container, secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final int secondsRemoved) {
        loaderComponent(componentType).waitToBeShown(secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final By loaderLocator,
                                final int secondsRemoved) {
        loaderComponent(componentType).waitToBeShown(loaderLocator, secondsRemoved);
    }

    private Loader loaderComponent(final LoaderComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getLoaderComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
