package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebDriver;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.NonNull;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;


@Getter
public class SmartWebDriver extends WebDriverDecorator {

    private final WebDriverWait wait;
    @Setter
    private boolean keepDriverForSession;

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
    @NonNull
    public WebElement findElement(@NonNull By by) {
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

    @Override
    @NonNull
    public List<WebElement> findElements(@NonNull By by) {
        Consumer<Function<WebDriver, ?>> waitFn =
                condition -> {
                    try {
                        WebDriverWait customWait = new WebDriverWait(getOriginal(), Duration.ofMillis(10));
                        customWait.until(condition);
                    } catch (Exception ignored) {
                    }
                };
        if (getUiConfig().useShadowRoot()) {
            List<SmartWebElement> elementsWithShadowRootDriver = SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
            return elementsWithShadowRootDriver.stream()
                    .map(element -> (WebElement) element)
                    .collect(Collectors.toList());
        } else {
            List<SmartWebElement> elementsNormally = SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
            return elementsNormally.stream()
                    .map(element -> (WebElement) element)
                    .collect(Collectors.toList());
        }
    }


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
            try {
                return (T) exceptionHandlingOptional.get()
                        .getExceptionHandlingMap()
                        .get(cause.getClass())
                        .apply(this.getOriginal(), params);
            } catch (Exception handlerException) {
                LogUI.error("Framework attempted to handle an exception in method '" + methodName
                        + "', but the handler failed with: " + handlerException.getClass().getSimpleName() + ": "
                        + handlerException.getMessage(), handlerException);
                exception.addSuppressed(handlerException);
                LogUI.error("Propagating original exception: " + exception.getClass().getSimpleName()
                        + ": " + exception.getMessage(), exception);
                throw exception;
            }
        } else {
            LogUI.error("No exception handling for this specific exception.");
            throw exception;
        }
    }
}
