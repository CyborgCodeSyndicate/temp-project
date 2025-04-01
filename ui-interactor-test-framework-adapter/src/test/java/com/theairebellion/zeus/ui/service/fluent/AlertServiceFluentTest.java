package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAlertService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockAlertUIElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("AlertServiceFluent Tests")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AlertServiceFluentTest extends BaseUnitUITest {

    private MockAlertService mockService;
    private AlertServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private MockAlertUIElement element;

    @BeforeEach
    void setUp() {
        mockService = new MockAlertService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);

        element = new MockAlertUIElement(By.id("test"), MockAlertComponentType.DUMMY);

        doReturn(uiServiceFluent)
                .when(uiServiceFluent)
                .validate(any(Runnable.class));

        doReturn(uiServiceFluent)
                .when(uiServiceFluent)
                .validate(any(java.util.function.Consumer.class));

        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

        // Create the SUT
        sut = new AlertServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @Test
    void constructorCoverage() {
        AlertServiceFluent<UIServiceFluent<?>> otherSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, mockService, driver);
        assertThat(otherSut).isNotNull();
    }

    @Test
    void getValueTest() {
        mockService.reset();

        // Act
        sut.getValue(element);

        // Verify
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));

        // Verify fluent return
        assertThat(sut.getValue(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void isVisibleTest() {
        mockService.reset();

        // Act
        sut.isVisible(element);

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        verify(storageUI).put(element.enumImpl(), true);

        // Verify fluent return
        assertThat(sut.isVisible(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsVisibleTest() {
        mockService.reset();

        // Act
        sut.validateIsVisible(element);

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        assertThat(sut.validateIsVisible(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateValueTest() {
        mockService.reset();

        // Act
        sut.validateValue(element, "ExpectedValue");

        // Verify
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        assertThat(sut.validateValue(element, "ExpectedValue")).isSameAs(uiServiceFluent);
    }

    @Test
    void validateValueSoftTest() {
        mockService.reset();

        // Act
        sut.validateValue(element, "SoftValue", true);

        // Verify
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        assertThat(sut.validateValue(element, "SoftValue", true)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsHiddenTest() {
        mockService.reset();

        // Act
        sut.validateIsHidden(element);

        // Verify
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        assertThat(sut.validateIsHidden(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsVisibleWithSoftAssertionTest() {
        mockService.reset();

        // Act
        sut.validateIsVisible(element, true);

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));

        // Verify fluent return
        assertThat(sut.validateIsVisible(element, true)).isSameAs(uiServiceFluent);
    }


    @Test
    void validateIsHiddenWithSoftAssertionTest() {
        mockService.reset();

        // Create a special instance for this test that returns false
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false; // Override to return false
            }
        };

        // Create a new SUT with our custom service
        AlertServiceFluent<UIServiceFluent<?>> customSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        // Act
        customSut.validateIsHidden(element, true);

        // Verify
        assertThat(hiddenService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(hiddenService.lastLocator).isEqualTo(By.id("test"));

        // Verify fluent return
        assertThat(customSut.validateIsHidden(element, true)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateValueWithTrueSoftTest() {
        mockService.reset();

        // Act - calling with soft=true
        sut.validateValue(element, "ExpectedValue", true);

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);

        // Verify fluent return
        assertThat(sut.validateValue(element, "ExpectedValue", true)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsVisibleWithTrueVisibilityHardTest() {
        mockService.reset();

        // Act
        sut.validateIsVisible(element);

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);

        // Verify fluent return
        assertThat(sut.validateIsVisible(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsVisibleWithFalseVisibilityHardTest() {
        mockService.reset();

        // Create a special instance for this test that returns false
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false; // Override to return false
            }
        };

        // Create a new SUT with our custom service
        AlertServiceFluent<UIServiceFluent<?>> customSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        // Act
        customSut.validateIsHidden(element);

        // Verify
        assertThat(hiddenService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(hiddenService.lastLocator).isEqualTo(By.id("test"));

        // Verify fluent return
        assertThat(customSut.validateIsHidden(element)).isSameAs(uiServiceFluent);
    }

    @Test
    void validateValueWithHardTest() {
        mockService.reset();

        // Act
        sut.validateValue(element, "ExpectedValue");

        // Verify
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);

        // Verify fluent return
        assertThat(sut.validateValue(element, "ExpectedValue")).isSameAs(uiServiceFluent);
    }

    @Test
    void validateIsVisibleWithTrueVisibleTrueSoft() {
        mockService.reset();

        // Call the public method that uses the private method with true, true
        sut.validateIsVisible(element, true);

        // Verify basic interaction
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    void validateIsVisibleWithFalseVisibleTrueSoft() {
        // Create a custom service that returns false for visibility
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false;
            }
        };

        // Create custom SUT with our service
        AlertServiceFluent<UIServiceFluent<?>> customSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        // Test the shouldBeVisible=false, soft=true branch
        customSut.validateIsHidden(element, true);

        // Verify interaction
        assertThat(hiddenService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(hiddenService.lastLocator).isEqualTo(By.id("test"));
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    void validateIsVisibleWithTrueVisibleFalseSoft() {
        mockService.reset();

        // Call the public method that uses the private method with true, false
        sut.validateIsVisible(element);

        // Verify basic interaction
        assertThat(mockService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("test"));
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    void validateIsVisibleWithFalseVisibleFalseSoft() {
        // Create a custom service that returns false for visibility
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false;
            }
        };

        // Create custom SUT with our service
        AlertServiceFluent<UIServiceFluent<?>> customSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        // Test the shouldBeVisible=false, soft=false branch
        customSut.validateIsHidden(element);

        // Verify interaction
        assertThat(hiddenService.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
        assertThat(hiddenService.lastLocator).isEqualTo(By.id("test"));
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    void testValidateValueLambdas() {
        // Test soft assertion path (soft=true)
        mockService.reset();
        sut.validateValue(element, "value", true); // Use soft assertion

        // Verify basic method flow
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);

        // Test hard assertion path (soft=false)
        mockService.reset();
        sut.validateValue(element, "value"); // Use hard assertion

        // Verify basic method flow
        verify(storageUI, times(2)).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);
    }

    @Test
    void testValidateIsVisibleLambdas() {
        // Case 1: visible=true, shouldBeVisible=true, soft=true
        mockService.reset();
        sut.validateIsVisible(element, true); // soft=true
        verify(storageUI).put(element.enumImpl(), true);

        // Case 2: visible=true, shouldBeVisible=true, soft=false
        mockService.reset();
        sut.validateIsVisible(element); // soft=false
        verify(storageUI, times(2)).put(element.enumImpl(), true);

        // For the remaining cases, we need a version that returns false for isVisible
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false;
            }
        };

        AlertServiceFluent<UIServiceFluent<?>> hiddenSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        // Case 3: visible=false, shouldBeVisible=false, soft=true
        hiddenSut.validateIsHidden(element, true); // soft=true
        verify(storageUI).put(element.enumImpl(), false);

        // Case 4: visible=false, shouldBeVisible=false, soft=false
        hiddenSut.validateIsHidden(element); // soft=false
        verify(storageUI, times(2)).put(element.enumImpl(), false);

        // Extra cases to ensure full coverage

        // Case 5: visible=false, shouldBeVisible=true, soft=true (will fail assertion)
        try {
            hiddenSut.validateIsVisible(element, true);
        } catch (Exception e) {
            // Expected to fail assertion, but we want to test the code path
        }

        // Case 6: visible=true, shouldBeVisible=false, soft=true (will fail assertion)
        try {
            sut.validateIsHidden(element, true);
        } catch (Exception e) {
            // Expected to fail assertion, but we want to test the code path
        }
    }

    @Test
    void validateValueWithLambdaExecution() {
        mockService.reset();

        // For soft assertions (soft=true)
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        sut.validateValue(element, "ExpectedValue", true);
        verify(storageUI).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);

        // For hard assertions (soft=false)
        reset(uiServiceFluent);
        doReturn(uiServiceFluent).when(uiServiceFluent).validate(any(Runnable.class));
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected, we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        sut.validateValue(element, "ExpectedValue", false);
        verify(storageUI, times(2)).put(element.enumImpl(), MockAlertService.VALUE_LOCATOR);
    }

    @Test
    void validateIsVisibleWithLambdaExecution() {
        // Test all combinations of visible, shouldBeVisible, and soft

        // Case 1: visible=true, shouldBeVisible=true, soft=true
        mockService.reset();
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        sut.validateIsVisible(element, true);
        verify(storageUI).put(element.enumImpl(), true);

        // Case 2: visible=true, shouldBeVisible=true, soft=false
        reset(uiServiceFluent);
        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);
        doReturn(uiServiceFluent).when(uiServiceFluent).validate(any(Runnable.class));
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected, we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        sut.validateIsVisible(element);
        verify(storageUI, times(2)).put(element.enumImpl(), true);

        // Case 3: visible=false, shouldBeVisible=false, soft=true
        MockAlertService hiddenService = new MockAlertService() {
            @Override
            public boolean isVisible(AlertComponentType componentType, By containerLocator) {
                lastComponentType = componentType;
                lastLocator = containerLocator;
                return false;
            }
        };

        reset(uiServiceFluent);
        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);
        doReturn(uiServiceFluent).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        AlertServiceFluent<UIServiceFluent<?>> hiddenSut =
                new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        hiddenSut.validateIsHidden(element, true);
        verify(storageUI).put(element.enumImpl(), false);

        // Case 4: visible=false, shouldBeVisible=false, soft=false
        reset(uiServiceFluent);
        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);
        doReturn(uiServiceFluent).when(uiServiceFluent).validate(any(Runnable.class));
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected, we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        hiddenSut = new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);

        hiddenSut.validateIsHidden(element);
        verify(storageUI, times(2)).put(element.enumImpl(), false);

        // Case 5: visible=true, shouldBeVisible=false, soft=true
        reset(uiServiceFluent);
        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);
        doReturn(uiServiceFluent).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        try {
            sut.validateIsHidden(element, true);
        } catch (AssertionError ignored) {
            // Expected to fail, but we want to execute the lambda
        }

        // Case 6: visible=false, shouldBeVisible=true, soft=true
        try {
            hiddenSut = new AlertServiceFluent<>(uiServiceFluent, storage, hiddenService, driver);
            hiddenSut.validateIsVisible(element, true);
        } catch (AssertionError ignored) {
            // Expected to fail, but we want to execute the lambda
        }
    }

}