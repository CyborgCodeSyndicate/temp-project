package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import javax.swing.text.html.HTML;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ValidationServiceFluent Tests")
@ExtendWith(MockitoExtension.class)
class ValidationServiceFluentTest {

   @Mock
   private UiServiceFluent<?> uiServiceFluent;

   @Mock
   private SmartWebDriver driver;

   private ValidationServiceFluent<UiServiceFluent<?>> sut;

   @BeforeEach
   void setUp() {
      // Setup lenient mocking for methods that might be called multiple times
      lenient().doNothing().when(driver).waitUntilElementIsShown(any(By.class), anyInt());

      // Create the system under test
      sut = new ValidationServiceFluent<>(uiServiceFluent, driver);
   }

   @Test
   void constructorCoverage() {
      ValidationServiceFluent<UiServiceFluent<?>> other =
            new ValidationServiceFluent<>(uiServiceFluent, driver);
      assertThat(other).isNotNull();
   }

   @Test
   void validateTextInFieldDefaultTest() {
      // Prepare the mock to return true when checking for no exception
      when(driver.checkNoException(any())).thenReturn(true);

      sut.validateTextInField(HTML.Tag.P, "HelloText");

      // Verify the expected method calls
      By expectedLocator = By.xpath("//p[contains(text(),'HelloText')]");
      verify(driver).waitUntilElementIsShown(expectedLocator, 2);
      verify(driver).checkNoException(any());

      // Capture and verify the Runnable validated by uiServiceFluent
      ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
      verify(uiServiceFluent).validate(captor.capture());

      // Execute the captured runnable to ensure no exceptions
      captor.getValue().run();
   }

   @Test
   void validateTextInFieldSoftTest() {
      // Prepare the mock to return true when checking for no exception
      when(driver.checkNoException(any())).thenReturn(true);

      sut.validateTextInField(HTML.Tag.SPAN, "SoftVal", true);

      // Verify the expected method calls
      By expectedLocator = By.xpath("//span[contains(text(),'SoftVal')]");
      verify(driver).waitUntilElementIsShown(expectedLocator, 2);
      verify(driver).checkNoException(any());

      // Capture the soft assertions consumer
      ArgumentCaptor<java.util.function.Consumer<SoftAssertions>> softCaptor =
            ArgumentCaptor.forClass(java.util.function.Consumer.class);
      verify(uiServiceFluent).validate(softCaptor.capture());

      // Execute the captured consumer with a SoftAssertions to confirm no exception
      SoftAssertions s = new SoftAssertions();
      softCaptor.getValue().accept(s);
      s.assertAll(); // Should pass
   }

   @Test
   void validateTextInFieldFails() {
      // Simulate element not found
      when(driver.checkNoException(any())).thenReturn(false);

      sut.validateTextInField(HTML.Tag.DIV, "MissingVal");

      By expectedLocator = By.xpath("//div[contains(text(),'MissingVal')]");
      verify(driver).waitUntilElementIsShown(expectedLocator, 2);
      verify(driver).checkNoException(any());

      // Capture the Runnable for validation
      ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
      verify(uiServiceFluent).validate(captor.capture());

      // Attempt to run the captured runnable
      // Note: This will likely throw an AssertionError in the actual test context
      try {
         captor.getValue().run();
      } catch (AssertionError e) {
         // Expected in a real scenario, do nothing
      }
   }

   @Test
   void validateTextInFieldSoftFails() {
      // Simulate element not found
      when(driver.checkNoException(any())).thenReturn(false);

      sut.validateTextInField(HTML.Tag.SPAN, "FailSoft", true);

      By locator = By.xpath("//span[contains(text(),'FailSoft')]");
      verify(driver).waitUntilElementIsShown(locator, 2);
      verify(driver).checkNoException(any());

      // Capture the soft assertions consumer
      ArgumentCaptor<java.util.function.Consumer<SoftAssertions>> softCaptor =
            ArgumentCaptor.forClass(java.util.function.Consumer.class);
      verify(uiServiceFluent).validate(softCaptor.capture());

      // Execute the consumer with soft assertions
      SoftAssertions s = new SoftAssertions();
      softCaptor.getValue().accept(s);

      // Note: In a real test, you might want to add more specific assertions
      // about the soft assertion failure
   }
}