package com.bakery.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;
import org.openqa.selenium.By;

public interface ContextConsumer extends Consumer<SmartWebDriver> {
   Consumer<SmartWebDriver> asConsumer(By locator);
}
