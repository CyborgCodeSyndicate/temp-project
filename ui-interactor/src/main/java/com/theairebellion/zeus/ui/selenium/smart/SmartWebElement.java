package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebElementDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public class SmartWebElement extends WebElementDecorator {


    private final WebDriver driver;
    private final WebDriverWait wait;


    public SmartWebElement(WebElement original, WebDriver driver) {
        super(original);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getUiConfig().waitDuration()));
    }


    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
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
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
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
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.click();
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            super.click();
        } catch (Exception e) {
            handleException("click", e, new Object[0]);
        }
    }

    @HandleUIException
    public void doubleClick() {
        Actions actions = new Actions(driver);
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            actions.doubleClick();
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            actions.doubleClick();
        } catch (Exception e) {
            handleException("doubleClick", e, new Object[0]);
        }
    }

    @Override
    public void clear() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.clear();
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            super.clear();
        } catch (Exception e) {
            handleException("clear", e, new Object[0]);
        }
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.sendKeys(keysToSend);
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
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

    public void waitUntilAttributeValueIsChanged(String attributeName, String initialAttributeValue) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            customWait.until(attributeValueChanged(attributeName, initialAttributeValue));
        } catch (Exception ignore) {
        }
    }


    private ExpectedCondition<Boolean> attributeValueChanged(final String attributeName, final String initialValue) {
        return driver -> {
            String currentValue = getAttribute(attributeName);
            return !initialValue.equals(currentValue);
        };
    }

}
