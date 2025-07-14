package com.theairebellion.zeus.ui.selenium.enums;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebElementActionTest extends BaseUnitUITest {

    @Mock
    private WebDriver mockDriver;

    @Mock
    private WebElement mockElement;

    @BeforeEach
    void setUp() {
        // No additional setup needed
    }

    @Test
    @DisplayName("getMethodName() should return correct Selenium method names for all action types")
    void testGetMethodName() {
        assertEquals("findElement", WebElementAction.FIND_ELEMENT.getMethodName());
        assertEquals("findElements", WebElementAction.FIND_ELEMENTS.getMethodName());
        assertEquals("click", WebElementAction.CLICK.getMethodName());
        assertEquals("sendKeys", WebElementAction.SEND_KEYS.getMethodName());
        assertEquals("submit", WebElementAction.SUBMIT.getMethodName());
        assertEquals("clear", WebElementAction.CLEAR.getMethodName());
    }

    @Test
    @DisplayName("performAction() static method should execute FIND_ELEMENT and return SmartWebElement wrapper")
    void testPerformActionStaticMethod() {
        // We can't mock enum instances, but we can verify the behavior
        // by checking the result matches what the enum instance would return

        Object result = WebElementAction.performAction(mockDriver, mockElement, WebElementAction.FIND_ELEMENT);

        // Verify that result is a SmartWebElement wrapping our mockElement
        assertInstanceOf(SmartWebElement.class, result);
        assertEquals(mockElement, ((SmartWebElement) result).getOriginal());
    }

    @Test
    @DisplayName("FIND_ELEMENT.performActionWebElement() should wrap element in SmartWebElement")
    void testFindElementPerformActionWebElement() {
        // When
        Object result = WebElementAction.FIND_ELEMENT.performActionWebElement(mockDriver, mockElement);

        // Then
        assertInstanceOf(SmartWebElement.class, result);
        assertEquals(mockElement, ((SmartWebElement) result).getOriginal());
    }

    @Test
    @DisplayName("FIND_ELEMENT.performActionWebDriver() should wrap provided element in SmartWebElement")
    void testFindElementPerformActionWebDriver() {
        // Given
        WebElement element = mock(WebElement.class);

        // When
        Object result = WebElementAction.FIND_ELEMENT.performActionWebDriver(mockDriver, element);

        // Then
        assertInstanceOf(SmartWebElement.class, result);
        assertEquals(element, ((SmartWebElement) result).getOriginal());
    }

    @Test
    @DisplayName("FIND_ELEMENTS.performActionWebElement() should find and wrap child elements in SmartWebElement list")
    void testFindElementsPerformActionWebElement() {
        // Given
        By selector = By.id("testId");
        WebElement element1 = mock(WebElement.class);
        WebElement element2 = mock(WebElement.class);
        List<WebElement> elements = List.of(element1, element2);

        when(mockElement.findElements(selector)).thenReturn(elements);

        // When
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) WebElementAction.FIND_ELEMENTS.performActionWebElement(mockDriver, mockElement, selector);

        // Then
        assertEquals(2, result.size());
        verify(mockElement).findElements(selector);

        // Verify that all results are SmartWebElement instances
        for (int i = 0; i < result.size(); i++) {
            assertInstanceOf(SmartWebElement.class, result.get(i));
            assertEquals(elements.get(i), result.get(i).getOriginal());
        }
    }

    @Test
    @DisplayName("FIND_ELEMENTS.performActionWebDriver() should find and wrap elements in SmartWebElement list")
    void testFindElementsPerformActionWebDriver() {
        // Given
        By selector = By.id("testId");
        WebElement element1 = mock(WebElement.class);
        WebElement element2 = mock(WebElement.class);
        List<WebElement> elements = List.of(element1, element2);

        when(mockDriver.findElements(selector)).thenReturn(elements);

        // When
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) WebElementAction.FIND_ELEMENTS.performActionWebDriver(mockDriver, selector);

        // Then
        assertEquals(2, result.size());
        verify(mockDriver).findElements(selector);

        // Verify that all results are SmartWebElement instances
        for (int i = 0; i < result.size(); i++) {
            assertInstanceOf(SmartWebElement.class, result.get(i));
            assertEquals(elements.get(i), result.get(i).getOriginal());
        }
    }

    @Test
    @DisplayName("CLICK.performActionWebElement() should delegate click to element and return null")
    void testClickPerformActionWebElement() {
        // When
        Object result = WebElementAction.CLICK.performActionWebElement(mockDriver, mockElement);

        // Then
        assertNull(result);
        verify(mockElement).click();
    }

    @Test
    @DisplayName("CLICK.performActionWebDriver() should do nothing and return null (no element to click)")
    void testClickPerformActionWebDriver() {
        // When
        Object result = WebElementAction.CLICK.performActionWebDriver(mockDriver);

        // Then
        assertNull(result);
        verifyNoInteractions(mockDriver);
    }

    @Test
    @DisplayName("SEND_KEYS.performActionWebElement() should delegate text input to element and return null")
    void testSendKeysPerformActionWebElement() {
        // Given
        String text = "test text";

        // When
        Object result = WebElementAction.SEND_KEYS.performActionWebElement(mockDriver, mockElement, text);

        // Then
        assertNull(result);
        verify(mockElement).sendKeys(text);
    }

    @Test
    @DisplayName("SEND_KEYS.performActionWebDriver() should do nothing and return null (no element for input)")
    void testSendKeysPerformActionWebDriver() {
        // When
        Object result = WebElementAction.SEND_KEYS.performActionWebDriver(mockDriver);

        // Then
        assertNull(result);
        verifyNoInteractions(mockDriver);
    }

    @Test
    @DisplayName("SUBMIT.performActionWebElement() should delegate submit to element and return null")
    void testSubmitPerformActionWebElement() {
        // When
        Object result = WebElementAction.SUBMIT.performActionWebElement(mockDriver, mockElement);

        // Then
        assertNull(result);
        verify(mockElement).submit();
    }

    @Test
    @DisplayName("SUBMIT.performActionWebDriver() should do nothing and return null (no element to submit)")
    void testSubmitPerformActionWebDriver() {
        // When
        Object result = WebElementAction.SUBMIT.performActionWebDriver(mockDriver);

        // Then
        assertNull(result);
        verifyNoInteractions(mockDriver);
    }

    @Test
    @DisplayName("CLEAR.performActionWebElement() should delegate clear to element and return null")
    void testClearPerformActionWebElement() {
        // When
        Object result = WebElementAction.CLEAR.performActionWebElement(mockDriver, mockElement);

        // Then
        assertNull(result);
        verify(mockElement).clear();
    }

    @Test
    @DisplayName("CLEAR.performActionWebDriver() should do nothing and return null (no element to clear)")
    void testClearPerformActionWebDriver() {
        // When
        Object result = WebElementAction.CLEAR.performActionWebDriver(mockDriver);

        // Then
        assertNull(result);
        verifyNoInteractions(mockDriver);
    }

    @Test
    @DisplayName("FIND_ELEMENTS should handle empty list from WebDriver")
    void testFindElementsWithEmptyList() {
        // When
        when(mockDriver.findElements(any())).thenReturn(List.of());

        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>)
                WebElementAction.FIND_ELEMENTS.performActionWebDriver(mockDriver, By.id("test"));

        // Then
        assertTrue(result.isEmpty());
    }
}