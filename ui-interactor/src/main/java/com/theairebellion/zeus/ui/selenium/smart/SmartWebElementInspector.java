package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUIException;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class SmartWebElementInspector {

    private static final String SMART_WEB_ELEMENT_CLASS = SmartWebElement.class.getName();
    private static final String SMART_WEB_DRIVER_CLASS = SmartWebDriver.class.getName();
    private static final Set<String> ANNOTATED_METHODS = new HashSet<>();

    static {
        for (Method m : SmartWebElement.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(HandleUIException.class)) {
                ANNOTATED_METHODS.add(m.getName());
            }
        }
    }

    private SmartWebElementInspector() {
    }


    public static Result inspectStackTrace(Throwable t) {
        if (t == null) return new Result(false, false, false);

        boolean foundAnnotatedMethod = false;
        boolean foundHandleException = false;
        boolean comingFromWait = false;

        for (StackTraceElement element : t.getStackTrace()) {
            if (FluentWait.class.getName().equals(element.getClassName())) {
                comingFromWait = true;
            }
            if (SMART_WEB_ELEMENT_CLASS.equals(element.getClassName())) {
                String methodName = element.getMethodName();
                if (ANNOTATED_METHODS.contains(methodName)) {
                    foundAnnotatedMethod = true;
                }
                if ("handleException".equals(methodName)) {
                    foundHandleException = true;
                }
            }
            if (SMART_WEB_DRIVER_CLASS.equals(element.getClassName())) {
                String methodName = element.getMethodName();
                if (ANNOTATED_METHODS.contains(methodName)) {
                    foundAnnotatedMethod = true;
                }
                if ("handleException".equals(methodName)) {
                    foundHandleException = true;
                }
            }
        }
        return new Result(foundAnnotatedMethod, foundHandleException, comingFromWait);
    }

    public static final class Result {
        private final boolean foundAnnotatedMethod;
        private final boolean foundHandleException;
        private final boolean comingFromWait;

        public Result(boolean foundAnnotatedMethod, boolean foundHandleException, boolean comingFromWait) {
            this.foundAnnotatedMethod = foundAnnotatedMethod;
            this.foundHandleException = foundHandleException;
            this.comingFromWait = comingFromWait;
        }

        public boolean foundAnnotatedMethod() {
            return foundAnnotatedMethod;
        }

        public boolean foundHandleException() {
            return foundHandleException;
        }

        public boolean comingFromWait() {
            return comingFromWait;
        }
    }
}