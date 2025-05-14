package com.theairebellion.zeus.ui.selenium.listeners;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.logging.ExceptionLogging;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElementInspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

/**
 * Custom WebDriver event listener for logging and exception handling.
 *
 * <p>This class implements {@link WebDriverListener} to provide logging before and after element interactions
 * and manage exception handling for WebDriver actions.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class WebDriverEventListener implements WebDriverListener {

   /**
    * Logs and handles exceptions based on predefined criteria.
    *
    * @param target The object on which the method was invoked.
    * @param method The method that caused the exception.
    * @param args   The arguments passed to the method.
    * @param e      The invocation target exception.
    * @param cause  The root cause of the exception.
    */
   private static void exceptionLogging(final Object target, final Method method, final Object[] args,
                                        final InvocationTargetException e, final Throwable cause) {
      Optional<ExceptionLogging> matched = Arrays.stream(ExceptionLogging.values())
            .filter(log -> matchesLogCriteria(log, target, method, args, cause)).findFirst();

      matched.ifPresent(exceptionLogging -> {
         WebElementAction action = exceptionLogging.getAction();
         exceptionLogging.getExceptionLoggingMap().get(cause.getClass()).accept(target, action, args, e);
      });
   }

   /**
    * Checks whether an exception matches the criteria for logging and handling.
    *
    * @param log    The exception logging configuration.
    * @param target The object on which the method was invoked.
    * @param method The method that caused the exception.
    * @param args   The arguments passed to the method.
    * @param cause  The root cause of the exception.
    * @return True if the exception matches the criteria, false otherwise.
    */
   private static boolean matchesLogCriteria(final ExceptionLogging log, final Object target, final Method method,
                                             final Object[] args, final Throwable cause) {
      return isTargetValid(log, target)
            && isMethodValid(log, method)
            && isCauseValid(log, cause)
            && areArgumentsCompatible(method, args);
   }

   private static boolean isTargetValid(ExceptionLogging log, Object target) {
      return log.getTargetClass().isAssignableFrom(target.getClass());
   }

   private static boolean isMethodValid(ExceptionLogging log, Method method) {
      return log.getAction().getMethodName().equals(method.getName());
   }

   private static boolean isCauseValid(ExceptionLogging log, Throwable cause) {
      return log.getExceptionLoggingMap().containsKey(cause.getClass());
   }

   private static boolean areArgumentsCompatible(Method method, Object[] args) {
      Class<?>[] paramTypes = method.getParameterTypes();
      Class<?>[] argTypes = Arrays.stream(args)
            .map(arg -> arg == null ? null : arg.getClass())
            .toArray(Class<?>[]::new);

      if (paramTypes.length != argTypes.length) {
         return false;
      }

      for (int i = 0; i < paramTypes.length; i++) {
         if (!isArgumentAssignable(paramTypes[i], argTypes[i])) {
            return false;
         }
      }

      return true;
   }

   private static boolean isArgumentAssignable(Class<?> paramType, Class<?> argType) {
      if (argType == null) {
         return !paramType.isPrimitive();
      }
      return paramType.isAssignableFrom(argType);
   }

   /**
    * Logs an informational message before an element is clicked.
    *
    * @param element The WebElement that is about to be clicked.
    */
   @Override
   public void beforeClick(final WebElement element) {
      LogUi.extended("Element: '{}' is about to get clicked", element.toString());
   }

   /**
    * Logs an informational message after an element is clicked.
    *
    * @param element The WebElement that was clicked.
    */
   @Override
   public void afterClick(final WebElement element) {
      LogUi.extended("Element: '{}' was clicked", element.toString());
   }

   /**
    * Handles errors occurring during WebDriver method executions.
    *
    * <p>This method logs the exception details and determines if the exception should be handled by
    * the framework or logged as unhandled.
    *
    * @param target The object on which the method was invoked.
    * @param method The method that caused the exception.
    * @param args   The arguments passed to the method.
    * @param e      The invocation target exception.
    */
   @Override
   public void onError(final Object target, final Method method, final Object[] args,
                       final InvocationTargetException e) {
      Throwable cause = e.getCause();

      LogUi.extended("Exception in method {}: {}", method.getName(), cause.getMessage());
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(cause);

      if (result.comingFromWait()) {
         return;
      }

      if (result.foundAnnotatedMethod()) {
         if (result.foundHandleException()) {
            LogUi.error("Exception was not handled");
         } else {
            LogUi.info("Framework will try to handle the exception");
         }
      } else {
         LogUi.info("No implementation in framework for exception handling in this method");
      }
      exceptionLogging(target, method, args, e, cause);

   }

}
