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
                "Exception [%s]: Failed to locate element using locator [%s] at the root level.\n" +
                        "Possible reasons: incorrect locator, element not present in the DOM, or slow page loading.\n\n" +
                        "Current Page Source (truncated):\n%s",
                cause.getClass().getSimpleName(), locator, pageSource);

        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void noSuchElementExceptionLogging(final Object target, final String method, final Object[] args,
                                                                           final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;

        if (element == null) {
            logException(cause.getClass(), target, method, args,
                    String.format("Exception [%s]: Unable to process the element. It is null, possibly due to stale reference or incorrect locator.",
                            cause.getClass().getSimpleName()));
            return;
        }

        String elementTagName = element.getTagName();
        String elementText = element.getText();
        String elementAttributes = truncateString(element.getAttribute("outerHTML"), 1000);
        String elementInnerHtml = truncateString(element.getAttribute("innerHTML"), 1000);

        String actionMessage = switch (method) {
            case "findElement", "findElements" -> locator != null
                    ? String.format("Exception [%s]: Unable to locate element [%s] using locator [%s]. " +
                            "Possible reasons: incorrect locator, slow loading elements, or missing elements in DOM.",
                    cause.getClass().getSimpleName(), elementTagName, locator)
                    : String.format("Exception [%s]: Unable to locate element [%s], but no locator was provided.",
                    cause.getClass().getSimpleName(), elementTagName);

            case "click" -> String.format("Exception [%s]: Click action failed on element [%s] with text [%s]. " +
                            "Possible reasons: element is covered by another element, disabled, or outside the viewport.",
                    cause.getClass().getSimpleName(), elementTagName, elementText);

            case "sendKeys" -> String.format("Exception [%s]: Unable to send keys to element [%s] with text [%s]. " +
                            "Possible reasons: element is not interactable, readonly, or detached from the DOM.",
                    cause.getClass().getSimpleName(), elementTagName, elementText);

            case "submit" -> String.format("Exception [%s]: Unable to submit form using element [%s]. " +
                            "Possible reasons: element is not a valid form element, not interactable, or detached from the DOM.",
                    cause.getClass().getSimpleName(), elementTagName);

            default -> String.format("Exception [%s]: Unexpected issue occurred while interacting with element [%s].",
                    cause.getClass().getSimpleName(), elementTagName);
        };

        String additionalInfo = String.format(
                "%s\nElement Attributes:\n[%s]\nElement Inner HTML (truncated):\n[%s]",
                actionMessage, elementAttributes, elementInnerHtml);

        logException(cause.getClass(), target, method, args, additionalInfo);
    }

    public static void elementNotInteractableExceptionLogging(final Object target, final String method, final Object[] args,
                                                              final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;

        if (element == null) {
            logException(cause.getClass(), target, method, args, "Element is null, cannot log additional info.");
            return;
        }

        String elementTagName = element.getTagName();
        String elementText = element.getText();
        String elementAttributes = truncateString(element.getAttribute("outerHTML"), 1000);

        String action = switch (method) {
            case "click" -> "click";
            case "sendKeys" -> "send keys";
            case "submit" -> "submit";
            default -> "interact";
        };

        String additionalInfo = String.format(
                "Exception [%s]: Element [%s] with text [%s] is not interactable when attempting to perform %s().\n" +
                        "Possible reasons: the element is hidden, disabled, covered by another element, or detached from the DOM.\n\n" +
                        "Element Outer HTML (truncated):\n%s",
                cause.getClass().getSimpleName(), elementTagName, elementText, action, elementAttributes);

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

    public static void elementClickInterceptedExceptionLogging(final Object target, final String method, final Object[] args,
                                                               final InvocationTargetException e) {
        Throwable cause = e.getCause();
        WebElement element = (target instanceof WebElement) ? (WebElement) target : null;

        if (element == null) {
            logException(cause.getClass(), target, method, args, "Element is null, cannot log additional info.");
            return;
        }

        String elementInnerHtml = truncateString(element.getAttribute("innerHTML"), 1000);
        String blockingElementLocator = extractLocatorFromMessage(cause.getMessage());

        String action = switch (method) {
            case "click" -> "click";
            case "sendKeys" -> "send keys";
            default -> "interact";
        };

        String additionalInfo = String.format(
                "Failed to %s on element: [%s]. Element's inner HTML: \n[%s]. " +
                        "Locator of the blocking element: [%s]",
                action, element, elementInnerHtml, blockingElementLocator);

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
