package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

import static com.theairebellion.zeus.ui.selenium.enums.WebElementAction.CLICK;
import static com.theairebellion.zeus.ui.selenium.enums.WebElementAction.SEND_KEYS;
import static com.theairebellion.zeus.ui.selenium.enums.WebElementAction.SUBMIT;


public class ExceptionHandlingWebElementFunctions {

    private static final String UNSUPPORTED_OPERATION = "Unsupported operation.";

    public static Object handleStaleElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = updateWebElement(driver, element);

        return switch (webElementAction) {
            case FIND_ELEMENT -> new SmartWebElement(element.getOriginal().findElement((By) args[0]), driver);
            case FIND_ELEMENTS -> null;
            case CLICK -> {
                CLICK.performAction(driver, element.getOriginal(), CLICK);
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performAction(driver, element.getOriginal(), SEND_KEYS, args[0]);
                yield null;
            }
            case SUBMIT -> {
                SUBMIT.performAction(driver, element.getOriginal(), SUBMIT);
                yield null;
            }
        };
    }

    public static Object handleNoSuchElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        WebElement foundElement = findElementInFrames(driver, element);
        if (foundElement != null) {
            return webElementAction.performAction(driver, foundElement, webElementAction, args);
        }
        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }


    public static Void handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        By blocker = By.xpath(extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));

        return switch (webElementAction) {
            case CLICK -> {
                CLICK.performAction(driver, element.getOriginal(), CLICK);
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performAction(driver, element.getOriginal(), SEND_KEYS, args[0]);
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
                CLICK.performAction(driver, clickableElement, CLICK);
                yield null;
            }
            case SEND_KEYS -> {
                SEND_KEYS.performAction(driver, clickableElement, SEND_KEYS, args[0]);
                yield null;
            }
            case SUBMIT -> {
                SUBMIT.performAction(driver, clickableElement, SUBMIT);
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

        WebElement updatedElement = driver.findElement(new ByChained(locatorsList.toArray(new By[0])));

        return new SmartWebElement(updatedElement, driver);
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

    private static WebElement findElementInFrames(WebDriver driver, SmartWebElement element) {
        if (element.isDisplayed()) {
            return element.getOriginal();
        }
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            driver.switchTo().frame(iframe);
            if (element.isDisplayed()) {
                return element.getOriginal();
            }
            driver.switchTo().defaultContent();
        }
        return null;
    }
}
