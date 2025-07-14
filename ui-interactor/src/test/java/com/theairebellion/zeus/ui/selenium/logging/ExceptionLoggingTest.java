package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionLoggingTest {

    @Test
    @DisplayName("Verify all enum constants have correct target classes")
    void verifyTargetClasses() {
        assertEquals(WebDriver.class, ExceptionLogging.FIND_ELEMENT_FROM_ROOT.getTargetClass());
        assertEquals(WebDriver.class, ExceptionLogging.FIND_ELEMENTS_FROM_ROOT.getTargetClass());
        assertEquals(WebElement.class, ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT.getTargetClass());
        assertEquals(WebElement.class, ExceptionLogging.FIND_ELEMENTS_FROM_ELEMENT.getTargetClass());
        assertEquals(WebElement.class, ExceptionLogging.CLICK.getTargetClass());
        assertEquals(WebElement.class, ExceptionLogging.SEND_KEYS.getTargetClass());
        assertEquals(WebElement.class, ExceptionLogging.SUBMIT.getTargetClass());
    }

    @Test
    @DisplayName("Verify all enum constants have correct actions")
    void verifyActions() {
        assertEquals(WebElementAction.FIND_ELEMENT, ExceptionLogging.FIND_ELEMENT_FROM_ROOT.getAction());
        assertEquals(WebElementAction.FIND_ELEMENTS, ExceptionLogging.FIND_ELEMENTS_FROM_ROOT.getAction());
        assertEquals(WebElementAction.FIND_ELEMENT, ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT.getAction());
        assertEquals(WebElementAction.FIND_ELEMENTS, ExceptionLogging.FIND_ELEMENTS_FROM_ELEMENT.getAction());
        assertEquals(WebElementAction.CLICK, ExceptionLogging.CLICK.getAction());
        assertEquals(WebElementAction.SEND_KEYS, ExceptionLogging.SEND_KEYS.getAction());
        assertEquals(WebElementAction.SUBMIT, ExceptionLogging.SUBMIT.getAction());
    }
}