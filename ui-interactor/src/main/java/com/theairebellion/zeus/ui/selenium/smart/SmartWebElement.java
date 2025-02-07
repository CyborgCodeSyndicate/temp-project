package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebElementDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
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

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

public class SmartWebElement extends WebElementDecorator {

    @Getter
    @Setter
    private WebDriver driver;
    private final WebDriverWait wait;


    public SmartWebElement(WebElement original, WebDriver driver) {
        super(original);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getUiConfig().waitDuration()));
    }


    @HandleUIException
    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return SmartFinder.findElementsNoWrap(this, by);
        }

        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementsWithShadowRootElement(this, by, this::waitWithoutFailure);
            } else {
                return SmartFinder.findElementsNormally(this, by, this::waitWithoutFailure);
            }
        } catch (Exception e) {
            return handleException("findElements", e, new Object[]{by});
        }
    }


    @HandleUIException
    public SmartWebElement findSmartElement(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return SmartFinder.findElementNoWrap(this, by);
        }
        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementWithShadowRootElement(this, by, this::waitWithoutFailure);
            } else {
                return SmartFinder.findElementNormally(this, by, this::waitWithoutFailure);
            }
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

    @Override
    public void submit() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.submit();
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            super.submit();
        } catch (Exception e) {
            handleException("submit", e, new Object[0]);
        }
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
                    .apply(driver, this, exception, params);
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