package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.FourFunction;
import lombok.Getter;
import org.openqa.selenium.*;

import java.util.Map;

/**
 * Enum that defines exception handling strategies for WebElement actions.
 * <p>
 * Each entry in this enum represents a specific WebElement action
 * and maps known exceptions to their respective handling functions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Getter
public enum ExceptionHandlingWebElement {

    /**
     * Handles exceptions for the findElement action.
     */
    FIND_ELEMENT("findElement",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.FIND_ELEMENT, objects[0]),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.FIND_ELEMENT, objects[0])
            )),

    /**
     * Handles exceptions for the findElements action.
     */
    FIND_ELEMENTS("findElements",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.FIND_ELEMENTS, objects[0]),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.FIND_ELEMENTS, objects[0])
            )),

    /**
     * Handles exceptions for the click action.
     */
    CLICK("click",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.CLICK),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(driver, smartWebElement, WebElementAction.CLICK, exception),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.CLICK),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.CLICK, objects[0])
            )),

    /**
     * Handles exceptions for the sendKeys action.
     */
    SEND_KEYS("sendKeys",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.SEND_KEYS, objects[0]),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(driver, smartWebElement, WebElementAction.SEND_KEYS, exception, objects[0]),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.SEND_KEYS, objects[0]),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.SEND_KEYS, objects[0])
            )),

    /**
     * Handles exceptions for the submit action.
     */
    SUBMIT("submit",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.SUBMIT),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.SUBMIT),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.SUBMIT, objects[0])
            )),

    /**
     * Handles exceptions for the clear action.
     */
    CLEAR("clear",
            Map.of(
                    StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.CLEAR),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.CLEAR),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleNoSuchElement(driver, smartWebElement, WebElementAction.CLICK, objects[0])
            ));

    /**
     * The name of the method being handled.
     */
    private final String methodName;

    /**
     * A map associating exception types with their handling functions.
     */
    private final Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>> exceptionHandlingMap;

    /**
     * Constructs an instance of {@code ExceptionHandlingWebElement} with the specified method name and exception handling map.
     *
     * @param methodName           The name of the WebElement method.
     * @param exceptionHandlingMap The mapping of exception types to handling functions.
     */
    ExceptionHandlingWebElement(final String methodName,
                                Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>> exceptionHandlingMap) {
        this.methodName = methodName;
        this.exceptionHandlingMap = exceptionHandlingMap;
    }

}
