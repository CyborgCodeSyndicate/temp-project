package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.BiFunction;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * Enum representing exception handling strategies for Selenium WebDriver operations.
 * <p>
 * This enum maps specific WebDriver methods to exception handling logic, allowing
 * for customized recovery strategies when exceptions occur during execution.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Getter
public enum ExceptionHandlingWebDriver {

    /**
     * Handles exceptions for the {@code findElement} method.
     * When a {@link NoSuchElementException} occurs, a recovery strategy is applied.
     */
    FIND_ELEMENT("findElement",
            Map.of(
                    NoSuchElementException.class,
                    (driver, objects) ->
                            ExceptionHandlingWebDriverFunctions.handleNoSuchElement(driver, WebElementAction.FIND_ELEMENT, objects)
            )),

    /**
     * Handles exceptions for the {@code findElements} method.
     * When a {@link NoSuchElementException} occurs, a recovery strategy is applied.
     */
    FIND_ELEMENTS("findElements",
            Map.of(
                    NoSuchElementException.class,
                    (driver, objects) ->
                            ExceptionHandlingWebDriverFunctions.handleNoSuchElement(driver, WebElementAction.FIND_ELEMENTS, objects)
            ));

    /**
     * The name of the WebDriver method associated with this exception handler.
     */
    private final String methodName;

    /**
     * A map that associates exception types with their corresponding handling strategies.
     * This allows defining specific behaviors for different exception scenarios.
     */
    private final Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> exceptionHandlingMap;

    /**
     * Constructs an exception handling configuration for a WebDriver method.
     *
     * @param methodName           The name of the WebDriver method.
     * @param exceptionHandlingMap A map linking exception types to their handling strategies.
     */
    ExceptionHandlingWebDriver(final String methodName,
                               Map<Class<? extends Throwable>, BiFunction<WebDriver, Object[], Object>> exceptionHandlingMap) {
        this.methodName = methodName;
        this.exceptionHandlingMap = exceptionHandlingMap;
    }

}
