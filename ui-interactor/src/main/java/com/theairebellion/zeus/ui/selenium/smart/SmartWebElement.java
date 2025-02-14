package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebElementDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
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
import java.util.function.Consumer;
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
        performActionWithWait(element -> super.click());
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
    @HandleUIException
    public void clear() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.clear();
        }
        performActionWithWait(element -> super.clear());
    }


    @Override
    @NullMarked
    public void sendKeys(CharSequence... keysToSend) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.sendKeys(keysToSend);
        }
        performActionWithWait(element -> super.sendKeys(keysToSend));
    }

    @Override
    public void submit() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.submit();
        }
        performActionWithWait(element -> super.submit());
    }

    public void clearAndSendKeys(CharSequence... keysToSend) {
        clear();
        sendKeys(keysToSend);
    }


    public boolean isEnabledAndVisible() {
        waitWithoutFailure(ExpectedConditions.and(
                ExpectedConditions.visibilityOf(this),
                ExpectedConditions.elementToBeClickable(this)
        ));
        return true;
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


    private void performActionWithWait(Consumer<SmartWebElement> action) {
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            action.accept(this);
        } catch (Exception e) {
            handleException(action.toString(), e, new Object[0]);
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
