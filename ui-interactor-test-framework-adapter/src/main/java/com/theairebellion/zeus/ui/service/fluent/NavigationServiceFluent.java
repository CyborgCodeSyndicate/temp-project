package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;

import java.util.Objects;


public class NavigationServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final SmartWebDriver driver;


    public NavigationServiceFluent(T uiServiceFluent, SmartWebDriver webDriver) {
        this.uiServiceFluent = uiServiceFluent;
        this.driver = webDriver;
    }


    public T navigate(String url) {
        driver.manage().window().maximize();
        driver.get(url);
        return uiServiceFluent;
    }


    public T back() {
        driver.navigate().back();
        return uiServiceFluent;
    }


    public T forward() {
        driver.navigate().forward();
        return uiServiceFluent;
    }


    public T refresh() {
        driver.navigate().refresh();
        return uiServiceFluent;
    }


    public T switchToNewTab() {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return uiServiceFluent;
    }


    public T switchToWindow(String windowTitle) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (Objects.equals(driver.getTitle(), windowTitle)) {
                return uiServiceFluent;
            }
        }
        throw new NoSuchWindowException("No window found with title: " + windowTitle);
    }


    public T closeCurrentTab() {
        driver.close();
        if (!driver.getWindowHandles().isEmpty()) {
            driver.switchTo().window(driver.getWindowHandles().iterator().next());
        }
        return uiServiceFluent;
    }


    public T switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
        return uiServiceFluent;
    }


    public T switchToFrameByNameOrId(String nameOrId) {
        driver.switchTo().frame(nameOrId);
        return uiServiceFluent;
    }


    public T switchToParentFrame() {
        driver.switchTo().parentFrame();
        return uiServiceFluent;
    }


    public T switchToDefaultContent() {
        driver.switchTo().defaultContent();
        return uiServiceFluent;
    }


    public T acceptAlert() {
        driver.switchTo().alert().accept();
        return uiServiceFluent;
    }


    public T dismissAlert() {
        driver.switchTo().alert().dismiss();
        return uiServiceFluent;
    }


    private String getAlertText() {
        return driver.switchTo().alert().getText();
    }


    public T validateAlertText(String expected) {
        return validateAlertText(expected, false);
    }


    public T validateAlertText(String expected, boolean soft) {
        String alertText = getAlertText();
        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        }
    }


    public T openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open();");
        return switchToNewTab();
    }
}
