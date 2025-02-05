package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.FourConsumer;
import lombok.Getter;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


@Getter
public enum ExceptionLogging {
    FIND_ELEMENT_FROM_ROOT(WebDriver.class, WebElementAction.FIND_ELEMENT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logFindElementFromRootNoSuchElementException)),
    FIND_ELEMENTS_FROM_ROOT(WebDriver.class, WebElementAction.FIND_ELEMENTS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logFindElementFromRootNoSuchElementException)),
    FIND_ELEMENT_FROM_ELEMENT(WebElement.class, WebElementAction.FIND_ELEMENT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException)),
    FIND_ELEMENTS_FROM_ELEMENT(WebElement.class, WebElementAction.FIND_ELEMENTS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException)),
    CLICK(WebElement.class, WebElementAction.CLICK,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException,
                    InvalidSelectorException.class, LoggingFunctions::logClickInvalidSelectorException,
                    ElementClickInterceptedException.class, LoggingFunctions::logElementClickInterceptedException,
                    TimeoutException.class, LoggingFunctions::logClickTimeoutException)),
    SEND_KEYS(WebElement.class, WebElementAction.SEND_KEYS,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException,
                    ElementClickInterceptedException.class, LoggingFunctions::logElementClickInterceptedException)),
    SUBMIT(WebElement.class, WebElementAction.SUBMIT,
            Map.of(NoSuchElementException.class, LoggingFunctions::logNoSuchElementException,
                    ElementNotInteractableException.class, LoggingFunctions::logElementNotInteractableException));


    private final Class<?> objectClass;
    private final WebElementAction webElementAction;
    private final Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>>
            exceptionLoggingMap;


    ExceptionLogging(final Class<?> objectClass, final WebElementAction webElementAction,
                     Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> exceptionLoggingMap) {
        this.objectClass = objectClass;
        this.webElementAction = webElementAction;
        this.exceptionLoggingMap = exceptionLoggingMap;
    }
}



