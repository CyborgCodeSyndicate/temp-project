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
     * This method performs the following steps before executing a test:
     * <ul>
     *     <li>Initializes the test environment by collecting and storing configuration properties.</li>
     *     <li>Writes environment properties and category-related data for reporting.</li>
     *     <li>Extracts and logs the test class and method names.</li>
     *     <li>Assigns a unique identifier to the test execution context.</li>
     *     <li>Stores the test start time for later use in reporting.</li>
     * </ul>
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