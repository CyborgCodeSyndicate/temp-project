package com.theairebellion.zeus.ui.util.strategy;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.util.List;
import java.util.Random;

public class StrategyGenerator {

    public static SmartWebElement getRandomElementFromElements(List<SmartWebElement> elements) {
        return elements.get(new Random().nextInt(elements.size()));
    }

    public static SmartWebElement getFirstElementFromElements(List<SmartWebElement> elements) {
        return elements.get(0);
    }

    public static SmartWebElement getLastElementFromElements(List<SmartWebElement> elements) {
        return elements.get(elements.size() - 1);
    }
}