package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggingFunctions {


    public static void findElementFromRootNoSuchElementExceptionLogging(final Object target, final String method, final Object[] args,
                                                                        final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebDriver driver = (WebDriver) target;

        String pageSource = driver.getPageSource();
        if (pageSource != null && pageSource.length() > 1000) {
            pageSource = pageSource.substring(0, 1000) + "...";
        }
        String additionalInfo = String.format(
                "Element was not found from root by using locator: [%s]. Here is the whole page source: \n[%s]",
                locator.toString(), pageSource);
        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void findElementFromElementNoSuchElementExceptionLogging(final Object target, final String method, final Object[] args,
                                                                           final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebElement element = (WebElement) target;

        String elementInnerHtml = element.getAttribute("innerHTML");
        if (elementInnerHtml != null && elementInnerHtml.length() > 1000) {
            elementInnerHtml = elementInnerHtml.substring(0, 1000) + "...";
        }
        String additionalInfo = String.format(
                "Element was not found from another element: [%s] by using locator: [%s]. Here is the whole inner html: \n[%s]",
                element, locator.toString(), elementInnerHtml);
        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void clickElementNotInteractableExceptionLogging(final Object target, final String method, final Object[] args,
                                                                   final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (WebElement) target;

        String elementTagName = element.getTagName();
        String elementText = element.getText();
        String elementAttributes = element.getAttribute("outerHTML");

        if (elementAttributes != null && elementAttributes.length() > 1000) {
            elementAttributes = elementAttributes.substring(0, 1000) + "...";
        }

        String additionalInfo = String.format(
                "Element [%s] with text [%s] is not interactable when performing click(). " +
                        "Here is the outer HTML of the element: \n[%s]",
                elementTagName, elementText, elementAttributes);

        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void clickInvalidSelectorExceptionLogging(final Object target, final String method, final Object[] args, final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();

        String additionalInfo = String.format(
                "Invalid selector exception occurred while clicking element with malformed locator: [%s].",
                locator != null ? locator.toString() : "null");

        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void clickElementClickIterceptedExceptionLogging(final Object target, final String method, final Object[] args,
                                                                   final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (WebElement) target;
        String elementInnerHtml = element.getAttribute("innerHTML");
        if (elementInnerHtml != null && elementInnerHtml.length() > 1000) {
            elementInnerHtml = elementInnerHtml.substring(0, 1000) + "...";
        }
        String blockingElementLocator = extractLocatorFromMessage(cause.getMessage());

        String additionalInfo = String.format(
                "Failed to click on element: [%s]. Element's inner HTML: \n[%s]. " +
                        "Locator of the blocking element: [%s]",
                element, elementInnerHtml, blockingElementLocator);

        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void clickTimeoutExceptionLogging(final Object target, final String method, final Object[] args,
                                                    final InvocationTargetException e) {
        Throwable cause = e.getCause();
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        long timeout = (args != null && args.length > 1 && args[1] instanceof Long) ? (Long) args[1] : 0L;
        String timeoutMessage = timeout > 0 ? "Waited for " + timeout + " milliseconds." : "Timeout occurred with no specific timeout set.";
        String additionalInfo = String.format(
                "Timeout occurred while waiting for element to meet the condition: [%s]. Locator: [%s]. %s",
                method, locator != null ? locator.toString() : "Unknown locator", timeoutMessage);
        logException(cause.getClass(), target, method, args, additionalInfo);

    }


    private static void logException(Class<? extends Throwable> throwable, Object target, String methodName,
                                     Object[] methodArgs, String additionalInfo) {
        LogUI.extended("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with arguments: [{}]",
                throwable.getSimpleName(), target.getClass(), target.toString(), methodName,
                printMethodArgsClasses(methodArgs));
        LogUI.extended("Additional Info for the problem: {}", additionalInfo);
    }


    private static String printMethodArgsClasses(Object[] methodArgs) {
        if (methodArgs == null || methodArgs.length == 0) {
            return "";
        }
        return Arrays.stream(methodArgs)
                .map(arg -> arg == null ? "null" : arg.getClass().getSimpleName())
                .collect(Collectors.joining(","));
    }


    private static String extractRelevantHtml(String pageSource, String elementHtml, int charLimit) {
        if (pageSource == null || elementHtml == null || pageSource.isEmpty() || elementHtml.isEmpty()) {
            return "Page source unavailable.";
        }
        int index = pageSource.indexOf(elementHtml);
        if (index == -1) {
            return "Element HTML not found in page source.";
        }
        int start = Math.max(0, index - charLimit / 2);
        int end = Math.min(pageSource.length(), index + charLimit / 2);
        return pageSource.substring(start, end);
    }

    private static String extractLocatorFromMessage(String message) {
        if (message != null && message.contains("element must be clickable")) {
            String locatorPattern = "By\\.[a-zA-Z]+\\((.*?)\\)";
            if (message.matches(".*" + locatorPattern + ".*")) {
                return message.replaceAll(".*" + locatorPattern + ".*", "$1");
            }
        }
        return "Locator not found in exception message";
    }

}
