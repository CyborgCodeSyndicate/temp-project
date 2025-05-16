package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAllureListenerTest {

    private AllureLifecycle lifecycleMock;

    @BeforeEach
    void setUp() {
        lifecycleMock = mock(AllureLifecycle.class);
        Allure.setLifecycle(lifecycleMock);
    }

    @ParameterizedTest
    @EnumSource(CustomAllureListener.StatusType.class)
    @DisplayName("Should set correct Allure status based on StatusType enum")
    void testStartStepWithStatusType_ShouldSetCorrectStatus(CustomAllureListener.StatusType inputType) {
        // Given
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // When
        CustomAllureListener.startStepWithStatusType("Test Step", inputType);

        // Then
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();

        Status expectedStatus = getExpectedStatus(inputType);
        assertEquals(expectedStatus, captured.getStatus(),
                "Expected status " + expectedStatus + " for input type " + inputType + ", but got " + captured.getStatus());
    }

    @Test
    @DisplayName("Should set null status when input StatusType is null")
    void testStartStepWithStatusType_ShouldSetNullStatusForNullInput() {
        // Given
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // When
        CustomAllureListener.startStepWithStatusType("Test Step", null);

        // Then
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();
        assertNull(captured.getStatus(), "Expected status to be null for null input, but got " + captured.getStatus());
    }

    @Test
    @DisplayName("Should return correct active step name after starting a step")
    void testGetActiveStepName_ShouldReturnCorrectStepName() {
        // Given
        String expectedStepName = "Sample Step";

        // When
        CustomAllureListener.startStep(expectedStepName);
        String actualStepName = CustomAllureListener.getActiveStepName();

        // Then
        assertEquals(expectedStepName, actualStepName,
                "Expected step name to be '" + expectedStepName + "' but got '" + actualStepName + "'");
    }

    private Status getExpectedStatus(CustomAllureListener.StatusType inputType) {
        if (inputType == CustomAllureListener.StatusType.DEFAULT) {
            return null;
        } else if (inputType == CustomAllureListener.StatusType.INFO) {
            return Status.SKIPPED;
        } else if (inputType == CustomAllureListener.StatusType.SUCCESS) {
            return Status.PASSED;
        } else if (inputType == CustomAllureListener.StatusType.WARNING) {
            return Status.BROKEN;
        } else if (inputType == CustomAllureListener.StatusType.ERROR) {
            return Status.FAILED;
        }
        return null;
    }
}