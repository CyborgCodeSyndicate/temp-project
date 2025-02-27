package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Supplier;

public class CustomAllureListener extends AllureJunit5 implements BeforeEachCallback {

    private static final ThreadLocal<Deque<String>> STEP_STACK = ThreadLocal.withInitial(LinkedList::new);
    private static final ThreadLocal<String> PARENT_STEP = new ThreadLocal<>();

    public enum StepType {
        DEFAULT,
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Allure.getLifecycle().updateTestCase(tc -> tc.setName(context.getDisplayName()));
    }

    public static void startParentStep(String name, StepType type) {
        String uuid = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(name);
        applyStepType(stepResult, type);

        Allure.getLifecycle().startStep(uuid, stepResult);
        PARENT_STEP.set(uuid);
    }

    public static void startParentStep(String name) {
        startParentStep(name, StepType.DEFAULT);
    }

    public static void stopParentStep() {
        if (PARENT_STEP.get() != null) {
            Allure.getLifecycle().stopStep(PARENT_STEP.get());
            PARENT_STEP.remove();
        }
    }

    public static void startStep(String name, StepType type) {
        String uuid = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(name);
        applyStepType(stepResult, type);

        Allure.getLifecycle().startStep(uuid, stepResult);
        STEP_STACK.get().push(uuid);
    }

    public static void startStep(String name) {
        startStep(name, StepType.DEFAULT);
    }

    public static void stopStep() {
        if (!STEP_STACK.get().isEmpty()) {
            String uuid = STEP_STACK.get().pop();
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    public static <T> T runAsStep(String stepName, StepType type, Supplier<T> code) {
        String stepId = UUID.randomUUID().toString();
        StepResult stepResult = new StepResult().setName(stepName);
        applyStepType(stepResult, type);

        Allure.getLifecycle().startStep(stepId, stepResult);
        try {
            return code.get();
        } finally {
            Allure.getLifecycle().stopStep(stepId);
        }
    }

    public static <T> T runAsStep(String stepName, Supplier<T> code) {
        return runAsStep(stepName, StepType.DEFAULT, code);
    }

    private static void applyStepType(StepResult stepResult, StepType type) {
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