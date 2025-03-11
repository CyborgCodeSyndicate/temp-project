package com.theairebellion.zeus.ui.components.button;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@DisplayName("ButtonServiceImpl Test")
class ButtonServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private ButtonServiceImpl service;
    private SmartWebElement container;
    private Button buttonMock;
    private MockButtonComponentType mockButtonComponentType;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ButtonServiceImpl(driver);
        container = mock(SmartWebElement.class);
        buttonMock = mock(Button.class);
        mockButtonComponentType = MockButtonComponentType.DUMMY;
        locator = By.id("button");
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)))
                .thenReturn(buttonMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Click Method Tests")
    class ClickMethodTests {

        @Test
        @DisplayName("click with container and text delegates to component correctly")
        void testClickContainerAndText() {
            // When
            service.click(mockButtonComponentType, container, "ClickMe");

            // Then
            verify(buttonMock).click(container, "ClickMe");
        }

        @Test
        @DisplayName("click with container delegates to component correctly")
        void testClickContainer() {
            // When
            service.click(mockButtonComponentType, container);

            // Then
            verify(buttonMock).click(container);
        }

        @Test
        @DisplayName("click with text delegates to component correctly")
        void testClickString() {
            // When
            service.click(mockButtonComponentType, "ClickMe");

            // Then
            verify(buttonMock).click("ClickMe");
        }

        @Test
        @DisplayName("click with locator delegates to component correctly")
        void testClickLocator() {
            // When
            service.click(mockButtonComponentType, locator);

            // Then
            verify(buttonMock).click(locator);
        }

        @Test
        @DisplayName("default click with container and text delegates correctly")
        void testClickDefaultOverloadContainerAndText() {
            // When
            service.click(container, "DefaultClick");

            // Then
            verify(buttonMock).click(container, "DefaultClick");
        }

        @Test
        @DisplayName("default click with container delegates correctly")
        void testClickDefaultOverloadContainer() {
            // When
            service.click(container);

            // Then
            verify(buttonMock).click(container);
        }

        @Test
        @DisplayName("default click with text delegates correctly")
        void testClickDefaultOverloadString() {
            // When
            service.click("DefaultClick");

            // Then
            verify(buttonMock).click("DefaultClick");
        }

        @Test
        @DisplayName("default click with locator delegates correctly")
        void testClickDefaultOverloadLocator() {
            // When
            service.click(locator);

            // Then
            verify(buttonMock).click(locator);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container and text delegates to component correctly")
        void testIsEnabledContainerAndText() {
            // Given
            when(buttonMock.isEnabled(container, "EnableMe")).thenReturn(true);

            // When
            boolean result = service.isEnabled(mockButtonComponentType, container, "EnableMe");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container, "EnableMe");
        }

        @Test
        @DisplayName("isEnabled with container delegates to component correctly")
        void testIsEnabledContainer() {
            // Given
            when(buttonMock.isEnabled(container)).thenReturn(true);

            // When
            boolean result = service.isEnabled(mockButtonComponentType, container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container);
        }

        @Test
        @DisplayName("isEnabled with text delegates to component correctly")
        void testIsEnabledString() {
            // Given
            when(buttonMock.isEnabled("EnableMe")).thenReturn(true);

            // When
            boolean result = service.isEnabled(mockButtonComponentType, "EnableMe");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled("EnableMe");
        }

        @Test
        @DisplayName("isEnabled with locator delegates to component correctly")
        void testIsEnabledLocator() {
            // Given
            when(buttonMock.isEnabled(locator)).thenReturn(true);

            // When
            boolean result = service.isEnabled(mockButtonComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(locator);
        }

        @Test
        @DisplayName("default isEnabled with container and text delegates correctly")
        void testIsEnabledDefaultOverloadContainerAndText() {
            // Given
            when(buttonMock.isEnabled(container, "DefaultEnable")).thenReturn(true);

            // When
            boolean result = service.isEnabled(container, "DefaultEnable");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container, "DefaultEnable");
        }

        @Test
        @DisplayName("default isEnabled with container delegates correctly")
        void testIsEnabledDefaultOverloadContainer() {
            // Given
            when(buttonMock.isEnabled(container)).thenReturn(true);

            // When
            boolean result = service.isEnabled(container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container);
        }

        @Test
        @DisplayName("default isEnabled with text delegates correctly")
        void testIsEnabledDefaultOverloadString() {
            // Given
            when(buttonMock.isEnabled("DefaultEnable")).thenReturn(true);

            // When
            boolean result = service.isEnabled("DefaultEnable");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled("DefaultEnable");
        }

        @Test
        @DisplayName("default isEnabled with locator delegates correctly")
        void testIsEnabledDefaultOverloadLocator() {
            // Given
            when(buttonMock.isEnabled(locator)).thenReturn(true);

            // When
            boolean result = service.isEnabled(locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(locator);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible with container and text delegates to component correctly")
        void testIsVisibleContainerAndText() {
            // Given
            when(buttonMock.isVisible(container, "VisibleMe")).thenReturn(true);

            // When
            boolean result = service.isVisible(mockButtonComponentType, container, "VisibleMe");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container, "VisibleMe");
        }

        @Test
        @DisplayName("isVisible with container delegates to component correctly")
        void testIsVisibleContainer() {
            // Given
            when(buttonMock.isVisible(container)).thenReturn(true);

            // When
            boolean result = service.isVisible(mockButtonComponentType, container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container);
        }

        @Test
        @DisplayName("isVisible with text delegates to component correctly")
        void testIsVisibleString() {
            // Given
            when(buttonMock.isVisible("VisibleMe")).thenReturn(true);

            // When
            boolean result = service.isVisible(mockButtonComponentType, "VisibleMe");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible("VisibleMe");
        }

        @Test
        @DisplayName("isVisible with locator delegates to component correctly")
        void testIsVisibleLocator() {
            // Given
            when(buttonMock.isVisible(locator)).thenReturn(true);

            // When
            boolean result = service.isVisible(mockButtonComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(locator);
        }

        @Test
        @DisplayName("default isVisible with container and text delegates correctly")
        void testIsVisibleDefaultOverloadContainerAndText() {
            // Given
            when(buttonMock.isVisible(container, "DefaultVisible")).thenReturn(true);

            // When
            boolean result = service.isVisible(container, "DefaultVisible");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container, "DefaultVisible");
        }

        @Test
        @DisplayName("default isVisible with container delegates correctly")
        void testIsVisibleDefaultOverloadContainer() {
            // Given
            when(buttonMock.isVisible(container)).thenReturn(true);

            // When
            boolean result = service.isVisible(container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container);
        }

        @Test
        @DisplayName("default isVisible with text delegates correctly")
        void testIsVisibleDefaultOverloadString() {
            // Given
            when(buttonMock.isVisible("DefaultVisible")).thenReturn(true);

            // When
            boolean result = service.isVisible("DefaultVisible");

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible("DefaultVisible");
        }

        @Test
        @DisplayName("default isVisible with locator delegates correctly")
        void testIsVisibleDefaultOverloadLocator() {
            // Given
            when(buttonMock.isVisible(locator)).thenReturn(true);

            // When
            boolean result = service.isVisible(locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(locator);
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused between method calls")
        void testComponentCaching() {
            // When
            service.click(mockButtonComponentType, container, "ClickMe");
            service.isEnabled(mockButtonComponentType, container, "EnableMe");
            service.isVisible(mockButtonComponentType, container, "VisibleMe");

            // Then
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("Multiple service instances don't share component cache")
        void testMultipleServiceInstances() throws Exception {
            // Given - Create two service instances
            ButtonServiceImpl service1 = new ButtonServiceImpl(driver);
            ButtonServiceImpl service2 = new ButtonServiceImpl(driver);

            // Reset factory mock behavior
            factoryMock.close();
            factoryMock = Mockito.mockStatic(ComponentFactory.class);

            // We need to ensure different component instances are returned for each service
            factoryMock.when(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)))
                    .thenAnswer(invocation -> mock(Button.class));

            // When
            service1.click(mockButtonComponentType, container, "ClickMe");
            service2.click(mockButtonComponentType, container, "ClickMe");

            // Then - We need to check internal state via reflection
            Field componentsField1 = AbstractComponentService.class.getDeclaredField("components");
            componentsField1.setAccessible(true);
            Map<?, ?> componentsMap1 = (Map<?, ?>) componentsField1.get(service1);

            Field componentsField2 = AbstractComponentService.class.getDeclaredField("components");
            componentsField2.setAccessible(true);
            Map<?, ?> componentsMap2 = (Map<?, ?>) componentsField2.get(service2);

            // Assert the maps are different objects
            assertThat(componentsMap1).isNotSameAs(componentsMap2);

            // The component map in each service should have keys (after click was called)
            assertThat(componentsMap1).isNotEmpty();
            assertThat(componentsMap2).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("TableInsertion Method Tests")
    class TableInsertionMethodTests {

        @Test
        @DisplayName("tableInsertion delegates to button component's clickElementInCell")
        void testTableInsertion() {
            // Given
            SmartWebElement cellElement = mock(SmartWebElement.class);

            // When
            service.tableInsertion(cellElement, mockButtonComponentType, "value1", "value2");

            // Then
            verify(buttonMock).clickElementInCell(cellElement);
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableInsertion with non-ButtonComponentType throws ClassCastException")
        void testTableInsertionWithNonButtonComponentType() {
            // Given
            SmartWebElement cellElement = mock(SmartWebElement.class);
            ComponentType nonButtonType = mock(ComponentType.class);

            try {
                // When
                service.tableInsertion(cellElement, nonButtonType, "value1");
                throw new AssertionError("Expected ClassCastException was not thrown");
            } catch (ClassCastException e) {
                // Then - Expected exception
                assertThat(e).isInstanceOf(ClassCastException.class);
            }
        }
    }

    @Nested
    @DisplayName("Protected Method Tests")
    class ProtectedMethodTests {

        @Test
        @DisplayName("createComponent delegates to ComponentFactory")
        void testCreateComponent() throws Exception {
            // Given
            Method createComponentMethod = ButtonServiceImpl.class.getDeclaredMethod("createComponent", ButtonComponentType.class);
            createComponentMethod.setAccessible(true);

            // When
            createComponentMethod.invoke(service, mockButtonComponentType);

            // Then
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("buttonComponent delegates to getOrCreateComponent")
        void testButtonComponent() throws Exception {
            // Given
            Method buttonComponentMethod = ButtonServiceImpl.class.getDeclaredMethod("buttonComponent", ButtonComponentType.class);
            buttonComponentMethod.setAccessible(true);

            // Clear components map to ensure component creation
            Field componentsField = AbstractComponentService.class.getDeclaredField("components");
            componentsField.setAccessible(true);
            Map<ButtonComponentType, Button> componentsMap = new HashMap<>();
            componentsField.set(service, componentsMap);

            // When
            buttonComponentMethod.invoke(service, mockButtonComponentType);

            // Then
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("Method with null component type throws exception")
        void testMethodWithNullComponentType() {
            // When/Then - should throw NullPointerException
            try {
                service.click(null, container, "ClickMe");
                throw new AssertionError("Expected NullPointerException was not thrown");
            } catch (NullPointerException e) {
                // Expected exception
                assertThat(e).isInstanceOf(NullPointerException.class);
            }
        }

        @Test
        @DisplayName("Method with null container delegates to component")
        void testMethodWithNullContainer() {
            // When
            service.click(mockButtonComponentType, null, "ClickMe");

            // Then
            verify(buttonMock).click(null, "ClickMe");
        }

        @Test
        @DisplayName("Method with null text delegates to component")
        void testMethodWithNullText() {
            // When
            service.click(mockButtonComponentType, container, null);

            // Then
            verify(buttonMock).click(container, null);
        }
    }
}