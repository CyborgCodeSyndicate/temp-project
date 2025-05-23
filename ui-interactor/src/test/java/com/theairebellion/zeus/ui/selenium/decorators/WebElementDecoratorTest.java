package com.theairebellion.zeus.ui.selenium.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebElementDecoratorTest {

    @Mock
    private WebElement mockWebElement;

    private TestWebElementDecorator decorator;

    // Create a concrete implementation of the abstract class for testing
    private static class TestWebElementDecorator extends WebElementDecorator {
        public TestWebElementDecorator(WebElement original) {
            super(original);
        }
    }

    @BeforeEach
    void setUp() {
        decorator = new TestWebElementDecorator(mockWebElement);
    }

    @Test
    @DisplayName("getOriginal() should return the exact same WebElement instance that was wrapped")
    void shouldReturnOriginalWebElement() {
        // When getting the original WebElement
        WebElement result = decorator.getOriginal();

        // Then it should return the same instance
        assertSame(mockWebElement, result);
    }

    @Test
    @DisplayName("click() should delegate to the original WebElement's click method")
    void shouldDelegateClickMethod() {
        // When
        decorator.click();

        // Then
        verify(mockWebElement).click();
    }

    @Test
    @DisplayName("submit() should delegate to the original WebElement's submit method")
    void shouldDelegateSubmitMethod() {
        // When
        decorator.submit();

        // Then
        verify(mockWebElement).submit();
    }

    @Test
    @DisplayName("sendKeys() should delegate to the original WebElement's sendKeys method with provided input")
    void shouldDelegateSendKeysMethod() {
        // Given
        CharSequence[] keysToSend = new CharSequence[]{"test input"};

        // When
        decorator.sendKeys(keysToSend);

        // Then
        verify(mockWebElement).sendKeys(keysToSend);
    }

    @Test
    @DisplayName("clear() should delegate to the original WebElement's clear method")
    void shouldDelegateClearMethod() {
        // When
        decorator.clear();

        // Then
        verify(mockWebElement).clear();
    }

    @Test
    @DisplayName("getTagName() should delegate to original WebElement and return its tag name")
    void shouldDelegateGetTagNameMethod() {
        // Given
        String expectedTagName = "div";
        when(mockWebElement.getTagName()).thenReturn(expectedTagName);

        // When
        String result = decorator.getTagName();

        // Then
        assertEquals(expectedTagName, result);
        verify(mockWebElement).getTagName();
    }

    @Test
    @DisplayName("getAttribute() should delegate to original WebElement and return the requested attribute value")
    void shouldDelegateGetAttributeMethod() {
        // Given
        String attributeName = "id";
        String expectedValue = "element-id";
        when(mockWebElement.getAttribute(attributeName)).thenReturn(expectedValue);

        // When
        String result = decorator.getAttribute(attributeName);

        // Then
        assertEquals(expectedValue, result);
        verify(mockWebElement).getAttribute(attributeName);
    }

    @Test
    @DisplayName("isSelected() should delegate to original WebElement and return its selected state")
    void shouldDelegateIsSelectedMethod() {
        // Given
        when(mockWebElement.isSelected()).thenReturn(true);

        // When
        boolean result = decorator.isSelected();

        // Then
        assertTrue(result);
        verify(mockWebElement).isSelected();
    }

    @Test
    @DisplayName("isEnabled() should delegate to original WebElement and return its enabled state")
    void shouldDelegateIsEnabledMethod() {
        // Given
        when(mockWebElement.isEnabled()).thenReturn(true);

        // When
        boolean result = decorator.isEnabled();

        // Then
        assertTrue(result);
        verify(mockWebElement).isEnabled();
    }

    @Test
    @DisplayName("getText() should delegate to original WebElement and return its text content")
    void shouldDelegateGetTextMethod() {
        // Given
        String expectedText = "Element text";
        when(mockWebElement.getText()).thenReturn(expectedText);

        // When
        String result = decorator.getText();

        // Then
        assertEquals(expectedText, result);
        verify(mockWebElement).getText();
    }

    @Test
    @DisplayName("findElements() should delegate to original WebElement and return list of child elements")
    void shouldDelegateFindElementsMethod() {
        // Given
        By by = By.className("child");
        WebElement childElement = mock(WebElement.class);
        List<WebElement> expectedElements = List.of(childElement);
        when(mockWebElement.findElements(by)).thenReturn(expectedElements);

        // When
        List<WebElement> result = decorator.findElements(by);

        // Then
        assertEquals(expectedElements, result);
        verify(mockWebElement).findElements(by);
    }

    @Test
    @DisplayName("findElement() should delegate to original WebElement and return the first matching child element")
    void shouldDelegateFindElementMethod() {
        // Given
        By by = By.id("childId");
        WebElement expectedElement = mock(WebElement.class);
        when(mockWebElement.findElement(by)).thenReturn(expectedElement);

        // When
        WebElement result = decorator.findElement(by);

        // Then
        assertEquals(expectedElement, result);
        verify(mockWebElement).findElement(by);
    }

    @Test
    @DisplayName("isDisplayed() should delegate to original WebElement and return its visibility state")
    void shouldDelegateIsDisplayedMethod() {
        // Given
        when(mockWebElement.isDisplayed()).thenReturn(false);

        // When
        boolean result = decorator.isDisplayed();

        // Then
        assertFalse(result);
        verify(mockWebElement).isDisplayed();
    }

    @Test
    @DisplayName("getLocation() should delegate to original WebElement and return its position coordinates")
    void shouldDelegateGetLocationMethod() {
        // Given
        Point expectedLocation = new Point(10, 20);
        when(mockWebElement.getLocation()).thenReturn(expectedLocation);

        // When
        Point result = decorator.getLocation();

        // Then
        assertEquals(expectedLocation, result);
        verify(mockWebElement).getLocation();
    }

    @Test
    @DisplayName("getSize() should delegate to original WebElement and return its dimension")
    void shouldDelegateGetSizeMethod() {
        // Given
        Dimension expectedSize = new Dimension(100, 200);
        when(mockWebElement.getSize()).thenReturn(expectedSize);

        // When
        Dimension result = decorator.getSize();

        // Then
        assertEquals(expectedSize, result);
        verify(mockWebElement).getSize();
    }

    @Test
    @DisplayName("getCssValue() should delegate to original WebElement and return the CSS property value")
    void shouldDelegateGetCssValueMethod() {
        // Given
        String propertyName = "color";
        String expectedValue = "rgb(255, 0, 0)";
        when(mockWebElement.getCssValue(propertyName)).thenReturn(expectedValue);

        // When
        String result = decorator.getCssValue(propertyName);

        // Then
        assertEquals(expectedValue, result);
        verify(mockWebElement).getCssValue(propertyName);
    }

    @Test
    @DisplayName("getRect() should delegate to original WebElement and return its rectangle dimensions")
    void shouldDelegateGetRectMethod() {
        // Given
        Rectangle expectedRect = new Rectangle(10, 20, 100, 200);
        when(mockWebElement.getRect()).thenReturn(expectedRect);

        // When
        Rectangle result = decorator.getRect();

        // Then
        assertEquals(expectedRect, result);
        verify(mockWebElement).getRect();
    }

    @Test
    @DisplayName("getScreenshotAs() should delegate to original WebElement and return screenshot in specified format")
    void shouldDelegateGetScreenshotAsMethod() {
        // Given
        OutputType<String> outputType = OutputType.BASE64;
        String expectedScreenshot = "base64screenshot";
        when(mockWebElement.getScreenshotAs(outputType)).thenReturn(expectedScreenshot);

        // When
        String result = decorator.getScreenshotAs(outputType);

        // Then
        assertEquals(expectedScreenshot, result);
        verify(mockWebElement).getScreenshotAs(outputType);
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when original WebElement is null")
    void shouldThrowExceptionWhenOriginalIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TestWebElementDecorator(null));
        assertEquals("original WebElement must not be null", exception.getMessage());
    }

    @Test
    @DisplayName("sendKeys() should handle empty input array")
    void shouldHandleEmptySendKeys() {
        decorator.sendKeys();
        verify(mockWebElement).sendKeys();
    }

    @Test
    @DisplayName("getAttribute() should handle null return value")
    void shouldHandleNullAttribute() {
        when(mockWebElement.getAttribute("missing")).thenReturn(null);
        assertNull(decorator.getAttribute("missing"));
    }
}