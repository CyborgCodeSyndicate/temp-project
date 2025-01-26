package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


public class SmartWebDriver extends WebDriverDecorator {

    public static final UIConfig UI_CONFIG = ConfigCache.getOrCreate(UIConfig.class);

    private final WebDriverWait wait;

    public SmartWebDriver(WebDriver original) {
        super(original);
        this.wait = new WebDriverWait(original, Duration.ofSeconds(UI_CONFIG.waitDuration()));
    }

    public SmartWebElement findSmartElement(By by) {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            WebElement element = original.findElement(by);
            return new SmartWebElement(element, original);
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = original.findElement(by);
        return new SmartWebElement(element, original);
    }

    public List<SmartWebElement> findSmartElements(By by) {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            return original.findElements(by).stream()
                    .map(element -> new SmartWebElement(element, original))
                    .collect(Collectors.toList());
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return original.findElements(by).stream()
                .map(element -> new SmartWebElement(element, original))
                .collect(Collectors.toList());
    }
}
