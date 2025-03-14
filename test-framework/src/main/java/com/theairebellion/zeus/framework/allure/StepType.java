package com.theairebellion.zeus.framework.allure;

import lombok.Getter;

@Getter
public enum StepType {
    TEST_EXECUTION("Test Execution"),
    TEAR_DOWN("Tear Down"),
    PROCESSING_PRE_QUESTS("Processing Pre-Quests"),
    PERFORMING_PRE_QUEST_AUTHENTICATION("Performing Pre-Quest Authentication"),
    PROCESSING_PRE_QUEST("Processing preQuestJourney");

    private final String displayName;

    StepType(String displayName) {
        this.displayName = displayName;
    }

}