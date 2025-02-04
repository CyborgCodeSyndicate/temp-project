package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.FourFunction;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
public enum ExceptionHandlingWebElement {

    FIND_ELEMENT("findElement",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.findElementStaleElementExceptionHandling(driver, smartWebElement, (By) objects[0]),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.findElementNoSuchElementExceptionHandling(driver, smartWebElement)
            )),


    CLICK("click",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.clickStaleElementExceptionHandling(driver, smartWebElement),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.clickElementClickInterceptedExceptionHandling(driver, smartWebElement, exception),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.clickElementNotInteractableExceptionHandling(driver, smartWebElement),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.clickNoSuchElementExceptionHandling(driver, smartWebElement)
            )),

    SEND_KEYS("sendKeys",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.sendKeysStaleElementExceptionHandling(driver, (String) objects[0], smartWebElement),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.sendKeysElementClickInterceptedExceptionHandling(driver, (String) objects[0], smartWebElement, exception),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.sendKeysElementNotInteractableExceptionHandling(driver, (String) objects[0], smartWebElement),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.sendKeysNoSuchElementExceptionHandling(driver, (String) objects[0], smartWebElement)
            )),

    SUBMIT("submit",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.submitStaleElementExceptionHandling(driver, smartWebElement),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.submitElementNotInteractableExceptionHandling(driver, smartWebElement),
                    NoSuchElementException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.submitNoSuchElementExceptionHandling(driver, smartWebElement)
            ));


    private final String methodName;
    private final Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
            exceptionHandlingMap;


    ExceptionHandlingWebElement(final String methodName,
                                Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>> exceptionHandlingMap) {
        this.methodName = methodName;
        this.exceptionHandlingMap = exceptionHandlingMap;
    }


}
