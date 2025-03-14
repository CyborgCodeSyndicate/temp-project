package com.theairebellion.zeus.framework.extension;

import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;

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
        String className = context.getTestClass()
                .map(Class::getSimpleName)
                .orElse("UnknownClass");
        String methodName = context.getTestMethod()
                .map(Method::getName)
                .orElse("UnknownMethod");

        ThreadContext.put("testName", className + "." + methodName);

        context.getStore(ExtensionContext.Namespace.GLOBAL).put(START_TIME, System.currentTimeMillis());

    }

}
