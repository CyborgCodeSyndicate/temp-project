package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@Disabled
@DisplayName("NavigationServiceFluent Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NavigationServiceFluentTest extends BaseUnitUITest {

   private NavigationServiceFluent<UiServiceFluent<?>> sut;

   @Mock
   private UiServiceFluent<?> uiServiceFluent;

   @Mock
   private SmartWebDriver driver;

   @Mock
   private WebDriver.Navigation navigation;

   @Mock
   private WebDriver.TargetLocator targetLocator;

   @Mock
   private WebDriver.Window window;

   @Mock
   private WebDriver.Options options;

   @BeforeEach
   void setUp() {
      // Setup the mock chain
      when(driver.navigate()).thenReturn(navigation);
      when(driver.switchTo()).thenReturn(targetLocator);
      when(driver.manage()).thenReturn(options);
      when(options.window()).thenReturn(window);

      // Create the SUT
      sut = new NavigationServiceFluent<>(uiServiceFluent, driver);
   }

   @Test
   @DisplayName("navigate should call driver.get and maximize window")
   void navigateShouldCallDriverGetAndMaximizeWindow() {
      // Given
      String url = "https://example.com";

      // When
      UiServiceFluent<?> result = sut.navigate(url);

      // Then
      verify(window).maximize();
      verify(driver).get(url);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("back should call driver.navigate().back()")
   void backShouldCallDriverNavigateBack() {
      // When
      UiServiceFluent<?> result = sut.back();

      // Then
      verify(navigation).back();
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("forward should call driver.navigate().forward()")
   void forwardShouldCallDriverNavigateForward() {
      // When
      UiServiceFluent<?> result = sut.forward();

      // Then
      verify(navigation).forward();
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("refresh should call driver.navigate().refresh()")
   void refreshShouldCallDriverNavigateRefresh() {
      // When
      UiServiceFluent<?> result = sut.refresh();

      // Then
      verify(navigation).refresh();
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToNewTab should switch to a different window handle")
   void switchToNewTabShouldSwitchToDifferentWindowHandle() {
      // Given
      String currentHandle = "current-handle";
      Set<String> handles = new HashSet<>();
      handles.add(currentHandle);
      handles.add("new-handle");

      // Configure window handles
      when(driver.getWindowHandle()).thenReturn(currentHandle);
      when(driver.getWindowHandles()).thenReturn(handles);

      // When
      UiServiceFluent<?> result = sut.switchToNewTab();

      // Then
      verify(targetLocator).window("new-handle");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToWindow should switch to window with matching title")
   void switchToWindowShouldSwitchToWindowWithMatchingTitle() {
      // Given
      String targetTitle = "Target Window";
      Set<String> handles = new HashSet<>();
      handles.add("handle1");
      handles.add("handle2");

      // Mock driver behavior
      when(driver.getWindowHandles()).thenReturn(handles);

      // Set up a sequence of return values for getting title
      when(driver.getTitle())
            .thenReturn("First Window")  // First call returns this
            .thenReturn(targetTitle);    // Second call returns this

      // When
      UiServiceFluent<?> result = sut.switchToWindow(targetTitle);

      // Then
      // Verify it switched to both windows until it found the right one
      ArgumentCaptor<String> handleCaptor = ArgumentCaptor.forClass(String.class);
      verify(targetLocator, times(2)).window(handleCaptor.capture());
      assertThat(handleCaptor.getAllValues()).containsExactly("handle2", "handle1");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToWindow should throw NoSuchWindowException when no matching window found")
   void switchToWindowShouldThrowExceptionWhenNoMatchingWindow() {
      // Given
      String targetTitle = "Target Window";
      Set<String> handles = new HashSet<>();
      handles.add("handle1");
      handles.add("handle2");

      when(driver.getWindowHandles()).thenReturn(handles);
      when(driver.getTitle())
            .thenReturn("First Window")
            .thenReturn("Second Window");

      // When/Then
      assertThrows(NoSuchWindowException.class, () -> sut.switchToWindow(targetTitle));
   }

   @Test
   @DisplayName("closeCurrentTab should close current tab and switch to another")
   void closeCurrentTabShouldCloseTabAndSwitchToAnother() {
      // Given
      Set<String> handles = new HashSet<>();
      handles.add("handle1");

      // Mock behavior using a real set with predetermined content
      when(driver.getWindowHandles()).thenReturn(handles);

      // When
      UiServiceFluent<?> result = sut.closeCurrentTab();

      // Then
      verify(driver).close();
      verify(targetLocator).window("handle1"); // Only one handle left
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToFrameByIndex should switch to frame by index")
   void switchToFrameByIndexShouldSwitchToFrame() {
      // When
      UiServiceFluent<?> result = sut.switchToFrameByIndex(2);

      // Then
      verify(targetLocator).frame(2);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToFrameByNameOrId should switch to frame by name or id")
   void switchToFrameByNameOrIdShouldSwitchToFrame() {
      // Given
      String frameId = "testFrame";

      // When
      UiServiceFluent<?> result = sut.switchToFrameByNameOrId(frameId);

      // Then
      verify(targetLocator).frame(frameId);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToParentFrame should switch to parent frame")
   void switchToParentFrameShouldSwitchToParentFrame() {
      // When
      UiServiceFluent<?> result = sut.switchToParentFrame();

      // Then
      verify(targetLocator).parentFrame();
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("switchToDefaultContent should switch to default content")
   void switchToDefaultContentShouldSwitchToDefaultContent() {
      // When
      UiServiceFluent<?> result = sut.switchToDefaultContent();

      // Then
      verify(targetLocator).defaultContent();
      assertThat(result).isSameAs(uiServiceFluent);
   }

   // Special test for JavascriptExecutor
   @Test
   @DisplayName("openNewTab should execute JavaScript and switch to new tab")
   void openNewTabShouldExecuteJavaScriptAndSwitchToNewTab() {
      // Create a special driver that implements JavascriptExecutor
      SmartWebDriver jsDriver = mock(SmartWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) jsDriver;

      // Mock behavior for the special driver
      WebDriver.TargetLocator jsTargetLocator = mock(WebDriver.TargetLocator.class);
      when(jsDriver.switchTo()).thenReturn(jsTargetLocator);

      // Setup window handles for the special driver
      String currentHandle = "current-handle";
      Set<String> handles = new HashSet<>();
      handles.add(currentHandle);
      handles.add("new-handle");
      when(jsDriver.getWindowHandle()).thenReturn(currentHandle);
      when(jsDriver.getWindowHandles()).thenReturn(handles);

      // Create a new SUT with the special driver
      NavigationServiceFluent<UiServiceFluent<?>> jsSut = new NavigationServiceFluent<>(uiServiceFluent, jsDriver);

      // Execute the test
      UiServiceFluent<?> result = jsSut.openNewTab();

      // Verify JavaScript execution
      verify(jsExecutor).executeScript("window.open();");
      verify(jsTargetLocator).window("new-handle");
      assertThat(result).isSameAs(uiServiceFluent);
   }
}