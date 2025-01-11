package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.api.allure.RestClientAllureImpl;
import com.theairebellion.zeus.api.allure.RestResponseValidatorAllureImpl;
import com.theairebellion.zeus.api.client.RestClient;
import com.theairebellion.zeus.api.validator.RestResponseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = {"com.theairebellion.zeus.api"})
public class ApiTestFrameworkAutoConfiguration {


    @Bean
    @Primary
    public RestClient restClient(RestClientAllureImpl restClientAllure) {
        return restClientAllure;
    }


    @Bean
    @Primary
    public RestResponseValidator restResponseValidator(RestResponseValidatorAllureImpl restResponseValidator) {
        return restResponseValidator;
    }

}
