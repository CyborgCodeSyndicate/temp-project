package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
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
        LogUI.extended("Exception in method {}: {}", method.getName(), cause.getMessage(), cause);
        exceptionLogging(target, method, args, e, cause);
    }


    private static void exceptionLogging(final Object target, final Method method, final Object[] args,
                                         final InvocationTargetException e, final Throwable cause) {
        Optional<ExceptionLogging> exceptionalLoggingOptional = Arrays.stream(ExceptionLogging.values())
                                                                    .filter(log -> {
                                                                        boolean objectTypeIsSame = log.getObjectClass()
                                                                                                       .equals(
                                                                                                           target.getClass());
                                                                        boolean methodNameIsSame = log.getMethodName()
                                                                                                       .equals(
                                                                                                           method.getName());

                                                                        boolean exceptionIsSame = log.getExceptionClass()
                                                                                                      .equals(
                                                                                                          cause.getClass());

                                                                        Class<?>[] methodParamTypes = method.getParameterTypes();

                                                                        Class<?>[] argsTypes = Arrays.stream(args)
                                                                                                   .map(arg ->
                                                                                                            arg == null ?
                                                                                                                null :
                                                                                                                arg.getClass())
                                                                                                   .toArray(
                                                                                                       Class<?>[]::new);
                                                                        boolean methodArgumentsAreSame = Arrays.equals(
                                                                            methodParamTypes,
                                                                            argsTypes);
                                                                        return objectTypeIsSame && methodNameIsSame && exceptionIsSame && methodArgumentsAreSame;
                                                                    }).findFirst();
        exceptionalLoggingOptional.ifPresent(
            exceptionLogging -> exceptionLogging.getHandleFunction().accept(target, method.getName(), args, e));
    }


    private static WebDriver getWebDriverFromWebElement(WebElement element) {
        if (element instanceof RemoteWebElement) {
            return ((RemoteWebElement) element).getWrappedDriver();
        }
        throw new IllegalArgumentException("Element is not a RemoteWebElement and cannot provide WebDriver.");
    }

}
