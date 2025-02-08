package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

public class DbTestFrameworkAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void initializeContext() {
        context = new AnnotationConfigApplicationContext(DbTestFrameworkAutoConfiguration.class);
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    @Test
    void verifyBeansPresence() {
        DbClientManager clientManager = context.getBean(DbClientManager.class);
        assertNotNull(clientManager);
        assertThat(clientManager).isInstanceOf(AllureDbClientManager.class);

        QueryResponseValidator validator = context.getBean(QueryResponseValidator.class);
        assertNotNull(validator);
        assertThat(validator).isInstanceOf(QueryResponseValidatorAllureImpl.class);
    }
}