package com.theairebellion.zeus.ui.util.strategy;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class StrategyGenerator {

    public static WebElement getRandomElementFromElements(List<WebElement> elements) {
        return elements.get(new Random().nextInt(elements.size()));
    }

    public static WebElement getFirstElementFromElements(List<WebElement> elements) {
        return elements.get(0);
    }

    public static WebElement getLastElementFromElements(List<WebElement> elements) {
        return elements.get(elements.size() - 1);
    }
}