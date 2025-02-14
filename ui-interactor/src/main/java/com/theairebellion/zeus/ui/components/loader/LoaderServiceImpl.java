package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

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
        return loaderComponent(componentType).isVisible(container);
    }

    @Override
    public boolean isVisible(final LoaderComponentType componentType, final By loaderLocator) {
        return loaderComponent(componentType).isVisible(loaderLocator);
    }

    @Override
    public void waitToBeShown(final LoaderComponentType componentType, final SmartWebElement container,
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
    public void waitToBeRemoved(final LoaderComponentType componentType, final SmartWebElement container,
                                final int secondsRemoved) {
        loaderComponent(componentType).waitToBeRemoved(container, secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final int secondsRemoved) {
        loaderComponent(componentType).waitToBeRemoved(secondsRemoved);
    }

    @Override
    public void waitToBeRemoved(final LoaderComponentType componentType, final By loaderLocator,
                                final int secondsRemoved) {
        loaderComponent(componentType).waitToBeRemoved(loaderLocator, secondsRemoved);
    }

    private Loader loaderComponent(final LoaderComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
