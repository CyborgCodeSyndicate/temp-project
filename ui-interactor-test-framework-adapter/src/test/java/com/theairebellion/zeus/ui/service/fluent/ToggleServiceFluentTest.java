package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.ui.selenium.ToggleUIElement;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleUIElement;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("ToggleServiceFluent Tests")
class ToggleServiceFluentTest extends BaseUnitUITest {

    private MockToggleService mockService;
    private ToggleServiceFluent sut;
    private Storage storage;
    private Storage storageUI;
    private UIServiceFluent uiServiceFluent;
    private ToggleUIElement element;
    private MockedStatic<Allure> allureMock;

    @BeforeEach
    void setUp() {
        // Setup mocks
        mockService = new MockToggleService();
        storage = mock(Storage.class);
        storageUI = mock(Storage.class);
        uiServiceFluent = mock(UIServiceFluent.class);
        element = new MockToggleUIElement(By.id("testToggle"), MockToggleComponentType.DUMMY);

        when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

        // Mock Allure steps
        allureMock = mockStatic(Allure.class);

        // Create the SUT
        sut = new ToggleServiceFluent(uiServiceFluent, storage, mockService);
    }

    @AfterEach
    void tearDown() {
        allureMock.close();
    }

    @Test
    @DisplayName("activate should call toggleService.activate and return uiServiceFluent")
    void activateShouldCallToggleServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent result = sut.activate(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testToggle"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("deactivate should call toggleService.deactivate and return uiServiceFluent")
    void deactivateShouldCallToggleServiceAndReturnUiServiceFluent() {
        // When
        UIServiceFluent result = sut.deactivate(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testToggle"));
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isActivated should call toggleService.isActivated, store result and return uiServiceFluent")
    void isActivatedShouldCallToggleServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // When
        UIServiceFluent result = sut.isActivated(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testToggle"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }

    @Test
    @DisplayName("isEnabled should call toggleService.isEnabled, store result and return uiServiceFluent")
    void isEnabledShouldCallToggleServiceStoreResultAndReturnUiServiceFluent() {
        // Given
        mockService.reset();
        mockService.returnBool = true;

        // When
        UIServiceFluent result = sut.isEnabled(element);

        // Then
        assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
        assertThat(mockService.lastLocator).isEqualTo(By.id("testToggle"));
        verify(storageUI).put(element.enumImpl(), true);
        assertThat(result).isSameAs(uiServiceFluent);
    }
}