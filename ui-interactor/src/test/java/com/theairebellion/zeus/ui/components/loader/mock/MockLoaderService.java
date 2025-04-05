package com.theairebellion.zeus.ui.components.loader.mock;

import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockLoaderService implements LoaderService {

    public LoaderComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public By lastLocator;
    public int lastSeconds;
    public boolean returnVisible = false;

    public void reset() {
        lastComponentType = null;
        lastContainer = null;
        lastLocator = null;
        lastSeconds = 0;
        returnVisible = false;
    }

    @Override
    public boolean isVisible(LoaderComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnVisible;
    }

    @Override
    public boolean isVisible(LoaderComponentType componentType, By loaderLocator) {
        lastComponentType = componentType;
        lastLocator = loaderLocator;
        return returnVisible;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, SmartWebElement container, int secondsShown) {
        lastComponentType = componentType;
        lastContainer = container;
        lastSeconds = secondsShown;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, int secondsShown) {
        lastComponentType = componentType;
        lastSeconds = secondsShown;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, By loaderLocator, int secondsShown) {
        lastComponentType = componentType;
        lastLocator = loaderLocator;
        lastSeconds = secondsShown;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, SmartWebElement container, int secondsRemoved) {
        lastComponentType = componentType;
        lastContainer = container;
        lastSeconds = secondsRemoved;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, int secondsRemoved) {
        lastComponentType = componentType;
        lastSeconds = secondsRemoved;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, By loaderLocator, int secondsRemoved) {
        lastComponentType = componentType;
        lastLocator = loaderLocator;
        lastSeconds = secondsRemoved;
    }
}