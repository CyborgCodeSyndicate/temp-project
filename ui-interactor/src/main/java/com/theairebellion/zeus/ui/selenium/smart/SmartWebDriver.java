package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
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

    public SmartWebElement findSmartElement(By by) {
        return findSmartElementInternal(by, null);
    }

    public SmartWebElement findSmartElement(By by, long waitInMillis) {
        return findSmartElementInternal(by, waitInMillis);
    }

    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            // Bypass all custom logic
            return SmartFinder.findElementsNoWrap(getOriginal(), by);
        }

        Consumer<Function<WebDriver, ?>> waitFn = this::waitWithoutFailure;
        if (getUiConfig().useShadowRoot()) {
            return SmartFinder.findElementsWithShadowRootDriver(this, by, waitFn);
        } else {
            return SmartFinder.findElementsNormally(getOriginal(), by, waitFn);
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

        if (getUiConfig().useShadowRoot()) {
            return SmartFinder.findElementWithShadowRootDriver(this, by, waitFn, waitInMillis);
        } else {
            return SmartFinder.findElementNormally(getOriginal(), by, waitFn);
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
}
