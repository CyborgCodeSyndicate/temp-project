package com.example.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.function.Consumer;

public enum SharedUI implements Consumer<SmartWebDriver> {

    WAIT_FOR_LOADING(SharedUI::waitForLoading),
    WAIT_FOR_PRESENCE(SharedUI::waitForPresence);


    private final Consumer<SmartWebDriver> function;


    SharedUI(final Consumer<SmartWebDriver> function) {
        this.function = function;
    }


    @Override
    public void accept(final SmartWebDriver smartWebDriver) {
        function.accept(smartWebDriver);
    }


    public static void waitForLoading(SmartWebDriver smartWebDriver) {
        smartWebDriver.getWait().until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
    }

    public static void waitForPresence(SmartWebDriver smartWebDriver) {
        smartWebDriver.getWait().until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-dialog-overlay#overlay"))
        );
    }

}
