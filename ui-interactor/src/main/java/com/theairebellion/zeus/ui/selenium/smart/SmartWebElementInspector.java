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
 * if it originated from a wait operation, and if the exception was handled.</p>
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

   /**
    * Represents the result of a stack trace inspection, indicating whether an exception
    * was caused by an annotated method, was handled, or originated from a wait operation.
    *
    * @author Cyborg Code Syndicate 💍👨💻
    */
   public static final class Result {
      private final boolean foundAnnotatedMethod;
      private final boolean foundHandleException;
      private final boolean comingFromWait;

      /**
       * Constructs a result object containing exception origin flags.
       *
       * @param foundAnnotatedMethod {@code true} if the stack trace contains a method annotated with
       *     {@link HandleUiException}.
       * @param foundHandleException {@code true} if the stack trace contains a call to `handleException`.
       * @param comingFromWait       {@code true} if the stack trace indicates the exception occurred within a
       *     {@link FluentWait}.
       */
      public Result(boolean foundAnnotatedMethod, boolean foundHandleException, boolean comingFromWait) {
         this.foundAnnotatedMethod = foundAnnotatedMethod;
         this.foundHandleException = foundHandleException;
         this.comingFromWait = comingFromWait;
      }

      /**
       * Checks if the exception originated from a method annotated with {@link HandleUiException}.
       *
       * @return {@code true} if the method was annotated, otherwise {@code false}.
       */
      public boolean foundAnnotatedMethod() {
         return foundAnnotatedMethod;
      }

      /**
       * Checks if the exception was handled by the `handleException` method.
       *
       * @return {@code true} if the exception was handled, otherwise {@code false}.
       */
      public boolean foundHandleException() {
         return foundHandleException;
      }

      /**
       * Checks if the exception occurred during a Selenium {@link FluentWait} operation.
       *
       * @return {@code true} if the exception came from a wait operation, otherwise {@code false}.
       */
      public boolean comingFromWait() {
         return comingFromWait;
      }
   }
}