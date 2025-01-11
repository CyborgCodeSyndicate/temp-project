package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = {"com.theairebellion.zeus.db"})
public class DbTestFrameworkAutoConfiguration {

    @Bean
    @Primary
    public DbClientManager dbClientManager(AllureDbClientManager allureDbClientManager) {
        return allureDbClientManager;
    }


    @Bean
    @Primary
    public QueryResponseValidator queryResponseValidator(QueryResponseValidatorAllureImpl queryResponseValidator) {
        return queryResponseValidator;
    }


}
