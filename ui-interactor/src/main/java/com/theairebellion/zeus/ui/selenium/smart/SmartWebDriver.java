package com.theairebellion.zeus.ui.selenium.smart;

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


}
