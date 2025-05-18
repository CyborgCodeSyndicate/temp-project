package com.theairebellion.zeus.framework.allure;

import lombok.Getter;

/**
 * Defines standardized step types for structured Allure reporting.
 *
 * <p>This enumeration provides predefined step categories for tracking and organizing Allure steps.
 * It ensures consistency in test execution logging and enhances readability in reports.
 *
 * <p>Each step type has an associated display name used for reporting.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public enum StepType {

   /**
    * Represents the overall test execution phase.
    */
   TEST_EXECUTION("Test Execution"),

   /**
    * Represents the teardown phase after test execution.
    */
   TEAR_DOWN("Tear Down"),

   /**
    * Represents the pre-processing phase before executing API requests.
    */
   PROCESSING_PRE_QUESTS("Processing Pre-Quests"),

   /**
    * Represents the authentication process required before executing pre-quest operations.
    */
   PERFORMING_PRE_QUEST_AUTHENTICATION("Performing Pre-Quest Authentication"),

   /**
    * Represents the pre-processing journey for a specific request.
    */
   PROCESSING_PRE_QUEST("Processing preQuestJourney");

   /**
    * The display name of the step type, used in Allure reports.
    */
   private final String displayName;

   /**
    * Constructs a {@code StepType} with the specified display name.
    *
    * @param displayName The human-readable name of the step type.
    */
   StepType(String displayName) {
      this.displayName = displayName;
   }

}
