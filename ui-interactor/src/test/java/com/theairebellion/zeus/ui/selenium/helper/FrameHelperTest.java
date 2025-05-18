package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FrameHelperTest extends BaseUnitUITest {

    @Test
    @DisplayName("Should find element by locator directly")
    void findElementInIFrames_ByLocator_ElementFoundDirectly() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement element = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        when(driver.findElements(any())).thenReturn(List.of(element));
        SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

        // Then
        assertNotNull(result);
    }


    @Test
    @DisplayName("Should find element in iframe by locator when iframes are switched")
    void findElementInIFrames_ByLocator_ElementNotFoundDirectlyButInFrame() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement element = mock(WebElement.class);
        WebElement frameElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        List<WebElement> frames = new ArrayList<>();
        frames.add(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        when(driver.findElements(locator)).thenReturn(List.of()).thenReturn(List.of(element));
        SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

        // Then
        assertNotNull(result);
    }


    @Test
    @DisplayName("Element not found in iframes by locator")
    void findElementInIFrames_ByLocator_ElementNotFound() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement frameElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        List<WebElement> frames = new ArrayList<>();
        frames.add(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

        // Assert
        assertNull(result);
        verify(driver, times(3)).switchTo();
    }


    @Test
    @DisplayName("Should find element in iframe by webElement")
    void findElementInIFrames_ByWebElement_ElementFoundInFrame() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        WebElement element = mock(WebElement.class);
        WebElement frameElement = mock(WebElement.class);
        WebElement foundElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        List<WebElement> frames = new ArrayList<>();
        frames.add(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        when(element.getTagName()).thenReturn("div");
        when(element.getText()).thenReturn("test text");
        when(((JavascriptExecutor) driver).executeScript(anyString(), any())).thenReturn("[@id='test']");
        when(foundElement.getText()).thenReturn("test text");
        List<WebElement> matchingElements = new ArrayList<>();
        matchingElements.add(foundElement);
        when(driver.findElements(any(By.class))).thenReturn(matchingElements);
        SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

        // Then
        assertNotNull(result);
    }


    @Test
    @DisplayName("Element not found in iframes by webElement")
    void findElementInIFrames_ByWebElement_ElementNotFound() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        WebElement element = mock(WebElement.class);
        WebElement frameElement = mock(WebElement.class);
        WebElement nonMatchingElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        List<WebElement> frames = new ArrayList<>();
        frames.add(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        when(element.getTagName()).thenReturn("div");
        when(element.getText()).thenReturn("test text");
        when(((JavascriptExecutor) driver).executeScript(anyString(), any())).thenReturn("[@id='test']");
        when(nonMatchingElement.getText()).thenReturn("different text");
        List<WebElement> elements = new ArrayList<>();
        elements.add(nonMatchingElement);
        when(driver.findElements(any(By.class))).thenReturn(elements);
        SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

        // Then
        assertNull(result);
        verify(driver, times(2)).switchTo();
    }


    @Test
    @DisplayName("Element not found in iframes by webElement, exception during attributes collection")
    void findElementInIFrames_ByWebElement_ExceptionDuringAttributeCollection() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        WebElement element = mock(WebElement.class);
        WebElement frameElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(targetLocator.defaultContent()).thenReturn(driver);
        List<WebElement> frames = new ArrayList<>();
        frames.add(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        when(((JavascriptExecutor) driver).executeScript(anyString(), any()))
                .thenThrow(new JavascriptException("JS Error"));
        when(element.getTagName()).thenReturn("div");
        SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

        // Then
        assertNull(result);
        verify(driver, times(3)).switchTo();
    }

    @Test
    @DisplayName("findContainerIFrame should return containing iframe when element exists in first iframe")
    void findContainerIFrame_ElementInFirstIframe() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement frameElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(driver.findElements(locator))
                .thenReturn(List.of(mock(WebElement.class)));
        List<WebElement> frames = List.of(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        WebElement result = FrameHelper.findContainerIframe(driver, locator);

        // Assert
        assertEquals(frameElement, result);
        verify(driver, times(1)).switchTo();
    }

    @Test
    @DisplayName("findContainerIFrame should return null when element not found in any iframe")
    void findContainerIframe_ElementNotFound() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement frameElement = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(driver.findElements(locator)).thenReturn(List.of());
        List<WebElement> frames = List.of(frameElement);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        WebElement result = FrameHelper.findContainerIframe(driver, locator);

        // Then
        assertNull(result);
        verify(driver, times(2)).switchTo();
    }

    @Test
    @DisplayName("findContainerIFrame should return correct iframe when element exists in second iframe")
    void findContainerIFrame_ElementInSecondIframe() {
        // Given
        WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        By locator = By.id("testId");
        WebElement frame1 = mock(WebElement.class);
        WebElement frame2 = mock(WebElement.class);

        // When
        when(driver.switchTo()).thenReturn(targetLocator);
        when(driver.findElements(locator))
                .thenReturn(List.of()) // Not in first iframe
                .thenReturn(List.of(mock(WebElement.class))); // Found in second iframe

        List<WebElement> frames = List.of(frame1, frame2);
        when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);
        WebElement result = FrameHelper.findContainerIframe(driver, locator);

        // Then
        assertEquals(frame2, result);
        verify(driver, times(3)).switchTo();
    }

}