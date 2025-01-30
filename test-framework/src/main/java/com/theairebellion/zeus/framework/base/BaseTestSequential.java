package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseTestSequential extends BaseTest {


    @Autowired
    @Lazy
    private Services services;

    @AfterAll
    protected final void beforeAll() {
        beforeAll(services);
    }


    protected void beforeAll(Services services) {
    }


    @AfterAll
    protected final void afterAll() {
        afterAll(services);
    }


    protected void afterAll(Services services) {
    }

}
