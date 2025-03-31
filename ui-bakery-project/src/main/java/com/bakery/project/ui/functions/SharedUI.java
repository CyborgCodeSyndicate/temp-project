package com.bakery.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum SharedUI implements ContextConsumer {
    WAIT_FOR_TIMEOUT((driver, by) -> SharedUIFunctions.waitForTimeout(driver)),
    WAIT_FOR_LOADING((driver, by) -> SharedUIFunctions.waitForLoading(driver)),
    WAIT_FOR_PRESENCE(SharedUIFunctions::waitForPresence),
    WAIT_TO_BE_CLICKABLE(SharedUIFunctions::waitToBeClickable),
    WAIT_TO_BE_REMOVED(SharedUIFunctions::waitToBeRemoved);

    private final BiConsumer<SmartWebDriver, By> function;

    SharedUI(BiConsumer<SmartWebDriver, By> function) {
        this.function = function;
    }

    @Override
    public Consumer<SmartWebDriver> asConsumer(By locator) {
        return driver -> function.accept(driver, locator);
    }

    @Override
    public void accept(SmartWebDriver driver) {
        accept(driver, null);
    }

    public void accept(SmartWebDriver driver, By locator) {
        function.accept(driver, locator);
    }
}
