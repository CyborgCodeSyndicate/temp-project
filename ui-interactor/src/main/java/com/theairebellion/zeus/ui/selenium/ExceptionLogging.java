package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.util.FourConsumer;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;


public enum ExceptionLogging {
    FIND_ELEMENT_FROM_ROOT(WebElement.class, "findElement", NoSuchElementException.class,
        LoggingFunctions::findElementFromRoot),
    FIND_ELEMENTS_FROM_ROOT(WebElement.class, "findElements", NoSuchElementException.class,
        LoggingFunctions::findElementFromRoot),
    FIND_ELEMENT_FROM_ELEMENT(WebElement.class, "findElement", NoSuchElementException.class,
        LoggingFunctions::findElementFromElement),
    FIND_ELEMENTS_FROM_ELEMENT(WebElement.class, "findElements", NoSuchElementException.class,
        LoggingFunctions::findElementFromElement);


    @Getter
    private final Class<?> objectClass;
    @Getter
    private final String methodName;
    @Getter
    private final Class<? extends Throwable> exceptionClass;
    @Getter
    private final FourConsumer<Object, String, Object[], InvocationTargetException> handleFunction;


    ExceptionLogging(final Class<?> objectClass, final String methodName,
                     final Class<? extends Throwable> exceptionClass,
                     final FourConsumer<Object, String, Object[], InvocationTargetException> handleFunction) {
        this.objectClass = objectClass;
        this.methodName = methodName;
        this.exceptionClass = exceptionClass;
        this.handleFunction = handleFunction;
    }
}



