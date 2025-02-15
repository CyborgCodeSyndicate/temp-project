package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import javax.swing.text.html.HTML;

public class ValidationServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final SmartWebDriver driver;


    public ValidationServiceFluent(T uiServiceFluent, SmartWebDriver webDriver) {
        this.uiServiceFluent = uiServiceFluent;
        driver = webDriver;
    }


    public T validateTextInField(HTML.Tag tag, String text) {
        return validateTextInField(tag, text, false);
    }


    public T validateTextInField(HTML.Tag tag, String text, boolean soft) {
        By selector = By.xpath("//" + tag.toString() + "[contains(text(),'" + text + "')]");
        String description = String.format("Validate text: '%s' is present in tag: '%s'", text, tag);
        String errorMessage = String.format("Missing text: %s in tag: %s", text, tag);
        boolean condition = elementIsPresentAfterTime(selector, 2);
        validateTrue(condition, description, soft, errorMessage);
        return uiServiceFluent;
    }


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
