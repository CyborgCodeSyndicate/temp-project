package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocatorParser {

    private static final String LOCATOR_NOT_FOUND_MESSAGE = "Locator not found";
    private static final String NO_MESSAGE_AVAILABLE = "No message available";

    public static SmartWebElement updateWebElement(WebDriver driver, SmartWebElement element) {
        LogUI.extended("Element: '{}' is being relocated.", element.toString());
        List<By> locatorsList = new ArrayList<>();
        try {
            locatorsList = parseLocators(element.toString());
        } catch (Exception ignore) {
        }
        if (locatorsList.isEmpty()) {
            throw new RuntimeException("Element can't be auto updated");
        }
        return new SmartWebDriver(driver).findSmartElement(
                new ByChained(locatorsList.toArray(new By[0])), 10);
    }

    public static String extractBlockingElementLocator(String exceptionMessage) {
        String pattern = "Other element would receive the click: <(\\w+)([^>]*)>";
        Matcher matcher = Pattern.compile(pattern).matcher(exceptionMessage);
        if (matcher.find()) {
            String tag = matcher.group(1);
            String attributes = matcher.group(2).trim();
            if (attributes.isEmpty()) {
                return "//" + tag;
            }
            String[] attrPairs = attributes.split("\\s+");
            StringBuilder xpath = new StringBuilder("//" + tag + "[");
            for (int i = 0; i < attrPairs.length; i++) {
                String[] keyValue = attrPairs[i].split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1].replace("\"", "").replace("'", "");
                    xpath.append("@").append(key).append("='").append(value).append("'");
                    if (i < attrPairs.length - 1) {
                        xpath.append(" and ");
                    }
                }
            }
            xpath.append("]");
            return xpath.toString();
        }
        return null;
    }

    public static By extractLocator(Object[] args) {
        if (args != null && args.length > 0 && args[0] instanceof By) {
            return (By) args[0];
        }
        return null;
    }

    public static String extractLocatorFromMessage(String message) {
        if (message == null) return NO_MESSAGE_AVAILABLE;
        String locatorPattern = "By\\.(\\w+)\\((.*?)\\)";
        Pattern pattern = Pattern.compile(locatorPattern);
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.group() : LOCATOR_NOT_FOUND_MESSAGE;
    }

    public static String getElementDetails(WebElement element, String outerHTML, String innerHTML) {
        String tag = element.getTagName();
        String text = element.getText();
        return String.format("Tag: [%s]%nText: [%s]%nOuter HTML: [%s]%nInner HTML: [%s]",
                tag, text, outerHTML, innerHTML);
    }

    private static List<By> parseLocators(String locatorString) {
        String[] parts = locatorString.split("->");
        List<By> byList = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String trimmed = parts[i].trim();
            addLocatorIfMatches(byList, trimmed, "tag name", By::tagName);
            addLocatorIfMatches(byList, trimmed, "css selector", By::cssSelector);
            addLocatorIfMatches(byList, trimmed, "xpath", By::xpath);
            addLocatorIfMatches(byList, trimmed, "id", By::id);
            addLocatorIfMatches(byList, trimmed, "class name", By::className);
            addLocatorIfMatches(byList, trimmed, "name", By::name);
            addLocatorIfMatches(byList, trimmed, "link text", By::linkText);
            addLocatorIfMatches(byList, trimmed, "partial link text", By::partialLinkText);
        }
        return byList;
    }

    private static void addLocatorIfMatches(List<By> locatorList, String locatorText, String key,
                                            Function<String, By> locatorFunction) {
        if (locatorText.split(":")[0].equals(key)) {
            String locator = extractLocatorValue(locatorText);
            locatorList.add(locatorFunction.apply(locator));
        }
    }

    private static String extractLocatorValue(String locatorText) {
        String[] parts = locatorText.split(":");
        String value = parts[1].trim();
        return value.substring(0, value.length() - 2);
    }
}
