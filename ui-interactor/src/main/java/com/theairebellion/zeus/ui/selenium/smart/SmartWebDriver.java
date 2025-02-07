package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.selenium.decorators.WebDriverDecorator;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;


@Getter
public class SmartWebDriver extends WebDriverDecorator {

    @Getter
    @Setter
    private WebDriver driver;
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

    public SmartWebElement findSmartElement(By by, long waitInMillis) {
        return findSmartElementInternal(by, waitInMillis);
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



}
