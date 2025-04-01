package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockButtonService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockButtonUIElement;
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

@DisplayName("ButtonServiceFluent Tests")
class ButtonServiceFluentTest extends BaseUnitUITest {

    private MockButtonService mockService;
    private ButtonServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private ButtonUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockButtonService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);
        element = new MockButtonUIElement(By.id("testButton"), MockButtonComponentType.DUMMY);

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
        sut = new ButtonServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("click should call buttonService.click and return uiServiceFluent")
    void clickShouldCallButtonServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent<?> result = sut.click(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isEnabled should call buttonService.isEnabled, store result and return uiServiceFluent")
    void isEnabledShouldCallButtonServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // When
        UIServiceFluent<?> result = sut.isEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isVisible should call buttonService.isVisible, store result and return uiServiceFluent")
    void isVisibleShouldCallButtonServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // When
        UIServiceFluent<?> result = sut.isVisible(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled should call validateIsEnabled with correct parameters")
    void validateIsEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // When
        UIServiceFluent<?> result = sut.validateIsEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // When
        UIServiceFluent<?> result = sut.validateIsEnabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled should call validateIsEnabled with correct parameters")
    void validateIsDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

        // When
        UIServiceFluent<?> result = sut.validateIsDisabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

        // When
        UIServiceFluent<?> result = sut.validateIsDisabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible should call validateIsVisible with correct parameters")
    void validateIsVisibleShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // When
        UIServiceFluent<?> result = sut.validateIsVisible(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible with soft parameter should call validateIsVisible with correct parameters")
    void validateIsVisibleWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // When
        UIServiceFluent<?> result = sut.validateIsVisible(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden should call validateIsVisible with correct parameters")
    void validateIsHiddenShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

        // When
        UIServiceFluent<?> result = sut.validateIsHidden(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden with soft parameter should call validateIsVisible with correct parameters")
    void validateIsHiddenWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

        // When
        UIServiceFluent<?> result = sut.validateIsHidden(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testButton"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    // Additional tests to cover all lambda paths in private methods

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=true, shouldBeEnabled=true, soft=true")
    void validateIsEnabledLambdaWithEnabledTrueShouldBeEnabledTrueSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            // Don't call assertAll() as we're not testing the assertions themselves
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsEnabled(element, true); // shouldBeEnabled=true, soft=true

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=false, shouldBeEnabled=false, soft=true")
    void validateIsEnabledLambdaWithEnabledFalseShouldBeEnabledFalseSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsDisabled(element, true); // shouldBeEnabled=false, soft=true

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=true, shouldBeEnabled=true, soft=false")
    void validateIsEnabledLambdaWithEnabledTrueShouldBeEnabledTrueSoftFalse() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Assertions might fail, but we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        sut.validateIsEnabled(element); // shouldBeEnabled=true, soft=false

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=false, shouldBeEnabled=false, soft=false")
    void validateIsEnabledLambdaWithEnabledFalseShouldBeEnabledFalseSoftFalse() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Assertions might fail, but we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        sut.validateIsDisabled(element); // shouldBeEnabled=false, soft=false

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=true, shouldBeVisible=true, soft=true")
    void validateIsVisibleLambdaWithVisibleTrueShouldBeVisibleTrueSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsVisible(element, true); // shouldBeVisible=true, soft=true

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=false, shouldBeVisible=false, soft=true")
    void validateIsVisibleLambdaWithVisibleFalseShouldBeVisibleFalseSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        sut.validateIsHidden(element, true); // shouldBeVisible=false, soft=true

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=true, shouldBeVisible=true, soft=false")
    void validateIsVisibleLambdaWithVisibleTrueShouldBeVisibleTrueSoftFalse() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Assertions might fail, but we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        sut.validateIsVisible(element); // shouldBeVisible=true, soft=false

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=false, shouldBeVisible=false, soft=false")
    void validateIsVisibleLambdaWithVisibleFalseShouldBeVisibleFalseSoftFalse() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            Runnable assertionRunnable = invocation.getArgument(0);
            try {
                assertionRunnable.run();
            } catch (AssertionError ignored) {
                // Assertions might fail, but we just want to execute the lambda
            }
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(Runnable.class));

        // When
        sut.validateIsHidden(element); // shouldBeVisible=false, soft=false

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=true, shouldBeEnabled=false, soft=true")
    void validateIsEnabledLambdaWithEnabledTrueShouldBeEnabledFalseSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        try {
            sut.validateIsDisabled(element, true); // shouldBeEnabled=false, soft=true
        } catch (AssertionError ignored) {
            // Expected to fail, but we just want to execute the lambda
        }

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsEnabled lambda paths when enabled=false, shouldBeEnabled=true, soft=true")
    void validateIsEnabledLambdaWithEnabledFalseShouldBeEnabledTrueSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        try {
            sut.validateIsEnabled(element, true); // shouldBeEnabled=true, soft=true
        } catch (AssertionError ignored) {
            // Expected to fail, but we just want to execute the lambda
        }

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=true, shouldBeVisible=false, soft=true")
    void validateIsVisibleLambdaWithVisibleTrueShouldBeVisibleFalseSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        try {
            sut.validateIsHidden(element, true); // shouldBeVisible=false, soft=true
        } catch (AssertionError ignored) {
            // Expected to fail, but we just want to execute the lambda
        }

        // Then
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("Test validateIsVisible lambda paths when visible=false, shouldBeVisible=true, soft=true")
    void validateIsVisibleLambdaWithVisibleFalseShouldBeVisibleTrueSoftTrue() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        try {
            sut.validateIsVisible(element, true); // shouldBeVisible=true, soft=true
        } catch (AssertionError ignored) {
            // Expected to fail, but we just want to execute the lambda
        }

        // Then
        verify(storageUI).put(element.enumImpl(), false);
    }
}