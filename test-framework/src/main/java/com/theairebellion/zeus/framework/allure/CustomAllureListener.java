package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

public class CustomAllureListener extends AllureJunit5 {

    private static final ThreadLocal<Deque<String>> STEP_STACK = ThreadLocal.withInitial(LinkedList::new);
    private static final ThreadLocal<String> PARENT_STEP = new ThreadLocal<>();
    private static final ThreadLocal<String> PARENT_STEP_NAME = new ThreadLocal<>();
    private static final ThreadLocal<String> TEST_ID = new ThreadLocal<>();

    public enum StatusType {
        DEFAULT,
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }

    private static void startParentStep(String name, StatusType type) {
        String uuid = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(name);
        applyStepType(stepResult, type);

        Allure.getLifecycle().startStep(uuid, stepResult);
        PARENT_STEP.set(uuid);
        PARENT_STEP_NAME.set(name);
    }

    public static void startParentStep(StepType parentStepType) {
        startParentStep(parentStepType.getDisplayName(), StatusType.DEFAULT);
    }

    public static void stopParentStep() {
        if (PARENT_STEP.get() != null) {
            Allure.getLifecycle().stopStep(PARENT_STEP.get());
            PARENT_STEP.remove();
            PARENT_STEP_NAME.remove();
        }
    }

    private static void startStep(String name, StatusType type) {
        String uuid = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(name);
        applyStepType(stepResult, type);

        Allure.getLifecycle().startStep(uuid, stepResult);
        STEP_STACK.get().push(uuid);
    }

    public static void startStep(StepType stepType) {
        startStep(stepType.getDisplayName(), StatusType.DEFAULT);
    }

    public static void startStep(String stepName) {
        startStep(stepName, StatusType.DEFAULT);
    }

    public static void stopStep() {
        if (!STEP_STACK.get().isEmpty()) {
            String uuid = STEP_STACK.get().pop();
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    public static boolean isParentStepActive(StepType parentStepType) {
        return parentStepType.getDisplayName().equals(PARENT_STEP_NAME.get());
    }

    public static void setTestId(String id) {
        TEST_ID.set(id);
    }

    public static void clearTestId() {
        TEST_ID.remove();
    }

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