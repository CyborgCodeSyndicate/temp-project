package com.bakery.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public interface ContextConsumer extends Consumer<SmartWebDriver> {
    Consumer<SmartWebDriver> asConsumer(By locator);
}
