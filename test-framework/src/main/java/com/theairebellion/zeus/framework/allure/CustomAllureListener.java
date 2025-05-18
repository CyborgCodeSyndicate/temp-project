package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.util.UUID;

/**
 * Custom Allure listener for managing structured Allure steps and test lifecycle.
 *
 * <p>This class extends {@link AllureJunit5} to provide additional functionality for tracking parent and nested steps
 * in Allure reports. It ensures proper test structuring, supports dynamic step statuses, and enables test ID tracking.
 *
 * <p>Features:
 * <ul>
 *     <li>Tracks parent steps and child steps in Allure reports.</li>
 *     <li>Manages step statuses dynamically (INFO, SUCCESS, WARNING, ERROR).</li>
 *     <li>Supports clearing and setting test identifiers for traceability.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class CustomAllureListener extends AllureJunit5 {

   /**
    * Thread-local storage for the current step name.
    */
   private static final ThreadLocal<String> STEP_NAME = new ThreadLocal<>();

   /**
    * Enumeration for defining different step status types in Allure reporting.
    */
   public enum StatusType {
      /**
       * Represents a neutral step with no explicit status.
       */
      DEFAULT,

      /**
       * Indicates an informational step.
       */
      INFO,

      /**
       * Marks the step as successfully completed.
       */
      SUCCESS,

      /**
       * Marks the step as a warning (e.g., partial failure).
       */
      WARNING,

      /**
       * Marks the step as an error (e.g., assertion failure).
       */
      ERROR
   }

   /**
    * Starts a new step.
    * <p>
    * This method creates a new step entry in Allure and sets the step name variable.
    * </p>
    *
    * @param name The name of the step.
    * @param type The status type of the step.
    */
   private static void startStep(String name, StatusType type) {
      String uuid = UUID.randomUUID().toString();
      StepResult stepResult = new StepResult().setName(name);
      applyStepType(stepResult, type);

      Allure.getLifecycle().startStep(uuid, stepResult);
      STEP_NAME.set(name);
   }

   /**
    * Starts a new step using a predefined {@link StepType}.
    *
    * @param stepType The predefined step type.
    */
   public static void startStep(StepType stepType) {
      startStep(stepType.getDisplayName(), StatusType.DEFAULT);
   }

   /**
    * Starts a new step with a custom name.
    *
    * @param stepName The name of the step.
    */
   public static void startStep(String stepName) {
      startStep(stepName, StatusType.DEFAULT);
   }

   /**
    * Starts a new step with a custom name and predefined {@link StatusType}.
    *
    * @param stepName   The name of the step.
    * @param statusType The predefined status type.
    */
   public static void startStepWithStatusType(String stepName, StatusType statusType) {
      startStep(stepName, statusType);
   }

   /**
    * Stops the most recently started step.
    */
   public static void stopStep() {
      Allure.getLifecycle().stopStep();
      STEP_NAME.remove();
   }

   /**
    * Checks if a specific step name is currently active.
    *
    * @param stepName The step name to check.
    * @return {@code true} if the specified step with name is active, otherwise {@code false}.
    */
   public static boolean isStepActive(String stepName) {
      return stepName.equals(STEP_NAME.get());
   }

   /**
    * Retrieves active step name.
    *
    * @return active step name.
    */
   public static String getActiveStepName() {
      return STEP_NAME.get();
   }

   /**
    * Applies the appropriate Allure step status based on the provided {@link StatusType}.
    *
    * @param stepResult The step result to modify.
    * @param type       The status type to apply.
    */
   private static void applyStepType(StepResult stepResult, StatusType type) {
      if (type == null) {
         type = StatusType.DEFAULT;
      }

      switch (type) {
         case INFO:
            stepResult.setStatus(Status.SKIPPED);
            break;
         case SUCCESS:
            stepResult.setStatus(Status.PASSED);
            break;
         case WARNING:
            stepResult.setStatus(Status.BROKEN);
            break;
         case ERROR:
            stepResult.setStatus(Status.FAILED);
            break;
         case DEFAULT:
         default:
            break;
      }
   }

}
