package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.api.allure.RestClientAllureImpl;
import com.theairebellion.zeus.api.allure.RestResponseValidatorAllureImpl;
import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configures the API test framework components.
 *
 * <p>This class defines Spring beans for core API testing components, including
 * the {@link RestClient} and {@link RestResponseValidator}, with Allure-enabled implementations.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Configuration
@ComponentScan(basePackages = {"com.theairebellion.zeus.api"})
public class ApiTestFrameworkAutoConfiguration {

   /**
    * Provides a primary bean for REST API interactions.
    *
    * @param restClientAllure The Allure-integrated REST client.
    * @return The configured {@link RestClient}.
    */
   @Bean
   @Primary
   public RestClient restClient(RestClientAllureImpl restClientAllure) {
      return restClientAllure;
   }

   /**
    * Provides a primary bean for API response validation.
    *
    * @param restResponseValidator The Allure-integrated response validator.
    * @return The configured {@link RestResponseValidator}.
    */
   @Bean
   @Primary
   public RestResponseValidator restResponseValidator(RestResponseValidatorAllureImpl restResponseValidator) {
      return restResponseValidator;
   }

}
