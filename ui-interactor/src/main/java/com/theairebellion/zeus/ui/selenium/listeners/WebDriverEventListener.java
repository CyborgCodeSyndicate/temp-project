package com.theairebellion.zeus.ui.selenium.listeners;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.logging.ExceptionLogging;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElementInspector;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class WebDriverEventListener implements WebDriverListener {


    @Override
    public void beforeClick(final WebElement element) {
        LogUI.extended("Element: '{}' is about to get clicked", element.toString());
    }


    @Override
    public void afterClick(final WebElement element) {
        LogUI.extended("Element: '{}' was clicked", element.toString());
    }


    @Override
    public void onError(final Object target, final Method method, final Object[] args,
                        final InvocationTargetException e) {
        Throwable cause = e.getCause();

        LogUI.extended("Exception in method {}: {}", method.getName(), cause.getMessage());
        SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(cause);

        if (result.comingFromWait()) {
            return;
        }

        if (result.foundAnnotatedMethod()) {
            if (result.foundHandleException()) {
                LogUI.error("Exception was not handled");
            } else {
                LogUI.info("Framework will try to handle the exception");
            }
        } else {
            LogUI.info("No implementation in framework for exception handling in this method");
        }
        exceptionLogging(target, method, args, e, cause);

    }


    private static void exceptionLogging(final Object target, final Method method, final Object[] args,
                                         final InvocationTargetException e, final Throwable cause) {
        Optional<ExceptionLogging> matched = Arrays.stream(ExceptionLogging.values())
                .filter(log -> matchesLogCriteria(log, target, method, args, cause)).findFirst();

        matched.ifPresent(exceptionLogging -> {
            WebElementAction action = exceptionLogging.getAction();
            exceptionLogging.getExceptionLoggingMap().get(cause.getClass()).accept(target, action, args, e);
        });
    }

    private static boolean matchesLogCriteria(final ExceptionLogging log, final Object target, final Method method,
                                              final Object[] args, final Throwable cause) {
        if (!log.getTargetClass().isAssignableFrom(target.getClass())) {
            return false;
        }

        if (!log.getAction().getMethodName().equals(method.getName())) {
            return false;
        }

        if (!log.getExceptionLoggingMap().containsKey(cause.getClass())) {
            return false;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        Class<?>[] argTypes =
                Arrays.stream(args).map(arg -> arg == null ? null : arg.getClass()).toArray(Class<?>[]::new);

        if (paramTypes.length != argTypes.length) {
            return false;
        }

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            Class<?> argType = argTypes[i];

            if (argType == null) {
                if (paramType.isPrimitive()) {
                    return false;
                }
            } else {
                if (!paramType.isAssignableFrom(argType)) {
                    return false;
                }
            }
        }

        return true;
    }

}
