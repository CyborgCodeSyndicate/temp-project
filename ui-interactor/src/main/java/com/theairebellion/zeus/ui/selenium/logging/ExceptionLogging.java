package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.FourConsumer;
import lombok.Getter;
import org.openqa.selenium.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

/**
 * Enum representing different exception logging scenarios for Selenium WebDriver actions.
 * <p>
 * Each entry in the enum defines a WebDriver or WebElement action along with
 * a mapping of exception types to their corresponding logging functions.
 * This facilitates structured logging when exceptions occur during UI interactions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Getter
public enum ExceptionLogging {
    /**
     * Logs exceptions when attempting to find a single element from the root WebDriver.
     */
    FIND_ELEMENT_FROM_ROOT(WebDriver.class, WebElementAction.FIND_ELEMENT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logFindElementFromRootNoSuchElementException)),

    /**
     * Logs exceptions when attempting to find multiple elements from the root WebDriver.
     */
    FIND_ELEMENTS_FROM_ROOT(WebDriver.class, WebElementAction.FIND_ELEMENTS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logFindElementFromRootNoSuchElementException)),

    /**
     * Logs exceptions when attempting to find a single element within another WebElement.
     */
    FIND_ELEMENT_FROM_ELEMENT(WebElement.class, WebElementAction.FIND_ELEMENT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException)),

    /**
     * Logs exceptions when attempting to find multiple elements within another WebElement.
     */
    FIND_ELEMENTS_FROM_ELEMENT(WebElement.class, WebElementAction.FIND_ELEMENTS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException)),

    /**
     * Logs exceptions related to clicking a WebElement, including various interaction failures.
     */
    CLICK(WebElement.class, WebElementAction.CLICK,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException,
                    InvalidSelectorException.class, LoggingFunctions::logClickInvalidSelectorException,
                    ElementClickInterceptedException.class, LoggingFunctions::logElementClickInterceptedException,
                    TimeoutException.class, LoggingFunctions::logClickTimeoutException)),

    /**
     * Logs exceptions related to sending keys to a WebElement.
     */
    SEND_KEYS(WebElement.class, WebElementAction.SEND_KEYS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException,
                    ElementClickInterceptedException.class, LoggingFunctions::logElementClickInterceptedException)),

    /**
     * Logs exceptions related to submitting a WebElement.
     */
    SUBMIT(WebElement.class, WebElementAction.SUBMIT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException));

    /**
     * The class type this exception logging applies to (WebDriver or WebElement).
     */
    private final Class<?> targetClass;

    /**
     * The WebElementAction associated with this exception logging case.
     */
    private final WebElementAction action;

    /**
     * A mapping of exception types to their respective logging functions.
     */
    private final Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
            exceptionLoggingMap;

    /**
     * Constructs an exception logging entry for a given action and exception mapping.
     *
     * @param targetClass         The class type this logging applies to (WebDriver or WebElement).
     * @param action              The WebElementAction associated with this exception logging case.
     * @param exceptionLoggingMap A map of exception classes to their respective logging functions.
     */
    ExceptionLogging(final Class<?> targetClass, final WebElementAction action,
                     Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> exceptionLoggingMap) {
        this.targetClass = targetClass;
        this.action = action;
        this.exceptionLoggingMap = exceptionLoggingMap;
    }


    public Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> getExceptionLoggingMap() {
        return Collections.unmodifiableMap(exceptionLoggingMap);
    }
}



