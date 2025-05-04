package com.theairebellion.zeus.ui.components.loader.mock;

import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockLoaderService implements LoaderService {

    public LoaderComponentType lastComponentTypeUsed;
    public LoaderComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public By lastLocator;
    public int lastSeconds;
    public boolean returnVisible = false;

    public MockLoaderService(){
        reset();
    }

    private void setLastType(LoaderComponentType type) {
        this.explicitComponentType = type;
        if (MockLoaderComponentType.DUMMY_LOADER.equals(type)) {
            this.lastComponentTypeUsed = MockLoaderComponentType.DUMMY_LOADER;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockLoaderComponentType.DUMMY_LOADER;
        lastContainer = null;
        lastLocator = null;
        lastSeconds = 0;
        returnVisible = false;
    }

    @Override
    public boolean isVisible(LoaderComponentType componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        return returnVisible;
    }

    @Override
    public boolean isVisible(LoaderComponentType componentType, By loaderLocator) {
        setLastType(componentType);
        lastLocator = loaderLocator;
        return returnVisible;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, SmartWebElement container, int secondsShown) {
        setLastType(componentType);
        lastContainer = container;
        lastSeconds = secondsShown;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, int secondsShown) {
        setLastType(componentType);
        lastSeconds = secondsShown;
        lastContainer = null;
        lastLocator = null;
    }

    @Override
    public void waitToBeShown(LoaderComponentType componentType, By loaderLocator, int secondsShown) {
        setLastType(componentType);
        lastLocator = loaderLocator;
        lastSeconds = secondsShown;
        lastContainer = null;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, SmartWebElement container, int secondsRemoved) {
        setLastType(componentType);
        lastContainer = container;
        lastSeconds = secondsRemoved;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, int secondsRemoved) {
        setLastType(componentType);
        lastSeconds = secondsRemoved;
        lastContainer = null;
        lastLocator = null;
    }

    @Override
    public void waitToBeRemoved(LoaderComponentType componentType, By loaderLocator, int secondsRemoved) {
        setLastType(componentType);
        lastLocator = loaderLocator;
        lastSeconds = secondsRemoved;
        lastContainer = null;
    }
}