package com.theairebellion.zeus.framework.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomAllureListenerTest {

    private AllureLifecycle lifecycleMock;

    @BeforeEach
    void setUp() {
        lifecycleMock = mock(AllureLifecycle.class);
        Allure.setLifecycle(lifecycleMock);
    }

    @ParameterizedTest
    @EnumSource(CustomAllureListener.StatusType.class)
    void testStartStepWithStatusType_ShouldSetCorrectStatus(CustomAllureListener.StatusType inputType) {
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // Act
        CustomAllureListener.startStepWithStatusType("Test Step", inputType);

        // Assert
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();

        Status expectedStatus = getExpectedStatus(inputType);
        assertEquals(expectedStatus, captured.getStatus());
    }

    @Test
    void testStartStepWithStatusType_ShouldSetNullStatusForNullInput() {
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // Act
        CustomAllureListener.startStepWithStatusType("Test Step", null);

        // Assert
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();
        assertNull(captured.getStatus());
    }

    @Test
    void testGetActiveStepName_ShouldReturnCorrectStepName() {
        // Arrange
        String expectedStepName = "Sample Step";
        CustomAllureListener.startStep(expectedStepName);

        // Act
        String actualStepName = CustomAllureListener.getActiveStepName();

        // Assert
        assertEquals(expectedStepName, actualStepName, "getActiveStepName should return the correct step name");
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