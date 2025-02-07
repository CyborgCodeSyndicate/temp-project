package com.theairebellion.zeus.ui.selenium.locating;

import com.theairebellion.zeus.ui.selenium.shadowroot.ShadowDomUtils;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SmartFinder {

    private SmartFinder() {
    }


    public static SmartWebElement findElementNoWrap(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new SmartWebElement(element, driver);
    }


    public static SmartWebElement findElementNormally(
            WebDriver driver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        return findElementNoWrap(driver, by);
    }


    public static SmartWebElement findElementWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn,
            Long waitInMillis
    ) {
        List<WebElement> elements = smartDriver.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return new SmartWebElement(elements.get(0), smartDriver.getOriginal());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartDriver)) {
            return (waitInMillis == null)
                    ? ShadowDomUtils.findElementInShadowRoots(smartDriver, by)
                    : ShadowDomUtils.findElementInShadowRoots(smartDriver, by, waitInMillis);
        } else {
            waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
            WebElement element = smartDriver.getOriginal().findElement(by);
            return new SmartWebElement(element, smartDriver.getOriginal());
        }
    }


    public static SmartWebElement findElementWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        return findElementWithShadowRootDriver(smartDriver, by, waitFn, null);
    }


    public static List<SmartWebElement> findElementsNoWrap(WebDriver driver, By by) {
        return driver.findElements(by).stream()
                .map(elem -> new SmartWebElement(elem, driver))
                .collect(Collectors.toList()); // or .toList() if using Java 16+
    }


    public static List<SmartWebElement> findElementsNormally(
            WebDriver driver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        List<WebElement> elements = driver.findElements(by);

        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, driver);
        }
        WebElement single = driver.findElement(by);
        return List.of(new SmartWebElement(single, driver));
    }


    public static List<SmartWebElement> findElementsWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        WebDriver driver = smartDriver.getOriginal();
        List<WebElement> elements = driver.findElements(by);

        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, driver);
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartDriver)) {
            return ShadowDomUtils.findElementsInShadowRoots(smartDriver, by);
        } else {
            waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
            List<WebElement> elementsAfterWait = driver.findElements(by);
            if (!elementsAfterWait.isEmpty()) {
                return toSmartWebElements(elementsAfterWait, driver);
            } else {
                WebElement single = driver.findElement(by);
                return List.of(new SmartWebElement(single, driver));
            }
        }
    }


    public static SmartWebElement findElementNoWrap(SmartWebElement smartElem, By by) {
        WebElement element = smartElem.getOriginal().findElement(by);
        return new SmartWebElement(element, smartElem.getDriver());
    }


    public static SmartWebElement findElementNormally(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = smartElem.getOriginal().findElement(by);
        return new SmartWebElement(element, smartElem.getDriver());
    }


    public static SmartWebElement findElementWithShadowRootElement(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return new SmartWebElement(elements.get(0), smartElem.getDriver());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartElem)) {
            return ShadowDomUtils.findElementInShadowRoots(smartElem, by);
        } else {
            return findElementNormally(smartElem, by, waitFn);
        }
    }


    public static List<SmartWebElement> findElementsNoWrap(SmartWebElement smartElem, By by) {
        return smartElem.getOriginal().findElements(by).stream()
                .map(e -> new SmartWebElement(e, smartElem.getDriver()))
                .collect(Collectors.toList());
    }


    public static List<SmartWebElement> findElementsNormally(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, smartElem.getDriver());
        } else {
            WebElement single = smartElem.getOriginal().findElement(by);
            return List.of(new SmartWebElement(single, smartElem.getDriver()));
        }
    }


    public static List<SmartWebElement> findElementsWithShadowRootElement(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, smartElem.getDriver());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartElem)) {
            return ShadowDomUtils.findElementsInShadowRoots(smartElem, by);
        } else {
            return findElementsNormally(smartElem, by, waitFn);
        }
    }


    private static List<SmartWebElement> toSmartWebElements(List<WebElement> elements, WebDriver driver) {
        return elements.stream()
                .map(e -> new SmartWebElement(e, driver))
                .collect(Collectors.toList());
    }

}