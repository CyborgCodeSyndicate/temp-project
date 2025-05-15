package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("SuperUiServiceFluent Tests")
class SuperUiServiceFluentTest {

   private UiServiceFluent<?> original;
   private SmartWebDriver mockDriver;
   private SuperUiServiceFluent<?> sut;

   @BeforeEach
   void setUp() {
      original = mock(UiServiceFluent.class);
      mockDriver = mock(SmartWebDriver.class);

      when(original.getDriver()).thenReturn(mockDriver);
      sut = new SuperUiServiceFluent<>(original);
   }

   @Test
   @DisplayName("getDriver should return the driver from the original UiServiceFluent")
   void getDriverShouldReturnOriginalDriver() {
      SmartWebDriver driver = sut.getDriver();
      assertThat(driver).isSameAs(mockDriver);
      verify(original, times(2)).getDriver();
   }

   @Test
   @DisplayName("validation should delegate to original UiServiceFluent")
   void validationShouldDelegateToOriginal() {
      List<AssertionResult<Object>> mockResults = mock(List.class);

      sut.validation(mockResults);

      verify(original).validation(mockResults);
   }

   @Test
   @DisplayName("delegate methods from original should be accessible and consistent")
   void delegatedMethodsShouldBeCallable() {
      // Example: say original has a validate(Runnable) method
      Runnable assertion = mock(Runnable.class);
      UiServiceFluent<?> returned = mock(UiServiceFluent.class);

      when(((UiServiceFluent) original).validate(assertion)).thenReturn(returned);

      UiServiceFluent<?> result = sut.validate(assertion);

      assertThat(result).isSameAs(returned);
      verify(original).validate(assertion);
   }
}
