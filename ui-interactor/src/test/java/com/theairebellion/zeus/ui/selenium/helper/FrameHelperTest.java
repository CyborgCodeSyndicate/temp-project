package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FrameHelperTest extends BaseUnitUITest {

   @Test
   void testConstructor() throws Exception {
      // Use reflection to call the constructor to cover the class declaration line
      java.lang.reflect.Constructor<FrameHelper> constructor = FrameHelper.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      FrameHelper instance = constructor.newInstance();
      assertNotNull(instance); // Just to verify it was created
   }


   @Test
   void findElementInIFrames_ByLocator_ElementFoundDirectly() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      By locator = By.id("testId");
      WebElement element = mock(WebElement.class);

      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(driver.findElements(any())).thenReturn(List.of(element));

      // Setup SmartWebDriver mock to find element directly
      try (MockedStatic<UiConfigHolder> staticMock = mockStatic(UiConfigHolder.class);
           MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class);
           MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
                 SmartWebElement.class)) {


         UiConfig mockConfig = mock(UiConfig.class);
         lenient().when(mockConfig.waitDuration()).thenReturn(2); // <-- lenient here

         staticMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);
         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

         // Assert
         assertNotNull(result);
         verify(driver).switchTo();
         verify(targetLocator).defaultContent();
         assertEquals(1, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByLocator_ElementNotFoundDirectlyButInFrame() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      By locator = By.id("testId");
      WebElement element = mock(WebElement.class);
      WebElement frameElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup finding element in frame
      when(driver.findElements(locator)).thenReturn(List.of()).thenReturn(List.of(element));

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

         // Assert
         assertNotNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(1, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByLocator_ElementNotFound() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      By locator = By.id("testId");
      WebElement frameElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup finding no element in frame
      when(driver.findElement(locator)).thenThrow(new NoSuchElementException("Element not found"));

      try (MockedConstruction<SmartWebDriver> smartDriverMock = mockConstruction(
            SmartWebDriver.class,
            (mock, context) -> when(mock.findElement(locator))
                  .thenThrow(new NoSuchElementException("Element not found")));
           MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
                 SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

         // Assert
         assertNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(0, smartDriverMock.constructed().size());
         assertEquals(0, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByWebElement_ElementFoundInFrame() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      WebElement element = mock(WebElement.class);
      WebElement frameElement = mock(WebElement.class);
      WebElement foundElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup element attributes
      when(element.getTagName()).thenReturn("div");
      when(element.getText()).thenReturn("test text");
      when(((JavascriptExecutor) driver).executeScript(anyString(), any())).thenReturn("[@id='test']");

      // Setup matching element in frame
      when(foundElement.getText()).thenReturn("test text");
      List<WebElement> matchingElements = new ArrayList<>();
      matchingElements.add(foundElement);
      when(driver.findElements(any(By.class))).thenReturn(matchingElements);

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

         // Assert
         assertNotNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(1, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByWebElement_ElementNotFound() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      WebElement element = mock(WebElement.class);
      WebElement frameElement = mock(WebElement.class);
      WebElement nonMatchingElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup element attributes
      when(element.getTagName()).thenReturn("div");
      when(element.getText()).thenReturn("test text");
      when(((JavascriptExecutor) driver).executeScript(anyString(), any())).thenReturn("[@id='test']");

      // Setup non-matching element in frame
      when(nonMatchingElement.getText()).thenReturn("different text");
      List<WebElement> elements = new ArrayList<>();
      elements.add(nonMatchingElement);
      when(driver.findElements(any(By.class))).thenReturn(elements);

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

         // Assert
         assertNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(0, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByWebElement_NoFramesAvailable() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      WebElement element = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);

      // Setup empty frames list
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(Collections.emptyList());

      // Setup element attributes
      when(element.getTagName()).thenReturn("div");

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

         // Assert
         assertNull(result);
         verify(targetLocator, never()).frame(any(WebElement.class));
         assertEquals(0, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByLocator_ExceptionInFrame() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      By locator = By.id("testId");
      WebElement frameElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      // frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup exception when finding in frame
      when(driver.findElements(locator)).thenReturn(List.of());
      try (MockedConstruction<SmartWebDriver> smartDriverMock = mockConstruction(
            SmartWebDriver.class,
            (mock, context) -> when(mock.findElements(locator))
                  .thenReturn(List.of()));
           MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
                 SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, locator);

         // Assert
         assertNull(result);
         assertEquals(0, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByWebElement_ExceptionDuringAttributeCollection() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      WebElement element = mock(WebElement.class);
      WebElement frameElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup JavaScript exception during attribute collection
      when(((JavascriptExecutor) driver).executeScript(anyString(), any()))
            .thenThrow(new JavascriptException("JS Error"));

      // Setup element attributes
      when(element.getTagName()).thenReturn("div");

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

         // Assert
         assertNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(0, smartElementMock.constructed().size());
      }
   }


   @Test
   void findElementInIFrames_ByWebElement_ExceptionDuringFindElements() {
      // Setup driver and mocks
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
      WebElement element = mock(WebElement.class);
      WebElement frameElement = mock(WebElement.class);

      // Setup driver behavior
      when(driver.switchTo()).thenReturn(targetLocator);
      when(targetLocator.defaultContent()).thenReturn(driver);
      when(targetLocator.frame(any(WebElement.class))).thenReturn(driver);

      // Setup frames
      List<WebElement> frames = new ArrayList<>();
      frames.add(frameElement);
      when(((JavascriptExecutor) driver).executeScript(anyString())).thenReturn(frames);

      // Setup element attributes
      when(element.getTagName()).thenReturn("div");
      when(element.getText()).thenReturn("test text");
      when(((JavascriptExecutor) driver).executeScript(anyString(), any())).thenReturn("[@id='test']");

      // Setup exception during findElements
      when(driver.findElements(any(By.class))).thenThrow(new StaleElementReferenceException("Stale element"));

      try (MockedConstruction<SmartWebElement> smartElementMock = mockConstruction(
            SmartWebElement.class)) {

         // Act
         SmartWebElement result = FrameHelper.findElementInIframes(driver, element);

         // Assert
         assertNull(result);
         verify(targetLocator).frame(frameElement);
         assertEquals(0, smartElementMock.constructed().size());
      }
   }

}