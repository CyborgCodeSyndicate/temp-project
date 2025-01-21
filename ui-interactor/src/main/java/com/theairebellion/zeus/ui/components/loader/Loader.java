package com.theairebellion.zeus.ui.components.loader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Loader {

    boolean isPresent(WebElement container);

    boolean isPresent(By loaderLocator);

    void waitToBeShown(WebElement container, int secondsShown);

    void waitToBeShown(int secondsShown);

    void waitToBeShown(By loaderLocator, int secondsShown);

    void waitToBeRemoved(WebElement container, int secondsRemoved);

    void waitToBeRemoved(int secondsRemoved);

    void waitToBeRemoved(By loaderLocator, int secondsRemoved);
}
