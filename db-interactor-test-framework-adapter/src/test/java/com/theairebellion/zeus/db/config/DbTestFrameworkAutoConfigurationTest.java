package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.db.allure.AllureDbClientManager;
import com.theairebellion.zeus.db.allure.QueryResponseValidatorAllureImpl;
import com.theairebellion.zeus.db.client.DbClientManager;
import com.theairebellion.zeus.db.validator.QueryResponseValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class DbTestFrameworkAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(DbTestFrameworkAutoConfiguration.class);
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @Test
    void testBeansPresence() {
        // Verify that a DbClientManager bean is present and is an instance of AllureDbClientManager.
        DbClientManager clientManager = context.getBean(DbClientManager.class);
        assertNotNull(clientManager);
        assertThat(clientManager).isInstanceOf(AllureDbClientManager.class);

        // Verify that a QueryResponseValidator bean is present and is an instance of QueryResponseValidatorAllureImpl.
        QueryResponseValidator validator = context.getBean(QueryResponseValidator.class);
        assertNotNull(validator);
        assertThat(validator).isInstanceOf(QueryResponseValidatorAllureImpl.class);
    }
}