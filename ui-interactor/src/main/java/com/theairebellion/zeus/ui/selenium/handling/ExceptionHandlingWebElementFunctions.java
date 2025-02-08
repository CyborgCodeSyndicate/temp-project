package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.theairebellion.zeus.ui.selenium.enums.WebElementAction.*;


public class ExceptionHandlingWebElementFunctions {

    private static final String UNSUPPORTED_OPERATION = "Unsupported operation.";

    public static Object handleStaleElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = updateWebElement(driver, element);

        return switch (webElementAction) {
            case FIND_ELEMENT -> FIND_ELEMENT.performActionWebElement(driver, element, args[0]);
            case FIND_ELEMENTS -> FIND_ELEMENTS.performActionWebElement(driver, element, args[0]);
            case CLICK -> {
                CLICK.performActionWebElement(driver, element.getOriginal());
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performActionWebElement(driver, element.getOriginal(), args[0]);
                yield null;
            }
            case SUBMIT -> {
                SUBMIT.performActionWebElement(driver, element.getOriginal());
                yield null;
            }
            case CLEAR -> {
                CLEAR.performActionWebElement(driver, element.getOriginal());
                yield null;
            }
        };
    }

    public static Object handleNoSuchElement(WebDriver driver, WebElementAction webElementAction, Object... args) {
        if (args.length == 0 || !(args[0] instanceof By)) {
            LogUI.error("Invalid or missing locator argument for FIND_ELEMENT.");
            throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
        }

        WebElement foundElement = tryToFindElementInIFrame(driver, (By) args[0]);
        if (foundElement != null) {
            return webElementAction.performActionWebElement(driver, foundElement, args);
        }

        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }


    public static Void handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        By blocker = By.xpath(extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
        } catch (TimeoutException e) {
            LogUI.warn("Blocking element did not disappear after waiting, attempting action anyway.");
        }

        return switch (webElementAction) {
            case CLICK -> {
                CLICK.performActionWebElement(driver, element.getOriginal());
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performActionWebElement(driver, element.getOriginal(), args[0]);
                yield null;
            }
            case CLEAR -> {
                CLEAR.performActionWebElement(driver, element.getOriginal());
                yield null;
            }
            default -> throw new IllegalArgumentException(UNSUPPORTED_OPERATION);
        };
    }

    public static Void handleElementNotInteractable(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = updateWebElement(driver, element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal());
        WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));

        return switch (webElementAction) {
            case CLICK -> {
                CLICK.performActionWebElement(driver, clickableElement);
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performActionWebElement(driver, clickableElement, args[0]);
                yield null;
            }
            case SUBMIT -> {
                SUBMIT.performActionWebElement(driver, clickableElement);
                yield null;
            }
            case CLEAR -> {
                CLEAR.performActionWebElement(driver, clickableElement);
                yield null;
            }
            default -> throw new IllegalArgumentException(UNSUPPORTED_OPERATION);
        };
    }

    private static SmartWebElement updateWebElement(WebDriver driver, SmartWebElement element) {
        LogUI.extended("Element: '{}' is being relocated.", element.toString());

        final List<String> locators = Arrays.asList(element.toString().split("->"));
        final List<String> trimmedLocators = locators.stream()
                .map(String::trim)
                .toList();

        List<By> locatorsList = new ArrayList<>();

        for (int i = 1; i < trimmedLocators.size(); i++) {
            createLocator(locatorsList, trimmedLocators.get(i), "tag name", By::tagName);
            createLocator(locatorsList, trimmedLocators.get(i), "css selector", By::cssSelector);
            createLocator(locatorsList, trimmedLocators.get(i), "xpath", By::xpath);
            createLocator(locatorsList, trimmedLocators.get(i), "id", By::id);
            createLocator(locatorsList, trimmedLocators.get(i), "class name", By::className);
            createLocator(locatorsList, trimmedLocators.get(i), "name", By::name);
            createLocator(locatorsList, trimmedLocators.get(i), "link text", By::linkText);
            createLocator(locatorsList, trimmedLocators.get(i), "partial link text", By::partialLinkText);
        }

        return new SmartWebDriver(driver).findSmartElement(
                new ByChained(locatorsList.toArray(new By[0])), 10);
    }


    private static void createLocator(List<By> locatorList, String locatorText, String tagName,
                                      Function<String, By> locatorFunction) {
        if (locatorText.split(":")[0].equals(tagName)) {
            String locator = findLocator(locatorText);
            locatorList.add(locatorFunction.apply(locator));
        }
    }

    private static String findLocator(String locator) {
        String loc = locator.split(":")[1];
        String trimLocked = loc.trim();
        return trimLocked.substring(0, trimLocked.length() - 2);
    }

    private static String extractBlockingElementLocator(String exceptionMessage) {
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

    private static SmartWebElement tryToFindElementInIFrame(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return findElementRecursively(driver, by);

    }

    private static SmartWebElement findElementRecursively(WebDriver driver, By by) {
        try {
            SmartWebElement element = new SmartWebDriver(driver).findSmartElement(by, 10);
            return element;
        } catch (NoSuchElementException e) {
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<WebElement> frames = (List<WebElement>) js.executeScript(
                "return Array.from(document.getElementsByTagName('iframe'));"
        );

        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);

            WebElement elementInChild = findElementRecursively(driver, by);
            if (elementInChild != null) {
                return new SmartWebElement(elementInChild, driver);
            }

            driver.switchTo().parentFrame();
        }

        return null;
    }
}
