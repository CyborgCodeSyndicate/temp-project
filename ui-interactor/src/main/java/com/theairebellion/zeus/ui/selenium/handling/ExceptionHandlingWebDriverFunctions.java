package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExceptionHandlingWebDriverFunctions {

    public static Object handleNoSuchElement(WebDriver driver, WebElementAction webElementAction, Object... args) {
        if (args.length == 0 || !(args[0] instanceof By)) {
            LogUI.error("Invalid or missing locator argument for FIND_ELEMENT.");
            throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
        }

        WebElement foundElement = FrameHelper.findElementInIFrames(driver, (By) args[0]);
        if (foundElement != null) {
            return webElementAction.performActionWebDriver(driver, foundElement);
        }
        String errorMessage = String.format(
                "[BROKEN] WebElement action '%s' could not be executed - Element with locator '%s' not found.",
                webElementAction.getMethodName(), args[0]
        );

        Allure.step(errorMessage, Status.BROKEN);
        throw new NoSuchElementException("Element not found in the main DOM or any iframe.");
    }
}
