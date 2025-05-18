package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.ui.validator.UiTableValidatorImpl;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UiInteractionAutoConfigurationTest {

   private UiInteractionAutoConfiguration configuration;

   @Mock
   private WebDriver mockDriver;

   @Mock
   private UiConfig mockUiConfig;

   @BeforeEach
   void setUp() {
      configuration = new UiInteractionAutoConfiguration();
   }

   @Test
   void webDriverBean_ShouldBePrototypeScopedAndLazy() throws NoSuchMethodException {
      // Given
      Method webDriverMethod = UiInteractionAutoConfiguration.class.getMethod("webDriver");

      // Then
      assertTrue(webDriverMethod.isAnnotationPresent(Bean.class), "Should be annotated with @Bean");
      assertTrue(webDriverMethod.isAnnotationPresent(Lazy.class), "Should be annotated with @Lazy");
      assertTrue(webDriverMethod.isAnnotationPresent(Scope.class), "Should be annotated with @Scope");

      Scope scopeAnnotation = webDriverMethod.getAnnotation(Scope.class);
      assertEquals("prototype", scopeAnnotation.value(), "Should have prototype scope");
   }

   @Test
   void uiTableValidatorBean_ShouldBeLazy() throws NoSuchMethodException {
      // Given
      Method validatorMethod = UiInteractionAutoConfiguration.class.getMethod("uiTableValidator");

      // Then
      assertTrue(validatorMethod.isAnnotationPresent(Bean.class), "Should be annotated with @Bean");
      assertTrue(validatorMethod.isAnnotationPresent(Lazy.class), "Should be annotated with @Lazy");
      assertFalse(validatorMethod.isAnnotationPresent(Scope.class), "Should not have a Scope annotation");
   }

   @Test
   void webDriver_ShouldCreateSmartWebDriverWithCorrectConfiguration() {
      // Setup of UiConfig mock and WebDriverFactory
      try (MockedStatic<UiConfigHolder> mockedUiConfigHolder = mockStatic(UiConfigHolder.class);
           MockedStatic<WebDriverFactory> mockedWebDriverFactory = mockStatic(WebDriverFactory.class)) {

         // Configure UiConfig mock
         when(mockUiConfig.browserType()).thenReturn("chrome");
         when(mockUiConfig.browserVersion()).thenReturn("latest");
         when(mockUiConfig.headless()).thenReturn(true);
         when(mockUiConfig.remoteDriverUrl()).thenReturn("http://localhost:4444/wd/hub");

         // Configure static mocks
         mockedUiConfigHolder.when(UiConfigHolder::getUiConfig).thenReturn(mockUiConfig);
         mockedWebDriverFactory.when(() -> WebDriverFactory.createDriver(eq("chrome"), any(WebDriverConfig.class)))
               .thenReturn(mockDriver);

         // When
         SmartWebDriver result = configuration.webDriver();

         // Then
         assertNotNull(result, "Should return a SmartWebDriver instance");
         assertEquals(mockDriver, result.getOriginal(), "Should wrap the WebDriver created by the factory");

         // Verify the WebDriverFactory was called with the correct parameters
         mockedWebDriverFactory.verify(() -> WebDriverFactory.createDriver(eq("chrome"), any(WebDriverConfig.class)));
      }
   }

   @Test
   void uiTableValidator_ShouldReturnUiTableValidatorImplInstance() {
      // When
      UiTableValidator result = configuration.uiTableValidator();

      // Then
      assertNotNull(result, "Should return a UiTableValidator instance");
      assertInstanceOf(UiTableValidatorImpl.class, result, "Should be an instance of UiTableValidatorImpl");
   }

   @Test
   void configClass_ShouldHaveConfigurationAnnotation() {
      // Then
      assertTrue(UiInteractionAutoConfiguration.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class),
            "Should be annotated with @Configuration");
   }
}