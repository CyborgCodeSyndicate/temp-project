package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;

import java.util.Objects;

/**
 * A fluent service class for handling navigation-related actions in a web automation framework.
 * Provides methods to navigate between pages, handle browser tabs/windows, interact with frames, and manage alerts.
 *
 * @param <T> The type of the UI service fluent interface.
 *
 * @author Cyborg Code Syndicate
 */
public class NavigationServiceFluent<T extends UIServiceFluent<?>> {

    private final T uiServiceFluent;
    private final SmartWebDriver driver;

    /**
     * Constructs a new {@code NavigationServiceFluent} instance.
     *
     * @param uiServiceFluent The parent fluent UI service instance.
     * @param webDriver       The smart web driver used for navigation actions.
     */
    public NavigationServiceFluent(T uiServiceFluent, SmartWebDriver webDriver) {
        this.uiServiceFluent = uiServiceFluent;
        this.driver = webDriver;
    }

    /**
     * Navigates to the specified URL and maximizes the browser window.
     *
     * @param url The URL to navigate to.
     * @return The fluent UI service instance.
     */
    public T navigate(String url) {
        driver.manage().window().maximize();
        driver.get(url);
        return uiServiceFluent;
    }

    /**
     * Navigates back in the browser history.
     *
     * @return The fluent UI service instance.
     */
    public T back() {
        driver.navigate().back();
        return uiServiceFluent;
    }

    /**
     * Navigates forward in the browser history.
     *
     * @return The fluent UI service instance.
     */
    public T forward() {
        driver.navigate().forward();
        return uiServiceFluent;
    }

    /**
     * Refreshes the current page.
     *
     * @return The fluent UI service instance.
     */
    public T refresh() {
        driver.navigate().refresh();
        return uiServiceFluent;
    }

    /**
     * Switches to a newly opened browser tab.
     *
     * @return The fluent UI service instance.
     */
    public T switchToNewTab() {
        String currentHandle = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return uiServiceFluent;
    }

    /**
     * Switches to a browser window with the specified title.
     *
     * @param windowTitle The title of the target window.
     * @return The fluent UI service instance.
     * @throws NoSuchWindowException if no window with the given title is found.
     */
    public T switchToWindow(String windowTitle) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (Objects.equals(driver.getTitle(), windowTitle)) {
                return uiServiceFluent;
            }
        }
        throw new NoSuchWindowException("No window found with title: " + windowTitle);
    }

    /**
     * Closes the current browser tab and switches to another available tab if present.
     *
     * @return The fluent UI service instance.
     */
    public T closeCurrentTab() {
        driver.close();
        if (!driver.getWindowHandles().isEmpty()) {
            driver.switchTo().window(driver.getWindowHandles().iterator().next());
        }
        return uiServiceFluent;
    }

    /**
     * Switches to an iframe using its index.
     *
     * @param index The index of the iframe.
     * @return The fluent UI service instance.
     */
    public T switchToFrameByIndex(int index) {
        driver.switchTo().frame(index);
        return uiServiceFluent;
    }

    /**
     * Switches to an iframe using its name or ID.
     *
     * @param nameOrId The name or ID of the iframe.
     * @return The fluent UI service instance.
     */
    public T switchToFrameByNameOrId(String nameOrId) {
        driver.switchTo().frame(nameOrId);
        return uiServiceFluent;
    }

    /**
     * Switches to the parent frame of the current iframe.
     *
     * @return The fluent UI service instance.
     */
    public T switchToParentFrame() {
        driver.switchTo().parentFrame();
        return uiServiceFluent;
    }

    /**
     * Switches back to the default content from an iframe.
     *
     * @return The fluent UI service instance.
     */
    public T switchToDefaultContent() {
        driver.switchTo().defaultContent();
        return uiServiceFluent;
    }

    /**
     * Accepts an alert pop-up.
     *
     * @return The fluent UI service instance.
     */
    public T acceptAlert() {
        driver.switchTo().alert().accept();
        return uiServiceFluent;
    }

    /**
     * Dismisses an alert pop-up.
     *
     * @return The fluent UI service instance.
     */
    public T dismissAlert() {
        driver.switchTo().alert().dismiss();
        return uiServiceFluent;
    }

    /**
     * Retrieves the text of an alert pop-up.
     *
     * @return The text of the alert.
     */
    private String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    /**
     * Validates that the alert text matches the expected value.
     *
     * @param expected The expected alert text.
     * @return The fluent UI service instance.
     */
    public T validateAlertText(String expected) {
        return validateAlertText(expected, false);
    }

    /**
     * Validates that the alert text matches the expected value, with an optional soft assertion.
     *
     * @param expected The expected alert text.
     * @param soft     If {@code true}, the validation will be performed softly.
     * @return The fluent UI service instance.
     */
    public T validateAlertText(String expected, boolean soft) {
        String alertText = getAlertText();
        if (soft) {
            return (T) uiServiceFluent.validate(
                    softAssertions -> softAssertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        } else {
            return (T) uiServiceFluent.validate(
                    () -> Assertions.assertThat(alertText).as("Validating Alert text")
                            .isEqualTo(expected));
        }
    }

    /**
     * Opens a new browser tab using JavaScript and switches to it.
     *
     * @return The fluent UI service instance.
     */
    public T openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open();");
        return switchToNewTab();
    }

}
