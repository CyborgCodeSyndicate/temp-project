package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.BiFunction;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
public enum ExceptionHandlingWebDriver {

    FIND_ELEMENT("findElement",
            Map.of(
                    NoSuchElementException.class,
                    (driver, objects) ->
                            ExceptionHandlingWebDriverFunctions.handleNoSuchElement(driver, WebElementAction.FIND_ELEMENT, objects)
            )),

    FIND_ELEMENTS("findElements",
            Map.of(
                    NoSuchElementException.class,
                    (driver, objects) ->
                            ExceptionHandlingWebDriverFunctions.handleNoSuchElement(driver, WebElementAction.FIND_ELEMENTS, objects)
            ));


    private final String methodName;
    private final Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>>
            exceptionHandlingMap;


    ExceptionHandlingWebDriver(final String methodName,
                               Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> exceptionHandlingMap) {
        this.methodName = methodName;
        this.exceptionHandlingMap = exceptionHandlingMap;
    }
}
