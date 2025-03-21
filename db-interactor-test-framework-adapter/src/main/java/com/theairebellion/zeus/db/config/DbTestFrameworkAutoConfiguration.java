package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configures the database test framework components.
 * <p>
 * This class provides Spring configuration for database-related test components.
 * It ensures that the framework automatically registers the necessary database
 * client manager and query response validator implementations.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Configuration
@ComponentScan(basePackages = {"com.theairebellion.zeus.db"})
public class DbTestFrameworkAutoConfiguration {

    /**
     * Provides the primary database client manager implementation.
     * <p>
     * Uses {@link AllureDbClientManager} to enable database client interactions
     * with enhanced logging and reporting for Allure.
     * </p>
     *
     * @param allureDbClientManager The Allure-enabled database client manager.
     * @return The configured {@code DbClientManager} bean.
     */
    @Bean
    @Primary
    public DbClientManager dbClientManager(AllureDbClientManager allureDbClientManager) {
        return allureDbClientManager;
    }

    /**
     * Provides the primary query response validator implementation.
     * <p>
     * Uses {@link QueryResponseValidatorAllureImpl} to enhance query validation
     * with Allure reporting capabilities.
     * </p>
     *
     * @param queryResponseValidator The Allure-enabled query response validator.
     * @return The configured {@code QueryResponseValidator} bean.
     */
    @Bean
    @Primary
    public QueryResponseValidator queryResponseValidator(QueryResponseValidatorAllureImpl queryResponseValidator) {
        return queryResponseValidator;
    }

}
