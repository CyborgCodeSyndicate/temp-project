package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;

import java.util.Objects;


public class NavigationServiceFluent {

    private final UIServiceFluent uiServiceFluent;
    private final SmartWebDriver driver;


    public NavigationServiceFluent(UIServiceFluent uiServiceFluent, SmartWebDriver webDriver) {
        this.uiServiceFluent = uiServiceFluent;
        this.driver = webDriver;
    }


    public UIServiceFluent navigate(String url) {
        driver.manage().window().maximize();
        driver.get(url);
        return uiServiceFluent;
    }


    public UIServiceFluent back() {
        driver.navigate().back();
        return uiServiceFluent;
    }


    public UIServiceFluent forward() {
        driver.navigate().forward();
        return uiServiceFluent;
    }


    public UIServiceFluent refresh() {
        driver.navigate().refresh();
        return uiServiceFluent;
    }


    public UIServiceFluent switchToNewTab() {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return uiServiceFluent;
    }


    public UIServiceFluent switchToWindow(String windowTitle) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (Objects.equals(driver.getTitle(), windowTitle)) {
                return uiServiceFluent;
            }
        }
        throw new NoSuchWindowException("No window found with title: " + windowTitle);
    }


    public UIServiceFluent closeCurrentTab() {
        driver.close();
        if (!driver.getWindowHandles().isEmpty()) {
            driver.switchTo().window(driver.getWindowHandles().iterator().next());
        }
        return uiServiceFluent;
    }


    public UIServiceFluent switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
        return uiServiceFluent;
    }


    public UIServiceFluent switchToFrameByNameOrId(String nameOrId) {
        driver.switchTo().frame(nameOrId);
        return uiServiceFluent;
    }


    public UIServiceFluent switchToParentFrame() {
        driver.switchTo().parentFrame();
        return uiServiceFluent;
    }


    public UIServiceFluent switchToDefaultContent() {
        driver.switchTo().defaultContent();
        return uiServiceFluent;
    }


    public UIServiceFluent acceptAlert() {
        driver.switchTo().alert().accept();
        return uiServiceFluent;
    }


    public UIServiceFluent dismissAlert() {
        driver.switchTo().alert().dismiss();
        return uiServiceFluent;
    }


    private String getAlertText() {
        return driver.switchTo().alert().getText();
    }


    public UIServiceFluent validateAlertText(String expected) {
        return validateAlertText(expected, false);
    }


    public UIServiceFluent validateAlertText(String expected, boolean soft) {
        String alertText = getAlertText();
        if (soft) {
            return uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        } else {
            return uiServiceFluent.validate(
                    () -> Assertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        }
    }


    public UIServiceFluent openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open();");
        return switchToNewTab();
    }
}
