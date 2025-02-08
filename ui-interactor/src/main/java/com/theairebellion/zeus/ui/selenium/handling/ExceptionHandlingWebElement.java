package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.FourFunction;
import lombok.Getter;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
public enum ExceptionHandlingWebElement {

    FIND_ELEMENTS("findElements",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.FIND_ELEMENTS, objects[0])
            )),


    FIND_ELEMENT("findElement",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.FIND_ELEMENT, objects[0])
            )),


    CLICK("click",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.CLICK),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(driver, smartWebElement, WebElementAction.CLICK, exception),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.CLICK)
            )),

    SEND_KEYS("sendKeys",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.SEND_KEYS, objects[0]),
                    ElementClickInterceptedException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(driver, smartWebElement, WebElementAction.SEND_KEYS, exception, objects[0]),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.SEND_KEYS, objects[0])
            )),

    SUBMIT("submit",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.SUBMIT),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.SUBMIT)
            )),

    CLEAR("clear",
            Map.of(StaleElementReferenceException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleStaleElement(driver, smartWebElement, WebElementAction.CLEAR),
                    ElementNotInteractableException.class, (driver, smartWebElement, exception, objects) ->
                            ExceptionHandlingWebElementFunctions.handleElementNotInteractable(driver, smartWebElement, WebElementAction.CLEAR)
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
