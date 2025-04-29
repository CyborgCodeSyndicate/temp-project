package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base test class for sequential test execution.
 * <p>
 * This class extends {@code BaseTest} and ensures that tests run sequentially
 * within a single instance using the {@code PER_CLASS} lifecycle.
 * It also provides before-all and after-all lifecycle hooks for test setup and cleanup.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseTestSequential extends BaseTest {

    /**
     * The test service container for managing test-related dependencies.
     */
    private Services services;

    @Autowired
    public void setServices(Services services) {
        this.services = services;
    }

    /**
     * Executes the setup logic before all test methods in the class.
     */
    @BeforeAll
    protected final void beforeAll() {
        beforeAll(services);
    }

    /**
     * Hook method for pre-test setup logic.
     *
     * @param services The test service container.
     */
    protected void beforeAll(Services services) {
        //empty method for beforeAll to override if needed
    }

    /**
     * Executes the cleanup logic after all test methods in the class.
     */
    @AfterAll
    protected final void afterAll() {
        afterAll(services);
    }

    /**
     * Hook method for post-test cleanup logic.
     *
     * @param services The test service container.
     */
    protected void afterAll(Services services) {
        //empty method for beforeAll to override if needed
    }

}
