package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.util.FourConsumer;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


@Getter
public enum ExceptionLogging {
    FIND_ELEMENT_FROM_ROOT(WebDriver.class, "findElement",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromRoot)),
    FIND_ELEMENTS_FROM_ROOT(WebDriver.class, "findElements",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromRoot)),
    FIND_ELEMENT_FROM_ELEMENT(WebElement.class, "findElement",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromElement)),
    FIND_ELEMENTS_FROM_ELEMENT(WebElement.class, "findElements",
            Map.of(NoSuchElementException.class, LoggingFunctions::findElementFromElement));


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



