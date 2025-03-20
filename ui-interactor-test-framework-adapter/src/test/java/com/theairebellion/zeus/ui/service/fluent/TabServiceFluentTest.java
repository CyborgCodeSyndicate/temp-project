package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.TabUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockTabService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockTabUIElement;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("TabServiceFluent Tests")
class TabServiceFluentTest extends BaseUnitUITest {

    private MockTabService mockService;
    private TabServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private TabUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockTabService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);
        element = new MockTabUIElement(By.id("testTab"), MockTabComponentType.DUMMY);

        doReturn(uiServiceFluent)
                .when(uiServiceFluent)
                .validate(any(Runnable.class));

        doReturn(uiServiceFluent)
                .when(uiServiceFluent)
                .validate(any(java.util.function.Consumer.class));

        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

        // Mock Allure steps
        allureMock = mockStatic(Allure.class);

        // Create the SUT
        sut = new TabServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("click should call tabService.click and return uiServiceFluent")
    void clickShouldCallTabServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent<?> result = sut.click(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isEnabled should call tabService.isEnabled, store result and return uiServiceFluent")
    void isEnabledShouldCallTabServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // When
        UIServiceFluent<?> result = sut.isEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled should call validateIsEnabled with correct parameters")
    void validateIsEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsEnabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled should call validateIsEnabled with correct parameters")
    void validateIsDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsDisabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsDisabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled with returnBool=false should execute lambda and handle assertion failure")
    void validateIsEnabledWithReturnBoolFalseShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected to fail in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        try {
            sut.validateIsEnabled(element);
        } catch (AssertionError ignored) {
            // Expected in test environment
        }

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsEnabled with soft=true and returnBool=false should execute lambda")
    void validateIsEnabledWithSoftTrueAndReturnBoolFalseShouldExecuteLambda() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsEnabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("isVisible should call tabService.isVisible, store result and return uiServiceFluent")
    void isVisibleShouldCallTabServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // When
        UIServiceFluent<?> result = sut.isVisible(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible should call validateIsVisible with correct parameters")
    void validateIsVisibleShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsVisible(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible with soft parameter should call validateIsVisible with correct parameters")
    void validateIsVisibleWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsVisible(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden should call validateIsVisible with correct parameters")
    void validateIsHiddenShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsHidden(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden with soft parameter should call validateIsVisible with correct parameters")
    void validateIsHiddenWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsHidden(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isSelected should call tabService.isSelected, store result and return uiServiceFluent")
    void isSelectedShouldCallTabServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // When
        UIServiceFluent<?> result = sut.isSelected(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsSelected should call validateIsSelected with correct parameters")
    void validateIsSelectedShouldCallValidateIsSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsSelected(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsSelected with soft parameter should call validateIsSelected with correct parameters")
    void validateIsSelectedWithSoftShouldCallValidateIsSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsSelected(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsNotSelected should call validateIsSelected with correct parameters")
    void validateIsNotSelectedShouldCallValidateIsSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        UIServiceFluent<?> result = sut.validateIsNotSelected(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsNotSelected with soft parameter should call validateIsSelected with correct parameters")
    void validateIsNotSelectedWithSoftShouldCallValidateIsSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsNotSelected(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testTab"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsSelected with returnBool=false should execute lambda and handle assertion failure")
    void validateIsSelectedWithReturnBoolFalseShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Expected to fail in test environment
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        try {
            sut.validateIsSelected(element);
        } catch (AssertionError ignored) {
            // Expected in test environment
        }

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsSelected with soft=true and returnBool=false should execute lambda")
    void validateIsSelectedWithSoftTrueAndReturnBoolFalseShouldExecuteLambda() {
        // Given
        mockService.reset();
        mockService.returnBool = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsSelected(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockTabComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }
}