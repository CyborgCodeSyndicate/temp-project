package com.theairebellion.zeus.framework.extension;

import com.theairebellion.zeus.framework.log.LogTest;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

import static com.theairebellion.zeus.framework.storage.StoreKeys.START_TIME;

@Order(Integer.MIN_VALUE)
public class Prologue implements BeforeTestExecutionCallback {


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

        LogTest.info("The quest: '{}' has begun.", context.getDisplayName());
    }


}
