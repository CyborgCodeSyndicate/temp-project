package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUiException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.openqa.selenium.support.ui.FluentWait;

/**
 * Utility class for inspecting stack traces to determine if an exception
 * originated from a {@link SmartWebElement} or {@link SmartWebDriver} method.
 *
 * <p>It checks if the method was annotated with {@link HandleUiException},
 * if it originated from a wait operation, and if the exception was handled.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class SmartWebElementInspector {

   private static final String SMART_WEB_ELEMENT_CLASS = SmartWebElement.class.getName();
   private static final String SMART_WEB_DRIVER_CLASS = SmartWebDriver.class.getName();
   private static final Set<String> ANNOTATED_METHODS = new HashSet<>();

   static {
      for (Method m : SmartWebElement.class.getDeclaredMethods()) {
         if (m.isAnnotationPresent(HandleUiException.class)) {
            ANNOTATED_METHODS.add(m.getName());
         }
      }
   }

   /**
    * Private constructor to prevent instantiation.
    */
   private SmartWebElementInspector() {
   }

   /**
    * Inspects the stack trace of a given {@link Throwable} to determine if it originated from
    * a method annotated with {@link HandleUiException}, if it involved the `handleException` method,
    * or if it occurred during a Selenium {@link FluentWait} operation.
    *
    * @param t The throwable to inspect.
    * @return A {@link Result} object containing flags indicating the exception's origin.
    */
   public static Result inspectStackTrace(Throwable t) {
      if (t == null) {
         return new Result(false, false, false);
      }

      boolean foundAnnotatedMethod = false;
      boolean foundHandleException = false;
      boolean comingFromWait = false;

      for (StackTraceElement element : t.getStackTrace()) {
         String className = element.getClassName();
         String methodName = element.getMethodName();

         if (isFromWait(className)) {
            comingFromWait = true;
         }

         if (isSmartComponent(className)) {
            if (isAnnotated(methodName)) {
               foundAnnotatedMethod = true;
            }
            if (isHandleException(methodName)) {
               foundHandleException = true;
            }
         }
      }

      return new Result(foundAnnotatedMethod, foundHandleException, comingFromWait);
   }

   private static boolean isFromWait(String className) {
      return FluentWait.class.getName().equals(className);
   }

   private static boolean isSmartComponent(String className) {
      return SMART_WEB_ELEMENT_CLASS.equals(className) || SMART_WEB_DRIVER_CLASS.equals(className);
   }

   private static boolean isAnnotated(String methodName) {
      return ANNOTATED_METHODS.contains(methodName);
   }

   private static boolean isHandleException(String methodName) {
      return "handleException".equals(methodName);
   }

   /**
    * Represents the result of a stack trace inspection, indicating whether an exception
    * was caused by an annotated method, was handled, or originated from a wait operation.
    *
    * @author Cyborg Code Syndicate 💍👨💻
    */
   public record Result(boolean foundAnnotatedMethod, boolean foundHandleException, boolean comingFromWait) {

   }
}