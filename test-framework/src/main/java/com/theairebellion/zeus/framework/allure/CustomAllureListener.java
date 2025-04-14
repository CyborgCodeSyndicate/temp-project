package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.util.Deque;
import java.util.LinkedList;
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
    * A thread-local stack for tracking active Allure steps.
    *
    * <p>Each test execution maintains its own stack to handle nested steps.
    */
   private static final ThreadLocal<Deque<String>> STEP_STACK = ThreadLocal.withInitial(LinkedList::new);

   /**
    * Thread-local storage for the current parent step UUID.
    */
   private static final ThreadLocal<String> PARENT_STEP = new ThreadLocal<>();

   /**
    * Thread-local storage for the current parent step name.
    */
   private static final ThreadLocal<String> PARENT_STEP_NAME = new ThreadLocal<>();

   /**
    * Thread-local storage for tracking the test ID associated with the execution.
    */
   private static final ThreadLocal<String> TEST_ID = new ThreadLocal<>();

   /**
    * Enumeration for defining different step status types in Allure reporting.
    *
    * @author Cyborg Code Syndicate 💍👨💻
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
    * Starts a parent step in Allure reporting.
    *
    * <p>The parent step will contain nested steps if applicable. The step type defines its visual status.
    *
    * @param name The name of the parent step.
    * @param type The status type of the parent step.
    */
   private static void startParentStep(String name, StatusType type) {
      String uuid = UUID.randomUUID().toString();
      StepResult stepResult = new StepResult().setName(name);
      applyStepType(stepResult, type);

      Allure.getLifecycle().startStep(uuid, stepResult);
      PARENT_STEP.set(uuid);
      PARENT_STEP_NAME.set(name);
   }

   /**
    * Starts a parent step using a predefined {@link StepType}.
    *
    * @param parentStepType The predefined parent step type.
    */
   public static void startParentStep(StepType parentStepType) {
      startParentStep(parentStepType.getDisplayName(), StatusType.DEFAULT);
   }

   /**
    * Stops the currently active parent step.
    */
   public static void stopParentStep() {
      if (PARENT_STEP.get() != null) {
         Allure.getLifecycle().stopStep(PARENT_STEP.get());
         PARENT_STEP.remove();
         PARENT_STEP_NAME.remove();
      }
   }

   /**
    * Starts a new step within the current parent step.
    *
    * <p>This method creates a new step entry in Allure and pushes it onto the step stack.
    *
    * @param name The name of the step.
    * @param type The status type of the step.
    */
   private static void startStep(String name, StatusType type) {
      String uuid = UUID.randomUUID().toString();
      StepResult stepResult = new StepResult().setName(name);
      applyStepType(stepResult, type);

      Allure.getLifecycle().startStep(uuid, stepResult);
      STEP_STACK.get().push(uuid);
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
    * Stops the most recently started step.
    */
   public static void stopStep() {
      if (!STEP_STACK.get().isEmpty()) {
         String uuid = STEP_STACK.get().pop();
         Allure.getLifecycle().stopStep(uuid);
      }
   }

   /**
    * Checks if a specific parent step type is currently active.
    *
    * @param parentStepType The parent step type to check.
    * @return {@code true} if the specified parent step type is active, otherwise {@code false}.
    */
   public static boolean isParentStepActive(StepType parentStepType) {
      return parentStepType.getDisplayName().equals(PARENT_STEP_NAME.get());
   }

   /**
    * Associates a test ID with the current test execution.
    *
    * @param id The test ID to set.
    */
   public static void setTestId(String id) {
      TEST_ID.set(id);
   }

   /**
    * Clears the test ID associated with the current test execution.
    */
   public static void clearTestId() {
      TEST_ID.remove();
   }

   /**
    * Applies the appropriate Allure step status based on the provided {@link StatusType}.
    *
    * @param stepResult The step result to modify.
    * @param type       The status type to apply.
    */
   private static void applyStepType(StepResult stepResult, StatusType type) {
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
