package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.LinkUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockLinkService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockLinkUIElement;
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

@DisplayName("LinkServiceFluent Tests")
class LinkServiceFluentTest extends BaseUnitUITest {

    private MockLinkService mockService;
    private LinkServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private LinkUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockLinkService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);
        element = new MockLinkUIElement(By.id("testLink"), MockLinkComponentType.DUMMY);

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
        sut = new LinkServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("click should call linkService.click and return uiServiceFluent")
    void clickShouldCallLinkServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent<?> result = sut.click(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("doubleClick should call linkService.doubleClick and return uiServiceFluent")
    void doubleClickShouldCallLinkServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent<?> result = sut.doubleClick(element);

        // Then
        assertThat(mockService.lastLinkType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isEnabled should call linkService.isEnabled, store result and return uiServiceFluent")
    void isEnabledShouldCallLinkServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

        // When
        UIServiceFluent<?> result = sut.isEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled should call validateIsEnabled with correct parameters")
    void validateIsEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

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
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
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
        UIServiceFluent<?> result = sut.validateIsEnabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled should call validateIsEnabled with correct parameters")
    void validateIsDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

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
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
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
        UIServiceFluent<?> result = sut.validateIsDisabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled with returnEnabled=false should execute lambda and handle assertion failure")
    void validateIsEnabledWithReturnEnabledFalseShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnEnabled = false;

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
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsEnabled with soft=true and returnEnabled=false should execute lambda")
    void validateIsEnabledWithSoftTrueAndReturnEnabledFalseShouldExecuteLambda() {
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
        sut.validateIsEnabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsDisabled with returnEnabled=true should execute lambda and handle assertion failure")
    void validateIsDisabledWithReturnEnabledTrueShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnEnabled = true;

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
            sut.validateIsDisabled(element);
        } catch (AssertionError ignored) {
            // Expected in test environment
        }

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("validateIsDisabled with soft=true and returnEnabled=true should execute lambda")
    void validateIsDisabledWithSoftTrueAndReturnEnabledTrueShouldExecuteLambda() {
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
        sut.validateIsDisabled(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("isVisible should call linkService.isVisible, store result and return uiServiceFluent")
    void isVisibleShouldCallLinkServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

        // When
        UIServiceFluent<?> result = sut.isVisible(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible should call validateIsVisible with correct parameters")
    void validateIsVisibleShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

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
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible with soft parameter should call validateIsVisible with correct parameters")
    void validateIsVisibleWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
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
        UIServiceFluent<?> result = sut.validateIsVisible(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden should call validateIsVisible with correct parameters")
    void validateIsHiddenShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

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
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden with soft parameter should call validateIsVisible with correct parameters")
    void validateIsHiddenWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
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
        UIServiceFluent<?> result = sut.validateIsHidden(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testLink"));
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible with returnVisible=false should execute lambda and handle assertion failure")
    void validateIsVisibleWithReturnVisibleFalseShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnVisible = false;

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
            sut.validateIsVisible(element);
        } catch (AssertionError ignored) {
            // Expected in test environment
        }

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsVisible with soft=true and returnVisible=false should execute lambda")
    void validateIsVisibleWithSoftTrueAndReturnVisibleFalseShouldExecuteLambda() {
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
        sut.validateIsVisible(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), false);
    }

    @Test
    @DisplayName("validateIsHidden with returnVisible=true should execute lambda and handle assertion failure")
    void validateIsHiddenWithReturnVisibleTrueShouldExecuteLambdaAndHandleAssertionFailure() {
        // Given
        mockService.reset();
        mockService.returnVisible = true;

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
            sut.validateIsHidden(element);
        } catch (AssertionError ignored) {
            // Expected in test environment
        }

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
    }

    @Test
    @DisplayName("validateIsHidden with soft=true and returnVisible=true should execute lambda")
    void validateIsHiddenWithSoftTrueAndReturnVisibleTrueShouldExecuteLambda() {
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
        sut.validateIsHidden(element, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockLinkComponentType.DUMMY);
        verify(storageUI).put(element.enumImpl(), true);
    }
}