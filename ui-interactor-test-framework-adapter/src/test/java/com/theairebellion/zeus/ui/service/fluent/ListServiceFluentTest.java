package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.ListUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockItemListService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockItemListUIElement;
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

@DisplayName("ListServiceFluent Tests")
class ListServiceFluentTest extends BaseUnitUITest {

    private MockItemListService mockService;
    private ListServiceFluent<UIServiceFluent<?>> sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent<?> uiServiceFluent;
    private SmartWebDriver driver;
    private ListUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockItemListService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        driver = mock(SmartWebDriver.class);
        element = new MockItemListUIElement(By.id("testList"), MockItemListComponentType.DUMMY);

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
        sut = new ListServiceFluent<>(uiServiceFluent, storage, mockService, driver);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("select should call itemListService.select and return uiServiceFluent")
    void selectShouldCallItemListServiceAndReturnUiServiceFluent() {
        // Given
        String[] values = {"Item1", "Item2"};

        // When
        UIServiceFluent<?> result = sut.select(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("deSelect should call itemListService.deSelect and return uiServiceFluent")
    void deSelectShouldCallItemListServiceAndReturnUiServiceFluent() {
        // Given
        String[] values = {"Item1", "Item2"};

        // When
        UIServiceFluent<?> result = sut.deSelect(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("areSelected should call itemListService.areSelected, store result and return uiServiceFluent")
    void areSelectedShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

        // When
        UIServiceFluent<?> result = sut.areSelected(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreSelected should call validateAreSelected with correct parameters")
    void validateAreSelectedShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreSelected(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreSelected with soft parameter should call validateAreSelected with correct parameters")
    void validateAreSelectedWithSoftShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAreSelected(element, true, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreNotSelected should call validateAreSelected with correct parameters")
    void validateAreNotSelectedShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreNotSelected(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreNotSelected with soft parameter should call validateAreSelected with correct parameters")
    void validateAreNotSelectedWithSoftShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String[] values = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAreNotSelected(element, true, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isSelected should call itemListService.isSelected, store result and return uiServiceFluent")
    void isSelectedShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // When
        UIServiceFluent<?> result = sut.isSelected(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsSelected should call validateAreSelected with correct parameters")
    void validateIsSelectedShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsSelected(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsSelected with soft parameter should call validateAreSelected with correct parameters")
    void validateIsSelectedWithSoftShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsSelected(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsNotSelected should call validateAreSelected with correct parameters")
    void validateIsNotSelectedShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsNotSelected(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsNotSelected with soft parameter should call validateAreSelected with correct parameters")
    void validateIsNotSelectedWithSoftShouldCallValidateAreSelectedWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsNotSelected(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("areEnabled should call itemListService.areEnabled, store result and return uiServiceFluent")
    void areEnabledShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

        // When
        UIServiceFluent<?> result = sut.areEnabled(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreEnabled should call validateAreEnabled with correct parameters")
    void validateAreEnabledShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreEnabled(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreEnabled with soft parameter should call validateAreEnabled with correct parameters")
    void validateAreEnabledWithSoftShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAreEnabled(element, true, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreDisabled should call validateAreEnabled with correct parameters")
    void validateAreDisabledShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreDisabled(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreDisabled with soft parameter should call validateAreEnabled with correct parameters")
    void validateAreDisabledWithSoftShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String[] values = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAreDisabled(element, true, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isEnabled should call itemListService.isEnabled, store result and return uiServiceFluent")
    void isEnabledShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // When
        UIServiceFluent<?> result = sut.isEnabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("areVisible should call itemListService.areVisible, store result and return uiServiceFluent")
    void areVisibleShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

        // When
        UIServiceFluent<?> result = sut.areVisible(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreVisible should call validateAreVisible with correct parameters")
    void validateAreVisibleShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreVisible(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAreHidden should call validateAreVisible with correct parameters")
    void validateAreHiddenShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String[] values = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAreHidden(element, values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(values);
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isVisible should call itemListService.isVisible, store result and return uiServiceFluent")
    void isVisibleShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // When
        UIServiceFluent<?> result = sut.isVisible(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("getSelected should call itemListService.getSelected, store result and return uiServiceFluent")
    void getSelectedShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item1", "Item2");
        mockService.returnSelectedList = selectedItems;

        // When
        UIServiceFluent<?> result = sut.getSelected(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateSelectedItems should call validateSelectedItems with correct parameters")
    void validateSelectedItemsShouldCallValidateSelectedItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item1", "Item2");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateSelectedItems(element, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateSelectedItems with soft parameter should call validateSelectedItems with correct parameters")
    void validateSelectedItemsWithSoftShouldCallValidateSelectedItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item1", "Item2");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateSelectedItems(element, true, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateNotSelectedItems should call validateSelectedItems with correct parameters")
    void validateNotSelectedItemsShouldCallValidateSelectedItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item3", "Item4");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateNotSelectedItems(element, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateNotSelectedItems with soft parameter should call validateSelectedItems with correct parameters")
    void validateNotSelectedItemsWithSoftShouldCallValidateSelectedItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item3", "Item4");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateNotSelectedItems(element, true, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("getAll should call itemListService.getAll, store result and return uiServiceFluent")
    void getAllShouldCallItemListServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        List<String> allItems = Arrays.asList("Item1", "Item2", "Item3");
        mockService.returnAllList = allItems;

        // When
        UIServiceFluent<?> result = sut.getAll(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), allItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAllItems should call validateAllItems with correct parameters")
    void validateAllItemsShouldCallValidateAllItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item1", "Item2", "Item3");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

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
        UIServiceFluent<?> result = sut.validateAllItems(element, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateAllItems with soft parameter should call validateAllItems with correct parameters")
    void validateAllItemsWithSoftShouldCallValidateAllItemsWithCorrectParameters() {
        // Given
        mockService.reset();
        List<String> selectedItems = Arrays.asList("Item1", "Item2", "Item3");
        mockService.returnSelectedList = selectedItems;
        String[] expectedValues = {"Item1", "Item2"};

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateAllItems(element, true, expectedValues);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        verify(storageUI).put(element.enumImpl(), selectedItems);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("insertion should call itemListService.insertion with correct parameters")
    void insertionShouldCallItemListServiceWithCorrectParameters() {
        // Given
        mockService.reset();
        Object[] values = {"value1", "value2"};

        // When
        sut.insertion(MockItemListComponentType.DUMMY, By.id("testList"), values);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{"value1", "value2"});
    }

    @Test
    @DisplayName("validateIsEnabled single value should call validateAreEnabled with correct parameters")
    void validateIsEnabledSingleValueShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsEnabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsEnabled single value with soft parameter should call validateAreEnabled with correct parameters")
    void validateIsEnabledSingleValueWithSoftShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsEnabled(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled single value should call validateAreEnabled with correct parameters")
    void validateIsDisabledSingleValueShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsDisabled(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsDisabled single value with soft parameter should call validateAreEnabled with correct parameters")
    void validateIsDisabledSingleValueWithSoftShouldCallValidateAreEnabledWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsDisabled(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible single value should call validateAreVisible with correct parameters")
    void validateIsVisibleSingleValueShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsVisible(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsVisible single value with soft parameter should call validateAreVisible with correct parameters")
    void validateIsVisibleSingleValueWithSoftShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = true;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsVisible(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden single value should call validateAreVisible with correct parameters")
    void validateIsHiddenSingleValueShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

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
        UIServiceFluent<?> result = sut.validateIsHidden(element, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("validateIsHidden single value with soft parameter should call validateAreVisible with correct parameters")
    void validateIsHiddenSingleValueWithSoftShouldCallValidateAreVisibleWithCorrectParameters() {
        // Given
        mockService.reset();
        mockService.returnBool = false;
        String value = "Item1";

        // Configure uiServiceFluent.validate to actually execute the lambda
        doAnswer(invocation -> {
            java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
            org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
            assertionConsumer.accept(softAssertions);
            return uiServiceFluent;
        }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

        // When
        UIServiceFluent<?> result = sut.validateIsHidden(element, true, value);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
        assertThat(mockService.lastLocators).isEqualTo(new By[]{By.id("testList")});
        assertThat(mockService.lastText).isEqualTo(new String[]{value});
        verify(storageUI).put(element.enumImpl(), false);
        assertThat(result).isSameAs(uiServiceFluent);
    }
}