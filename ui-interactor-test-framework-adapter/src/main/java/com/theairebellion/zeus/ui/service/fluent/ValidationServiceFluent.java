package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import javax.swing.text.html.HTML;

/**
 * Provides fluent validation methods for verifying text presence in UI fields.
 * This class allows performing soft and hard assertions on text validation inside HTML elements.
 *
 * @param <T> The generic type extending {@link UIServiceFluent} for fluent chaining.
 *
 * @author Cyborg Code Syndicate
 */
public class ValidationServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final SmartWebDriver driver;

    /**
     * Constructs a {@code ValidationServiceFluent} instance.
     *
     * @param uiServiceFluent The fluent UI service instance.
     * @param webDriver       The instance of {@link SmartWebDriver} used for validation.
     */
    public ValidationServiceFluent(T uiServiceFluent, SmartWebDriver webDriver) {
        this.uiServiceFluent = uiServiceFluent;
        driver = webDriver;
    }

    /**
     * Validates that the specified text is present within a given HTML tag.
     * This method performs a hard assertion by default.
     *
     * @param tag  The {@link HTML.Tag} representing the HTML element to validate.
     * @param text The text expected to be present within the specified tag.
     * @return The fluent UI service instance.
     */
    public T validateTextInField(HTML.Tag tag, String text) {
        return validateTextInField(tag, text, false);
    }

    /**
     * Validates that the specified text is present within a given HTML tag.
     * Supports both hard and soft assertions based on the provided flag.
     *
     * @param tag  The {@link HTML.Tag} representing the HTML element to validate.
     * @param text The text expected to be present within the specified tag.
     * @param soft A boolean indicating whether the validation should be performed softly.
     *             If {@code true}, failures will be collected rather than throwing an exception immediately.
     * @return The fluent UI service instance.
     */
    public T validateTextInField(HTML.Tag tag, String text, boolean soft) {
        By selector = By.xpath("//" + tag.toString() + "[contains(text(),'" + text + "')]");
        String description = String.format("Validate text: '%s' is present in tag: '%s'", text, tag);
        String errorMessage = String.format("Missing text: %s in tag: %s", text, tag);
        boolean condition = elementIsPresentAfterTime(selector, 2);
        validateTrue(condition, description, soft, errorMessage);
        return uiServiceFluent;
    }

    /**
     * Waits for an element to be present within a specified timeout duration.
     *
     * @param locator The {@link By} locator of the element.
     * @param seconds The number of seconds to wait before checking.
     * @return {@code true} if the element is found within the given time, otherwise {@code false}.
     */
    private boolean elementIsPresentAfterTime(By locator, int seconds) {
        driver.waitUntilElementIsShown(locator, seconds);
        return driver.checkNoException(() -> driver.findSmartElement(locator));
    }


    private void validateTrue(boolean condition, String description, boolean soft, String errorMessage) {
        if (!soft) {
            uiServiceFluent.validate(() -> Assertions.assertThat(condition)
                    .as(description)
                    .withFailMessage(errorMessage)
                    .isTrue());
        } else {
            uiServiceFluent.validate(softAssertions -> softAssertions.assertThat(condition)
                    .as(description)
                    .withFailMessage(errorMessage)
                    .isTrue());
        }
    }

}
