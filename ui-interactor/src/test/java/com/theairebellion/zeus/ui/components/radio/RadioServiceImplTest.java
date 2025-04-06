package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("RadioServiceImpl Tests")
class RadioServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private RadioServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;
    private Radio radioMock;
    private MockRadioComponentType componentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new RadioServiceImpl(driver);
        container = MockSmartWebElement.createMock();
        locator = By.id("radio");
        strategy = Strategy.FIRST;
        radioMock = mock(Radio.class);
        componentType = MockRadioComponentType.DUMMY;

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getRadioComponent(eq(componentType), eq(driver)))
                .thenReturn(radioMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Component Creation and Caching")
    class ComponentCreationAndCaching {

        @Test
        @DisplayName("Components are cached and reused")
        void componentCaching() {
            // When
            service.select(componentType, "RadioOption1");
            service.select(componentType, "RadioOption2");

            // Then
            factoryMock.verify(() -> ComponentFactory.getRadioComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("Different component types create different instances")
        void differentComponentTypes() throws Exception {
            // Given
            RadioServiceImpl service2 = new RadioServiceImpl(driver);

            Radio component1 = mock(Radio.class);
            Radio component2 = mock(Radio.class);

            // Set up component caches manually using reflection
            Map<RadioComponentType, Radio> componentsMap1 = new HashMap<>();
            componentsMap1.put(componentType, component1);

            Map<RadioComponentType, Radio> componentsMap2 = new HashMap<>();
            componentsMap2.put(componentType, component2);

            // Access private field to set components map
            Field componentsField = AbstractComponentService.class.getDeclaredField("components");
            componentsField.setAccessible(true);
            componentsField.set(service, componentsMap1);
            componentsField.set(service2, componentsMap2);

            // When
            service.select(componentType, "test1");
            service2.select(componentType, "test2");

            // Then
            verify(component1).select("test1");
            verify(component2).select("test2");
        }

        @Test
        @DisplayName("createComponent calls ComponentFactory.getRadioComponent")
        void createComponentCallsFactory() throws Exception {
            // Given
            Method createComponentMethod = RadioServiceImpl.class.getDeclaredMethod(
                    "createComponent", RadioComponentType.class);
            createComponentMethod.setAccessible(true);

            // When
            Radio result = (Radio) createComponentMethod.invoke(service, componentType);

            // Then
            assertThat(result).isSameAs(radioMock);
            factoryMock.verify(() -> ComponentFactory.getRadioComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("radioComponent helper method returns correct component")
        void radioComponentReturnsComponent() throws Exception {
            // Given
            Method radioComponentMethod = RadioServiceImpl.class.getDeclaredMethod(
                    "radioComponent", RadioComponentType.class);
            radioComponentMethod.setAccessible(true);

            // Insert component into cache
            service.select(componentType, "test"); // This will cache the component

            // When
            Radio result = (Radio) radioComponentMethod.invoke(service, componentType);

            // Then
            assertThat(result).isSameAs(radioMock);
        }
    }

    @Nested
    @DisplayName("Select Method Tests")
    class SelectMethodTests {

        @Test
        @DisplayName("select(componentType, container, radioButtonText) delegates correctly")
        void selectContainerText() {
            // When
            service.select(componentType, container, "Option1");

            // Then
            verify(radioMock).select(container, "Option1");
        }

        @Test
        @DisplayName("select(componentType, container, strategy) delegates correctly")
        void selectContainerStrategy() {
            // Given
            when(radioMock.select(container, strategy)).thenReturn("SelectedOption");

            // When
            String result = service.select(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo("SelectedOption");
            verify(radioMock).select(container, strategy);
        }

        @Test
        @DisplayName("select(componentType, radioButtonText) delegates correctly")
        void selectText() {
            // When
            service.select(componentType, "Option1");

            // Then
            verify(radioMock).select("Option1");
        }

        @Test
        @DisplayName("select(componentType, radioButtonLocator) delegates correctly")
        void selectLocator() {
            // When
            service.select(componentType, locator);

            // Then
            verify(radioMock).select(locator);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled(componentType, container, radioButtonText) delegates correctly")
        void isEnabledContainerText() {
            // Given
            when(radioMock.isEnabled(container, "Option1")).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, container, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled(container, "Option1");
        }

        @Test
        @DisplayName("isEnabled(componentType, radioButtonText) delegates correctly")
        void isEnabledText() {
            // Given
            when(radioMock.isEnabled("Option1")).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled("Option1");
        }

        @Test
        @DisplayName("isEnabled(componentType, radioButtonLocator) delegates correctly")
        void isEnabledLocator() {
            // Given
            when(radioMock.isEnabled(locator)).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled(locator);
        }
    }

    @Nested
    @DisplayName("IsSelected Method Tests")
    class IsSelectedMethodTests {

        @Test
        @DisplayName("isSelected(componentType, container, radioButtonText) delegates correctly")
        void isSelectedContainerText() {
            // Given
            when(radioMock.isSelected(container, "Option1")).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, container, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected(container, "Option1");
        }

        @Test
        @DisplayName("isSelected(componentType, radioButtonText) delegates correctly")
        void isSelectedText() {
            // Given
            when(radioMock.isSelected("Option1")).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected("Option1");
        }

        @Test
        @DisplayName("isSelected(componentType, radioButtonLocator) delegates correctly")
        void isSelectedLocator() {
            // Given
            when(radioMock.isSelected(locator)).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected(locator);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible(componentType, container, radioButtonText) delegates correctly")
        void isVisibleContainerText() {
            // Given
            when(radioMock.isVisible(container, "Option1")).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, container, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible(container, "Option1");
        }

        @Test
        @DisplayName("isVisible(componentType, radioButtonText) delegates correctly")
        void isVisibleText() {
            // Given
            when(radioMock.isVisible("Option1")).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, "Option1");

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible("Option1");
        }

        @Test
        @DisplayName("isVisible(componentType, radioButtonLocator) delegates correctly")
        void isVisibleLocator() {
            // Given
            when(radioMock.isVisible(locator)).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible(locator);
        }
    }

    @Nested
    @DisplayName("Get Methods Tests")
    class GetMethodsTests {

        @Test
        @DisplayName("getSelected(componentType, container) delegates correctly")
        void getSelectedContainer() {
            // Given
            when(radioMock.getSelected(container)).thenReturn("SelectedOption");

            // When
            String result = service.getSelected(componentType, container);

            // Then
            assertThat(result).isEqualTo("SelectedOption");
            verify(radioMock).getSelected(container);
        }

        @Test
        @DisplayName("getSelected(componentType, containerLocator) delegates correctly")
        void getSelectedLocator() {
            // Given
            when(radioMock.getSelected(locator)).thenReturn("SelectedOption");

            // When
            String result = service.getSelected(componentType, locator);

            // Then
            assertThat(result).isEqualTo("SelectedOption");
            verify(radioMock).getSelected(locator);
        }

        @Test
        @DisplayName("getAll(componentType, container) delegates correctly")
        void getAllContainer() {
            // Given
            List<String> options = Arrays.asList("Option1", "Option2", "Option3");
            when(radioMock.getAll(container)).thenReturn(options);

            // When
            List<String> result = service.getAll(componentType, container);

            // Then
            assertThat(result).isEqualTo(options);
            verify(radioMock).getAll(container);
        }

        @Test
        @DisplayName("getAll(componentType, containerLocator) delegates correctly")
        void getAllLocator() {
            // Given
            List<String> options = Arrays.asList("Option1", "Option2", "Option3");
            when(radioMock.getAll(locator)).thenReturn(options);

            // When
            List<String> result = service.getAll(componentType, locator);

            // Then
            assertThat(result).isEqualTo(options);
            verify(radioMock).getAll(locator);
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("insertion method delegates to select with radioButtonText")
        void insertionDelegatesToSelect() {
            // When
            service.insertion(componentType, locator, "RadioOption");

            // Then
            verify(radioMock).select("RadioOption");
        }
    }
}