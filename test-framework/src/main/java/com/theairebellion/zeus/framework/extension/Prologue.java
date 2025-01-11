package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Order(Integer.MIN_VALUE)
public class Prologue implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        String className = context.getTestClass().get().getSimpleName();
        String methodName = context.getTestMethod().get().getName();
        ThreadContext.put("testName", className + "." + methodName);
        context.getStore(GLOBAL).put(START_TIME.getKey(), System.currentTimeMillis());
        LogTest.info("The : '{}' quest has begun.", context.getDisplayName());
    }

}
