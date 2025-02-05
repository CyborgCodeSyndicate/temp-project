package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.TriFunction;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
public enum ExceptionHandlingWebElement {

    FIND_ELEMENT("findElement",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, objects) ->
                    ExceptionHandlingWebElementFunctions.findElementHandling(driver, smartWebElement, (By) objects[0]))
    ),


    CLICK_ELEMENT("click", Map.of(
            StaleElementReferenceException.class, (driver, smartWebElement, objects) ->
                    ExceptionHandlingWebElementFunctions.clickElementHandling(driver, smartWebElement))
    );


    private final String methodName;
    private final Map<Class<? extends Throwable>, TriFunction<WebDriver, SmartWebElement, Object[], Object>>
            exceptionHandlingMap;


    ExceptionHandlingWebElement(final String methodName,
                                Map<Class<? extends Throwable>, TriFunction<WebDriver, SmartWebElement, Object[], Object>> exceptionHandlingMap) {
        this.methodName = methodName;
        this.exceptionHandlingMap = exceptionHandlingMap;
    }


}
