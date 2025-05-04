package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertService;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AlertService Test")
class AlertServiceTest extends BaseUnitUITest {

    private MockAlertService service;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        // Given
        service = new MockAlertService();
        container = MockSmartWebElement.createMock();
        locator = By.id("alertLocator");
        service.reset();
    }

    @Nested
    @DisplayName("Default Method Delegation Tests")
    class DefaultMethodDelegationTests {

        @Test
        @DisplayName("getValue with container delegates to implementation")
        void testGetValueWithContainerDefault() {
            // Given - setup in @BeforeEach

            // When
            var result = service.getValue(container);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_CONTAINER);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getValue with locator delegates to implementation")
        void testGetValueWithLocatorDefault() {
            // Given - setup in @BeforeEach

            // When
            var result = service.getValue(locator);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_LOCATOR);
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("isVisible with container delegates to implementation")
        void testIsVisibleWithContainerDefault() {
            // Given - setup in @BeforeEach

            // When
            var visible = service.isVisible(container);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("isVisible with locator delegates to implementation")
        void testIsVisibleWithLocatorDefault() {
            // Given - setup in @BeforeEach

            // When
            var visible = service.isVisible(locator);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }
}