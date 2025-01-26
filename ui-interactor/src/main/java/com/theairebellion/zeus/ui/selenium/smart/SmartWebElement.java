package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebElementDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class SmartWebElement extends WebElementDecorator {

    public static final UIConfig UI_CONFIG = ConfigCache.getOrCreate(UIConfig.class);

    private final WebDriver driver;
    private final WebDriverWait wait;


    public SmartWebElement(WebElement original, WebDriver driver) {
        super(original);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(UI_CONFIG.waitDuration()));
    }


    public List<SmartWebElement> findSmartElements(By by) {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            return super.findElements(by).stream().map(
                    element -> new SmartWebElement(element, driver)).toList();
        }
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return super.findElements(by).stream().map(
                    element -> new SmartWebElement(element, driver)).toList();
        } catch (Exception e) {
            return handleException("findElements", e, new Object[]{by});
        }
    }


    @HandleUIException
    public SmartWebElement findSmartElement(By by) {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            return new SmartWebElement(super.findElement(by), driver);
        }
        try {
            waitWithoutFailure(ExpectedConditions.presenceOfElementLocated(by));
            WebElement element = super.findElement(by);
            return new SmartWebElement(element, driver);
        } catch (Exception e) {
            return handleException("findElement", e, new Object[]{by});
        }
    }

    @Override
    @HandleUIException
    public void click() {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            super.click();
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(this));
            super.click();
        } catch (Exception e) {
            handleException("click", e, new Object[0]);
        }
    }

    @Override
    public void clear() {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            super.clear();
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(this));
            super.clear();
        } catch (Exception e) {
            handleException("clear", e, new Object[0]);
        }
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (!UI_CONFIG.useWrappedSeleniumFunctions()) {
            super.sendKeys(keysToSend);
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(this));
            super.sendKeys(keysToSend);
        } catch (Exception e) {
            handleException("clear", e, keysToSend);
        }
    }

    public void clearAndSendKeys(CharSequence... keysToSend) {
        clear();
        sendKeys(keysToSend);
    }


    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T> T handleException(String methodName, Exception exception, Object[] params) {
        Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

        Optional<ExceptionHandlingWebElement> exceptionHandlingOptional =
                Arrays.stream(ExceptionHandlingWebElement.values())
                        .filter(enumVal ->
                                enumVal.getMethodName().equals(methodName)
                                        && Objects.nonNull(enumVal.getExceptionHandlingMap().get(cause.getClass()))
                        )
                        .findFirst();

        if (exceptionHandlingOptional.isPresent()) {
            return (T) exceptionHandlingOptional.get()
                    .getExceptionHandlingMap()
                    .get(cause.getClass())
                    .apply(driver, this, params);
        } else {
            LogUI.error("No exception handling for this specific exception.");
            throw exception;
        }
    }

    @Override
    public String toString() {
        return original.toString();
    }


    private <T> void waitWithoutFailure(Function<WebDriver, T> expectedConditions) {
        try {
            wait.until(expectedConditions);
        } catch (Exception ignore) {
        }
    }
}
