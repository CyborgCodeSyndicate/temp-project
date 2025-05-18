package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.listeners.WebDriverEventListener;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import com.theairebellion.zeus.ui.validator.UiTableValidatorImpl;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Spring configuration class for managing UI-related beans.
 *
 * <p>This class defines beans for WebDriver and UI table validation, enabling dependency injection
 * for Selenium-based UI interactions and validation functionalities.
 *
 * <p>The configuration follows a lazy initialization strategy to optimize resource usage.
 * WebDriver instances are created with a "prototype" scope, ensuring each test receives a fresh instance.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Configuration
public class UiInteractionAutoConfiguration {

   /**
    * Provides a lazily initialized and prototype-scoped {@link SmartWebDriver} bean.
    *
    * <p>This method creates a WebDriver instance based on the UI framework configuration.
    * It supports remote and local execution and applies an event-firing decorator to enhance logging and debugging.
    *
    * @return A new instance of {@link SmartWebDriver}.
    */
   @Bean
   @Lazy
   @Scope("prototype")
   public SmartWebDriver webDriver() {
      return new SmartWebDriver(
            WebDriverFactory.createDriver(
                  getUiConfig().browserType(),
                  WebDriverConfig.builder()
                        .version(getUiConfig().browserVersion())
                        .headless(getUiConfig().headless())
                        .remote(getUiConfig().remoteDriverUrl() != null && !getUiConfig().remoteDriverUrl().isEmpty())
                        .remoteUrl(getUiConfig().remoteDriverUrl())
                        .eventFiringDecorator(new EventFiringDecorator<>(new WebDriverEventListener()))
                        .build()
            )
      );
   }

   /**
    * Provides a lazily initialized {@link UiTableValidator} bean.
    *
    * <p>This validator facilitates automated assertions on table-based UI components.
    * It ensures consistency in table data validation across tests.
    *
    * @return A new instance of {@link UiTableValidatorImpl}.
    */
   @Bean
   @Lazy
   public UiTableValidator uiTableValidator() {
      return new UiTableValidatorImpl();
   }

}
