package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggingFunctions {


    public static void findElementFromRoot(final Object target, final String method, final Object[] args,
                                           final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebDriver driver = (WebDriver) target;

        String pageSource = driver.getPageSource();
        if (pageSource != null && pageSource.length() > 1000) {
            pageSource = pageSource.substring(0, 1000) + "...";
        }
        String additionalInfo = String.format(
                "Element was not found from root by using locator: [%s]. Here is the whole page source: \n[%s]",
                locator.toString(), pageSource);
        logException(cause.getClass(), target, method, args, additionalInfo);

    }


    public static void findElementFromElement(final Object target, final String method, final Object[] args,
                                              final InvocationTargetException e) {
        By locator = (args != null && args.length > 0 && args[0] instanceof By) ? (By) args[0] : null;
        Throwable cause = e.getCause();
        WebElement element = (WebElement) target;

        String elementInnerHtml = element.getAttribute("innerHTML");
        if (elementInnerHtml != null && elementInnerHtml.length() > 1000) {
            elementInnerHtml = elementInnerHtml.substring(0, 1000) + "...";
        }
        String additionalInfo = String.format(
                "Element was not found from another element: [%s] by using locator: [%s]. Here is the whole inner html: \n[%s]",
                element, locator.toString(), elementInnerHtml);
        logException(cause.getClass(), target, method, args, additionalInfo);
    }


    private static void logException(Class<? extends Throwable> throwable, Object target, String methodName,
                                     Object[] methodArgs, String additionalInfo) {
        LogUI.extended("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with arguments: [{}]",
                throwable.getSimpleName(), target.getClass(), target.toString(), methodName,
                printMethodArgsClasses(methodArgs));
        LogUI.extended("Additional Info for the problem: {}", additionalInfo);
    }


    private static String printMethodArgsClasses(Object[] methodArgs) {
        if (methodArgs == null || methodArgs.length == 0) {
            return "";
        }
        return Arrays.stream(methodArgs)
                .map(arg -> arg == null ? "null" : arg.getClass().getSimpleName())
                .collect(Collectors.joining(","));
    }

}
