package com.theairebellion.zeus.ui.selenium.decorators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebDriverDecoratorTest {

    @Mock
    private WebDriver mockWebDriver;

    @Mock
    private WebElement mockWebElement;

    private TestWebDriverDecorator decorator;

    // Create a concrete implementation of the abstract class for testing
    private static class TestWebDriverDecorator extends WebDriverDecorator {
        public TestWebDriverDecorator(WebDriver original) {
            super(original);
        }
    }

    @BeforeEach
    void setUp() {
        decorator = new TestWebDriverDecorator(mockWebDriver);
    }

    @Test
    void shouldReturnOriginalWebDriver() {
        // When getting the original WebDriver
        WebDriver result = decorator.getOriginal();

        // Then it should return the same instance
        assertSame(mockWebDriver, result);
    }

    @Test
    void shouldDelegateGetMethod() {
        // Given
        String url = "https://example.com";

        // When
        decorator.get(url);

        // Then
        verify(mockWebDriver).get(url);
    }

    @Test
    void shouldDelegateFindElementMethod() {
        // Given
        By by = By.id("testId");
        when(mockWebDriver.findElement(by)).thenReturn(mockWebElement);

        // When
        WebElement result = decorator.findElement(by);

        // Then
        assertEquals(mockWebElement, result);
        verify(mockWebDriver).findElement(by);
    }

    @Test
    void shouldDelegateFindElementsMethod() {
        // Given
        By by = By.className("testClass");
        List<WebElement> elements = List.of(mockWebElement);
        when(mockWebDriver.findElements(by)).thenReturn(elements);

        // When
        List<WebElement> result = decorator.findElements(by);

        // Then
        assertEquals(elements, result);
        verify(mockWebDriver).findElements(by);
    }

    @Test
    void shouldDelegateGetCurrentUrlMethod() {
        // Given
        String expectedUrl = "https://example.com/page";
        when(mockWebDriver.getCurrentUrl()).thenReturn(expectedUrl);

        // When
        String result = decorator.getCurrentUrl();

        // Then
        assertEquals(expectedUrl, result);
        verify(mockWebDriver).getCurrentUrl();
    }

    @Test
    void shouldDelegateGetTitleMethod() {
        // Given
        String expectedTitle = "Example Page";
        when(mockWebDriver.getTitle()).thenReturn(expectedTitle);

        // When
        String result = decorator.getTitle();

        // Then
        assertEquals(expectedTitle, result);
        verify(mockWebDriver).getTitle();
    }

    @Test
    void shouldDelegateGetWindowHandleMethod() {
        // Given
        String expectedHandle = "window1";
        when(mockWebDriver.getWindowHandle()).thenReturn(expectedHandle);

        // When
        String result = decorator.getWindowHandle();

        // Then
        assertEquals(expectedHandle, result);
        verify(mockWebDriver).getWindowHandle();
    }

    @Test
    void shouldDelegateGetWindowHandlesMethod() {
        // Given
        Set<String> expectedHandles = Set.of("window1", "window2");
        when(mockWebDriver.getWindowHandles()).thenReturn(expectedHandles);

        // When
        Set<String> result = decorator.getWindowHandles();

        // Then
        assertEquals(expectedHandles, result);
        verify(mockWebDriver).getWindowHandles();
    }

    @Test
    void shouldDelegateCloseMethod() {
        // When
        decorator.close();

        // Then
        verify(mockWebDriver).close();
    }

    @Test
    void shouldDelegateQuitMethod() {
        // When
        decorator.quit();

        // Then
        verify(mockWebDriver).quit();
    }

    @Test
    void shouldDelegateSwitchToMethod() {
        // Given
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        when(mockWebDriver.switchTo()).thenReturn(targetLocator);

        // When
        WebDriver.TargetLocator result = decorator.switchTo();

        // Then
        assertEquals(targetLocator, result);
        verify(mockWebDriver).switchTo();
    }

    @Test
    void shouldDelegateNavigateMethod() {
        // Given
        WebDriver.Navigation navigation = mock(WebDriver.Navigation.class);
        when(mockWebDriver.navigate()).thenReturn(navigation);

        // When
        WebDriver.Navigation result = decorator.navigate();

        // Then
        assertEquals(navigation, result);
        verify(mockWebDriver).navigate();
    }

    @Test
    void shouldDelegateManageMethod() {
        // Given
        WebDriver.Options options = mock(WebDriver.Options.class);
        when(mockWebDriver.manage()).thenReturn(options);

        // When
        WebDriver.Options result = decorator.manage();

        // Then
        assertEquals(options, result);
        verify(mockWebDriver).manage();
    }
}