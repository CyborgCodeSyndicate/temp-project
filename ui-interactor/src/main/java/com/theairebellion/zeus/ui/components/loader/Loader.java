package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Loader {

    boolean isVisible(SmartWebElement container);

    boolean isVisible(By loaderLocator);

    void waitToBeShown(SmartWebElement container, int secondsShown);

    void waitToBeShown(int secondsShown);

    void waitToBeShown(By loaderLocator, int secondsShown);

    void waitToBeRemoved(SmartWebElement container, int secondsRemoved);

    void waitToBeRemoved(int secondsRemoved);

    void waitToBeRemoved(By loaderLocator, int secondsRemoved);
}
