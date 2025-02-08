package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebDriver;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;


@Getter
public class SmartWebDriver extends WebDriverDecorator {

    private final WebDriverWait wait;

    public SmartWebDriver(WebDriver original) {
        super(original);
        this.wait = new WebDriverWait(original, Duration.ofSeconds(getUiConfig().waitDuration()));
    }

    @HandleUIException
    public SmartWebElement findSmartElement(By by) {
        return findSmartElementInternal(by, null);
    }

    public SmartWebElement findSmartElement(By by, long waitInMillis) {
        return findSmartElementInternal(by, waitInMillis);
    }

    @HandleUIException
    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            // Bypass all custom logic
            return SmartFinder.findElementsNoWrap(getOriginal(), by);
        }

        Consumer<Function<WebDriver, ?>> waitFn = this::waitWithoutFailure;
        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
            } else {
                return SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
            }
        } catch (Exception e) {
            return handleException("findElements", e, new Object[]{by});
        }
    }

    @Override
    public WebElement findElement(By by) {
        Consumer<Function<WebDriver, ?>> waitFn =
                condition -> {
                    try {
                        WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(10));
                        customWait.until(condition);
                    } catch (Exception ignored) {
                    }
                };
        if (getUiConfig().useShadowRoot()) {
            return SmartFinder.findElementWithShadowRootDriver(this, by, waitFn, 10L);
        } else {
            return SmartFinder.findElementNormally(getOriginal(), by, waitFn);
        }
    }

//    @Override
//    public List<WebElement> findElements(By by) {
//        Consumer<Function<WebDriver, ?>> waitFn =
//                condition -> {
//                    try {
//                        WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(10));
//                        customWait.until(condition);
//                    } catch (Exception ignored) {
//                    }
//                };
//        if (getUiConfig().useShadowRoot()) {
//            return SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
//        } else {
//            return SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
//        }
//    }

    private SmartWebElement findSmartElementInternal(By by, Long waitInMillis) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return SmartFinder.findElementNoWrap(getOriginal(), by);
        }

        Consumer<Function<WebDriver, ?>> waitFn = (waitInMillis == null)
                ? this::waitWithoutFailure
                : condition -> {
            try {
                WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(waitInMillis));
                customWait.until(condition);
            } catch (Exception ignored) {
            }
        };

        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementWithShadowRootDriver(this, by, waitFn, waitInMillis);
            } else {
                return SmartFinder.findElementNormally(getOriginal(), by, waitFn);
            }
        } catch (Exception e) {
            return handleException("findElement", e, new Object[]{by});
        }
    }

    private <T> void waitWithoutFailure(Function<WebDriver, T> condition) {
        try {
            wait.until(condition);
        } catch (Exception ignored) {
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T> T handleException(String methodName, Exception exception, Object[] params) {
        Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

        Optional<ExceptionHandlingWebDriver> exceptionHandlingOptional =
                Arrays.stream(ExceptionHandlingWebDriver.values())
                        .filter(enumVal ->
                                enumVal.getMethodName().equals(methodName)
                                        && Objects.nonNull(enumVal.getExceptionHandlingMap().get(cause.getClass()))
                        )
                        .findFirst();

        if (exceptionHandlingOptional.isPresent()) {
            return (T) exceptionHandlingOptional.get()
                    .getExceptionHandlingMap()
                    .get(cause.getClass())
                    .apply(this.getOriginal(), params);
        } else {
            LogUI.error("No exception handling for this specific exception.");
            throw exception;
        }
    }
}
