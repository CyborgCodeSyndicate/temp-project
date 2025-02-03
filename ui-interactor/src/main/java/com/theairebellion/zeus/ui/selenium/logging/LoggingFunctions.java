package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LoggingFunctions {

    private static final String LOCATOR_NOT_FOUND_MESSAGE = "Locator not found";
    private static final String PAGE_SOURCE_UNAVAILABLE = "Page source unavailable.";
    private static final String ELEMENT_HTML_NOT_FOUND = "Element HTML not found in page source.";
    private static final String NO_MESSAGE_AVAILABLE = "No message available";


    public static void findElementFromRootNoSuchElementExceptionLogging(final Object target, final String method, final Object[] args,
                                                                        final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebDriver driver = (target instanceof WebDriver) ? (WebDriver) target : null;
        assert driver != null;
        String pageSource = truncateString(driver.getPageSource(), 1000);
        assert locator != null;
        String additionalInfo = String.format(
                "Element was not found from root by using locator: [%s]. Here is the whole page source: \n[%s]",
                locator, pageSource);
        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void findElementFromElementNoSuchElementExceptionLogging(final Object target, final String method, final Object[] args,
                                                                           final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;

        assert element != null;
        String elementInnerHtml = truncateString(element.getAttribute("innerHTML"), 1000);
        assert locator != null;
        String additionalInfo = String.format(
                "Element was not found from another element: [%s] by using locator: [%s]. Here is the whole inner html: \n[%s]",
                element, locator, elementInnerHtml);
        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void clickElementNotInteractableExceptionLogging(final Object target, final String method, final Object[] args,
                                                                   final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;

        assert element != null;
        String elementTagName = element.getTagName();
        String elementText = element.getText();
        String elementAttributes = truncateString(element.getAttribute("outerHTML"), 1000);

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
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;
        assert element != null;
        String elementInnerHtml = truncateString(element.getAttribute("innerHTML"), 1000);
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
            return PAGE_SOURCE_UNAVAILABLE;
        }
        int index = pageSource.indexOf(elementHtml);
        if (index == -1) {
            return ELEMENT_HTML_NOT_FOUND;
        }
        int start = Math.max(0, index - charLimit / 2);
        int end = Math.min(pageSource.length(), index + charLimit / 2);
        return pageSource.substring(start, end);
    }

    private static String extractLocatorFromMessage(String message) {
        if (message == null) return NO_MESSAGE_AVAILABLE;
        String locatorPattern = "By\\.(\\w+)\\((.*?)\\)";
        Pattern pattern = Pattern.compile(locatorPattern);
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group() : LOCATOR_NOT_FOUND_MESSAGE;
    }

    private static String truncateString(String input, int length) {
        if (input == null) return PAGE_SOURCE_UNAVAILABLE;
        return input.length() > length ? input.substring(0, length) + "..." : input;
    }

}
