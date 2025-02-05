package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;


@Getter
public class SmartWebDriver extends WebDriverDecorator {

    private final WebDriverWait wait;


    public SmartWebDriver(WebDriver original) {
        super(original);
        this.wait = new WebDriverWait(original, Duration.ofSeconds(getUiConfig().waitDuration()));
    }


    public SmartWebElement findSmartElement(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            WebElement element = original.findElement(by);
            return new SmartWebElement(element, original);
        }
        waitWithoutFailure(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = original.findElement(by);
        return new SmartWebElement(element, original);
    }


    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return original.findElements(by).stream()
                       .map(element -> new SmartWebElement(element, original))
                       .collect(Collectors.toList());
        }
        waitWithoutFailure(ExpectedConditions.presenceOfElementLocated(by));
        return original.findElements(by).stream()
                   .map(element -> new SmartWebElement(element, original))
                   .collect(Collectors.toList());
    }

    private <T> void waitWithoutFailure(Function<WebDriver, T> expectedConditions) {
        try {
            wait.until(expectedConditions);
        } catch (Exception ignore) {
        }
    }


    public boolean checkNoException(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void waitUntilElementIsShown(SmartWebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            LogUI.error("Element wasn't displayed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsShown(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            LogUI.error("Element wasn't displayed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsRemoved(SmartWebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            LogUI.error("Element wasn't removed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsRemoved(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            LogUI.error("Element wasn't removed after: " + seconds + " seconds");
        }
    }

}
