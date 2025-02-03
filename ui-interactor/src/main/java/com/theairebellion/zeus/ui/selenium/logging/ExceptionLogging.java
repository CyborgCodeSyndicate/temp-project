package com.theairebellion.zeus.ui.selenium.logging;

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
    FIND_ELEMENT_FROM_ROOT(WebDriver.class, "findElement",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromRootNoSuchElementExceptionLogging)),
    FIND_ELEMENTS_FROM_ROOT(WebDriver.class, "findElements",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromRootNoSuchElementExceptionLogging)),
    FIND_ELEMENT_FROM_ELEMENT(WebElement.class, "findElement",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromElementNoSuchElementExceptionLogging)),
    FIND_ELEMENTS_FROM_ELEMENT(WebElement.class, "findElements",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromElementNoSuchElementExceptionLogging)),
    CLICK(WebElement.class, "click",
            Map.of(ElementNotInteractableException.class, LoggingFunctions::clickElementNotInteractableExceptionLogging,
                    InvalidSelectorException.class, LoggingFunctions::clickInvalidSelectorExceptionLogging,
                    ElementClickInterceptedException.class, LoggingFunctions::clickElementClickIterceptedExceptionLogging,
                    TimeoutException.class, LoggingFunctions::clickTimeoutExceptionLogging));


    private final Class<?> objectClass;
    private final String methodName;
    private final Map<Class<? extends Throwable>, FourConsumer<Object, String, Object[], InvocationTargetException>>
            exceptionLoggingMap;


    ExceptionLogging(final Class<?> objectClass, final String methodName,
                     Map<Class<? extends Throwable>, FourConsumer<Object, String, Object[], InvocationTargetException>> exceptionLoggingMap) {
        this.objectClass = objectClass;
        this.methodName = methodName;
        this.exceptionLoggingMap = exceptionLoggingMap;
    }
}



