package com.theairebellion.zeus.framework.extension;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.theairebellion.zeus.framework.util.AllureStepHelper.initializeTestEnvironment;
import static com.theairebellion.zeus.framework.util.AllureStepHelper.setupTestContext;

/**
 * JUnit 5 {@code BeforeTestExecutionCallback} extension that logs test execution start details.
 * <p>
 * This extension captures and logs the test name before execution begins,
 * storing the start time in the test execution context for later use in reporting.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Order(Integer.MIN_VALUE)
public class Prologue implements BeforeTestExecutionCallback {

    /**
     * Executes before the test method starts.
     * <p>
     * This method records the start time of the test, assigns a unique identifier
     * to the test execution context, and logs the initiation of the test scenario.
     * </p>
     *
     * @param context The test execution context containing metadata about the test.
     */
    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        initializeTestEnvironment();
        setupTestContext(context);
    }

}