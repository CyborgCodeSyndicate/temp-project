package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.decorators.WebElementDecorator;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * A custom wrapper for {@link WebElement} that enhances interactions by adding wait mechanisms,
 * exception handling, and support for Shadow DOM elements.
 *
 * <p>It integrates with Selenium while providing additional functionality such as smart waiting,
 * exception management, and Shadow DOM compatibility.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class SmartWebElement extends WebElementDecorator {

    @Getter
    @Setter
    private WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Constructs a {@code SmartWebElement} wrapping the given {@link WebElement}.
     *
     * @param original The original {@link WebElement} instance.
     * @param driver   The {@link WebDriver} instance.
     */
    public SmartWebElement(WebElement original, WebDriver driver) {
        super(original);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getUiConfig().waitDuration()));
    }

    /**
     * Finds multiple {@link SmartWebElement}s matching the given {@link By} locator.
     *
     * @param by The {@link By} locator used to find elements.
     * @return A list of {@link SmartWebElement}, or an empty list if no elements are found.
     */
    @HandleUIException
    public List<SmartWebElement> findSmartElements(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return SmartFinder.findElementsNoWrap(this, by);
        }

        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementsWithShadowRootElement(this, by, this::waitWithoutFailure);
            } else {
                return SmartFinder.findElementsNormally(this, by, this::waitWithoutFailure);
            }
        } catch (Exception e) {
            return handleException("findElements", e, new Object[]{by});
        }
    }

    /**
     * Finds a single {@link SmartWebElement} using the given {@link By} locator.
     *
     * @param by The {@link By} locator used to find the element.
     * @return The found {@link SmartWebElement}, or handles the exception if the element is not found.
     */
    @HandleUIException
    public SmartWebElement findSmartElement(By by) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            return SmartFinder.findElementNoWrap(this, by);
        }
        try {
            if (getUiConfig().useShadowRoot()) {
                return SmartFinder.findElementWithShadowRootElement(this, by, this::waitWithoutFailure);
            } else {
                return SmartFinder.findElementNormally(this, by, this::waitWithoutFailure);
            }
        } catch (Exception e) {
            return handleException("findElement", e, new Object[]{by});
        }
    }

    /**
     * Clicks the element, ensuring it is clickable before performing the action.
     */
    @Override
    @HandleUIException
    public void click() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.click();
        }
        performActionWithWait(element -> super.click());
    }

    /**
     * Performs a double-click action on the element.
     */
    @HandleUIException
    public void doubleClick() {
        Actions actions = new Actions(driver);
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            actions.doubleClick();
        }
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            actions.doubleClick();
        } catch (Exception e) {
            handleException("doubleClick", e, new Object[0]);
        }
    }

    /**
     * Clears the text inside the element, ensuring it is interactable.
     */
    @Override
    @HandleUIException
    public void clear() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.clear();
        }
        performActionWithWait(element -> super.clear());
    }

    /**
     * Sends keystrokes to the element, ensuring it is interactable.
     *
     * @param keysToSend The sequence of keys to send.
     */
    @Override
    @NullMarked
    public void sendKeys(CharSequence... keysToSend) {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.sendKeys(keysToSend);
        }
        performActionWithWait(element -> super.sendKeys(keysToSend));
    }

    /**
     * Submits the form associated with this element.
     */
    @Override
    public void submit() {
        if (!getUiConfig().useWrappedSeleniumFunctions()) {
            super.submit();
        }
        performActionWithWait(element -> super.submit());
    }

    /**
     * Clears the element and then sends the specified keys.
     *
     * @param keysToSend The sequence of keys to send.
     */
    public void clearAndSendKeys(CharSequence... keysToSend) {
        clear();
        sendKeys(keysToSend);
    }

    /**
     * Checks whether the element is enabled and visible.
     *
     * @return {@code true} if the element is enabled and visible, otherwise {@code false}.
     */
    public boolean isEnabledAndVisible() {
        waitWithoutFailure(ExpectedConditions.and(
                ExpectedConditions.visibilityOf(this),
                ExpectedConditions.elementToBeClickable(this)
        ));
        return true;
    }

    /**
     * Handles exceptions by searching for predefined exception handling strategies.
     *
     * @param methodName The method where the exception occurred.
     * @param exception  The thrown exception.
     * @param params     The method parameters.
     * @return The handled result or rethrows the exception if no strategy is found.
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T> T handleException(String methodName, Exception exception, Object[] params) {
        Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

        Optional<ExceptionHandlingWebElement> exceptionHandlingOptional =
                Arrays.stream(ExceptionHandlingWebElement.values())
                        .filter(enumVal ->
                                enumVal.getMethodName().equals(methodName)
                                        && Objects.nonNull(enumVal.getExceptionHandlingMap().get(cause.getClass()))
                        )
                        .findFirst();

        if (exceptionHandlingOptional.isPresent()) {
            return (T) exceptionHandlingOptional.get()
                    .getExceptionHandlingMap()
                    .get(cause.getClass())
                    .apply(driver, this, exception, params);
        } else {
            String locator = (params.length > 0 && params[0] instanceof By) ? params[0].toString() : "Unknown locator";
            String exceptionMessage = exception.getClass().getSimpleName();

            String errorMessage = String.format(
                    "[BROKEN] Exception handling failed for method '%s'. Exception: '%s'. Parameters: Locator - '%s'.",
                    methodName, exceptionMessage, locator
            );
            LogUI.error(errorMessage);
            throw exception;
        }
    }

    /**
     * Returns the string representation of the original WebElement.
     *
     * @return The string representation of the wrapped WebElement.
     */
    @Override
    public String toString() {
        return original.toString();
    }

    /**
     * Executes a waiting condition while suppressing failures.
     *
     * @param expectedConditions The condition to wait for.
     */
    private <T> void waitWithoutFailure(Function<WebDriver, T> expectedConditions) {
        try {
            wait.until(expectedConditions);
        } catch (Exception ignore) {
        }
    }

    /**
     * Performs an action on the element after waiting for it to become clickable.
     *
     * @param action The action to perform.
     */
    private void performActionWithWait(Consumer<SmartWebElement> action) {
        try {
            waitWithoutFailure(ExpectedConditions.elementToBeClickable(this));
            action.accept(this);
        } catch (Exception e) {
            handleException(action.toString(), e, new Object[0]);
        }
    }

    /**
     * Waits until an element's attribute value changes from its initial value.
     *
     * @param attributeName         The name of the attribute.
     * @param initialAttributeValue The initial value of the attribute.
     */
    public void waitUntilAttributeValueIsChanged(String attributeName, String initialAttributeValue) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            customWait.until(attributeValueChanged(attributeName, initialAttributeValue));
        } catch (Exception ignore) {
        }
    }


    private ExpectedCondition<Boolean> attributeValueChanged(final String attributeName, final String initialValue) {
        return driver -> {
            String currentValue = getAttribute(attributeName);
            return !initialValue.equals(currentValue);
        };
    }

}
