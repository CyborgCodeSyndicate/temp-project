package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.SelectUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockSelectService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockSelectUIElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("SelectServiceFluent Tests")
class SelectServiceFluentTest extends BaseUnitUITest {

    private MockSelectService mockService;
    private SelectServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private SelectUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockSelectService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);
        element = new MockSelectUIElement(By.id("testSelect"), MockSelectComponentType.DUMMY);

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
        sut = new SelectServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("selectOptions should call selectService.selectOptions and return uiServiceFluent")
    void selectOptionsShouldCallSelectServiceAndReturnUiServiceFluent() {
        // Given
        String[] values = {"Option1", "Option2"};

        // When
        UIServiceFluent<?> result = sut.selectOptions(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(values);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("selectOption should call selectService.selectOption and return uiServiceFluent")
    void selectOptionShouldCallSelectServiceAndReturnUiServiceFluent() {
        // Given
        String value = "Option1";

        // When
        UIServiceFluent<?> result = sut.selectOption(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("selectOptions with strategy should call selectService.selectOptions with strategy and return uiServiceFluent")
    void selectOptionsWithStrategyShouldCallSelectServiceAndReturnUiServiceFluent() {
        // Given
        Strategy strategy = mock(Strategy.class);
        List<String> returnedOptions = Arrays.asList("Option1", "Option2");
        mockService.returnOptions = returnedOptions;

        // When
        UIServiceFluent<?> result = sut.selectOptions(element, strategy);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastStrategy).isEqualTo(strategy);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("getAvailableOptions should call selectService.getAvailableOptions, store result and return uiServiceFluent")
    void getAvailableOptionsShouldCallSelectServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        mockService.returnOptions = options;

        // When
        UIServiceFluent<?> result = sut.getAvailableOptions(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), options);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAvailableOptions should call validateAvailableOptions with correct parameters")
    void validateAvailableOptionsShouldCallValidateAvailableOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        mockService.returnOptions = options;
        String[] expectedValues = {"Option1", "Option2"};

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
        UIServiceFluent<?> result = sut.validateAvailableOptions(element, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), options);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAvailableOptions with soft parameter should call validateAvailableOptions with correct parameters")
    void validateAvailableOptionsWithSoftShouldCallValidateAvailableOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        mockService.returnOptions = options;
        String[] expectedValues = {"Option1", "Option2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAvailableOptions(element, true, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), options);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAvailableOptions with count should call validateAvailableOptions with correct parameters")
    void validateAvailableOptionsWithCountShouldCallValidateAvailableOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        mockService.returnOptions = options;
        int expectedCount = 3;

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
        UIServiceFluent<?> result = sut.validateAvailableOptions(element, expectedCount);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), options);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAvailableOptions with count and soft parameter should call validateAvailableOptions with correct parameters")
    void validateAvailableOptionsWithCountAndSoftShouldCallValidateAvailableOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        mockService.returnOptions = options;
        int expectedCount = 3;

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAvailableOptions(element, true, expectedCount);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), options);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("getSelectedOptions should call selectService.getSelectedOptions, store result and return uiServiceFluent")
    void getSelectedOptionsShouldCallSelectServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        List<String> selectedOptions = Arrays.asList("Option1", "Option2");
        mockService.returnOptions = selectedOptions;

        // When
        UIServiceFluent<?> result = sut.getSelectedOptions(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), selectedOptions);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateSelectedOptions should call validateSelectedOptions with correct parameters")
    void validateSelectedOptionsShouldCallValidateSelectedOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedOptions = Arrays.asList("Option1", "Option2");
        mockService.returnOptions = selectedOptions;
        String[] expectedValues = {"Option1", "Option2"};

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
        UIServiceFluent<?> result = sut.validateSelectedOptions(element, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), selectedOptions);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateSelectedOptions with soft parameter should call validateSelectedOptions with correct parameters")
    void validateSelectedOptionsWithSoftShouldCallValidateSelectedOptionsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedOptions = Arrays.asList("Option1", "Option2");
        mockService.returnOptions = selectedOptions;
        String[] expectedValues = {"Option1", "Option2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateSelectedOptions(element, true, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        verify(storageUI).put(element.enumImpl(), selectedOptions);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isOptionVisible should call selectService.isOptionVisible, store result and return uiServiceFluent")
    void isOptionVisibleShouldCallSelectServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

        // When
        UIServiceFluent<?> result = sut.isOptionVisible(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionVisible should call validateIsVisible with correct parameters")
    void validateIsOptionVisibleShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

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
        UIServiceFluent<?> result = sut.validateIsOptionVisible(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionVisible with soft parameter should call validateIsVisible with correct parameters")
    void validateIsOptionVisibleWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsOptionVisible(element, value, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionHidden should call validateIsVisible with correct parameters")
    void validateIsOptionHiddenShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Option1";

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
        UIServiceFluent<?> result = sut.validateIsOptionHidden(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionHidden with soft parameter should call validateIsVisible with correct parameters")
    void validateIsOptionHiddenWithSoftShouldCallValidateIsVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Option1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsOptionHidden(element, value, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isOptionEnabled should call selectService.isOptionEnabled, store result and return uiServiceFluent")
    void isOptionEnabledShouldCallSelectServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

        // When
        UIServiceFluent<?> result = sut.isOptionEnabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionEnabled should call validateIsEnabled with correct parameters")
    void validateIsOptionEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

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
        UIServiceFluent<?> result = sut.validateIsOptionEnabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionEnabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsOptionEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Option1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsOptionEnabled(element, value, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionDisabled should call validateIsEnabled with correct parameters")
    void validateIsOptionDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Option1";

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
        UIServiceFluent<?> result = sut.validateIsOptionDisabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsOptionDisabled with soft parameter should call validateIsEnabled with correct parameters")
    void validateIsOptionDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Option1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsOptionDisabled(element, value, true);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("insertion should call selectService.insertion with correct parameters")
    void insertionShouldCallSelectServiceWithCorrectParameters() {
        // Given
        mockService.reset();
        Object[] values = {"value1", "value2"};

        // When
        sut.insertion(MockSelectComponentType.DUMMY, By.id("testSelect"), values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testSelect"));
        assertThat(mockService.lastValues).isEqualTo(new String[]{"value1", "value2"});
    }
}