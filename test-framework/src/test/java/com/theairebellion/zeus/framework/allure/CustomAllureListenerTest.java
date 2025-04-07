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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomAllureListenerTest {

    private AllureLifecycle lifecycleMock;

    @BeforeEach
    void setUp() {
        lifecycleMock = mock(AllureLifecycle.class);
        Allure.setLifecycle(lifecycleMock);
    }

    @Test
    void testStartAndStopStep_ShouldUseSameUuid() {
        ArgumentCaptor<String> uuidCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        CustomAllureListener.startStep("My Step");
        CustomAllureListener.stopStep();

        // Assert
        verify(lifecycleMock, times(1)).startStep(uuidCaptor.capture(), any(StepResult.class));
        verify(lifecycleMock, times(1)).stopStep(uuidCaptor.capture());

        List<String> capturedUuids = uuidCaptor.getAllValues();
        assertEquals(2, capturedUuids.size(), "Expected one UUID for start and one for stop");
        assertEquals(capturedUuids.get(0), capturedUuids.get(1), "UUID should be the same for start and stop step");
    }

    @ParameterizedTest
    @EnumSource(StepType.class)
    void testStartStep_ShouldUseDisplayNameFromStepType(StepType testStepType) {
        ArgumentCaptor<StepResult> stepResultCaptor = ArgumentCaptor.forClass(StepResult.class);

        // Act
        CustomAllureListener.startStep(testStepType);

        // Assert
        verify(lifecycleMock).startStep(anyString(), stepResultCaptor.capture());
        StepResult capturedResult = stepResultCaptor.getValue();
        assertEquals(testStepType.getDisplayName(), capturedResult.getName(),
                "StepResult should have name matching the StepType's display name for " + testStepType);
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

    @ParameterizedTest
    @EnumSource(CustomAllureListener.StatusType.class)
    void testStartParentStepWithStatusType_ShouldSetCorrectStatus(CustomAllureListener.StatusType inputType) {
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // Act
        CustomAllureListener.startParentStepWithStatusType("Parent Step", inputType);

        // Assert
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();

        Status expectedStatus = getExpectedStatus(inputType);
        assertEquals(expectedStatus, captured.getStatus());
    }

    @Test
    void testStartParentStepWithStatusType_ShouldSetNullStatusForNullInput() {
        ArgumentCaptor<StepResult> stepCaptor = ArgumentCaptor.forClass(StepResult.class);

        // Act
        CustomAllureListener.startParentStepWithStatusType("Test Step", null);

        // Assert
        verify(lifecycleMock).startStep(anyString(), stepCaptor.capture());
        StepResult captured = stepCaptor.getValue();
        assertNull(captured.getStatus());
    }

    @Test
    void testIsParentStepActive_ShouldReturnCorrectResult() {
        String activeStepName = "Active Parent Step";
        StepType activeParentStep = mock(StepType.class);
        StepType inactiveParentStep = mock(StepType.class);

        // Configure mocks
        when(activeParentStep.getDisplayName()).thenReturn(activeStepName);
        when(inactiveParentStep.getDisplayName()).thenReturn("Different Step Name");

        // Act
        CustomAllureListener.startParentStep(activeParentStep);

        // Assert
        assertTrue(CustomAllureListener.isParentStepActive(activeParentStep), "Should return true when step name matches active parent");
        assertFalse(CustomAllureListener.isParentStepActive(inactiveParentStep), "Should return false when step name doesn't match active parent");
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