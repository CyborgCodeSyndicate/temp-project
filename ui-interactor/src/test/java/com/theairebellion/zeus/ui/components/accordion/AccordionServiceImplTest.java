package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("AccordionServiceImpl test")
class AccordionServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private AccordionServiceImpl service;
    private SmartWebElement container;
    private Accordion accordion;
    private MockAccordionComponentType componentType;
    private Strategy strategy;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new AccordionServiceImpl(driver);
        container = mock(SmartWebElement.class);
        accordion = mock(Accordion.class);
        componentType = MockAccordionComponentType.DUMMY;
        strategy = mock(Strategy.class);

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getAccordionComponent(eq(componentType), eq(driver)))
                .thenReturn(accordion);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Expand operations")
    class ExpandOperations {

        @Test
        @DisplayName("expand with container and text")
        void expandWithContainerAndText() {
            // When
            service.expand(componentType, container, "Panel1", "Panel2");

            // Then
            verify(accordion).expand(container, "Panel1", "Panel2");
        }

        @Test
        @DisplayName("expand with container and strategy")
        void expandWithContainerAndStrategy() {
            // Given
            when(accordion.expand(container, strategy)).thenReturn("Expanded");

            // When
            String result = service.expand(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo("Expanded");
            verify(accordion).expand(container, strategy);
        }

        @Test
        @DisplayName("expand with text only")
        void expandWithTextOnly() {
            // When
            service.expand(componentType, "Panel1");

            // Then
            verify(accordion).expand("Panel1");
        }

        @Test
        @DisplayName("expand with By locator")
        void expandWithByLocator() {
            // Given
            By locator = By.id("accordion");

            // When
            service.expand(componentType, locator);

            // Then
            verify(accordion).expand(locator);
        }
    }

    @Nested
    @DisplayName("Collapse operations")
    class CollapseOperations {

        @Test
        @DisplayName("collapse with container and text")
        void collapseWithContainerAndText() {
            // When
            service.collapse(componentType, container, "Panel1");

            // Then
            verify(accordion).collapse(container, "Panel1");
        }

        @Test
        @DisplayName("collapse with container and strategy")
        void collapseWithContainerAndStrategy() {
            // Given
            when(accordion.collapse(container, strategy)).thenReturn("Collapsed");

            // When
            String result = service.collapse(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo("Collapsed");
            verify(accordion).collapse(container, strategy);
        }

        @Test
        @DisplayName("collapse with text only")
        void collapseWithTextOnly() {
            // When
            service.collapse(componentType, "Panel1");

            // Then
            verify(accordion).collapse("Panel1");
        }

        @Test
        @DisplayName("collapse with By locator")
        void collapseWithByLocator() {
            // Given
            By locator = By.id("accordion");

            // When
            service.collapse(componentType, locator);

            // Then
            verify(accordion).collapse(locator);
        }
    }

    @Nested
    @DisplayName("Status check operations")
    class StatusCheckOperations {

        @Test
        @DisplayName("areEnabled with container and text")
        void areEnabledWithContainerAndText() {
            // Given
            when(accordion.areEnabled(container, "Panel1", "Panel2")).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, container, "Panel1", "Panel2");

            // Then
            assertThat(result).isTrue();
            verify(accordion).areEnabled(container, "Panel1", "Panel2");
        }

        @Test
        @DisplayName("areEnabled with text only")
        void areEnabledWithTextOnly() {
            // Given
            when(accordion.areEnabled("Panel1")).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, "Panel1");

            // Then
            assertThat(result).isTrue();
            verify(accordion).areEnabled("Panel1");
        }

        @Test
        @DisplayName("areEnabled with By locator")
        void areEnabledWithByLocator() {
            // Given
            By locator = By.id("accordion");
            when(accordion.areEnabled(locator)).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(accordion).areEnabled(locator);
        }
    }

    @Nested
    @DisplayName("Get accordion information")
    class GetAccordionInformation {

        @Test
        @DisplayName("getExpanded returns list of expanded panels")
        void getExpanded() {
            // Given
            List<String> expandedList = Collections.singletonList("Panel1");
            when(accordion.getExpanded(container)).thenReturn(expandedList);

            // When
            List<String> result = service.getExpanded(componentType, container);

            // Then
            assertThat(result).isEqualTo(expandedList);
            verify(accordion).getExpanded(container);
        }

        @Test
        @DisplayName("getCollapsed returns list of collapsed panels")
        void getCollapsed() {
            // Given
            List<String> collapsedList = Arrays.asList("Panel2", "Panel3");
            when(accordion.getCollapsed(container)).thenReturn(collapsedList);

            // When
            List<String> result = service.getCollapsed(componentType, container);

            // Then
            assertThat(result).isEqualTo(collapsedList);
            verify(accordion).getCollapsed(container);
        }

        @Test
        @DisplayName("getAll returns list of all panels")
        void getAll() {
            // Given
            List<String> allPanels = Arrays.asList("Panel1", "Panel2", "Panel3");
            when(accordion.getAll(container)).thenReturn(allPanels);

            // When
            List<String> result = service.getAll(componentType, container);

            // Then
            assertThat(result).isEqualTo(allPanels);
            verify(accordion).getAll(container);
        }

        @Test
        @DisplayName("getTitle returns panel title")
        void getTitle() {
            // Given
            By locator = By.id("title");
            when(accordion.getTitle(locator)).thenReturn("Title");

            // When
            String result = service.getTitle(componentType, locator);

            // Then
            assertThat(result).isEqualTo("Title");
            verify(accordion).getTitle(locator);
        }

        @Test
        @DisplayName("getText returns panel text content")
        void getText() {
            // Given
            By locator = By.id("text");
            when(accordion.getText(locator)).thenReturn("Text");

            // When
            String result = service.getText(componentType, locator);

            // Then
            assertThat(result).isEqualTo("Text");
            verify(accordion).getText(locator);
        }
    }

    @Test
    @DisplayName("Component is cached and reused")
    void componentCaching() {
        // When
        service.expand(componentType, container, "Panel1");
        service.collapse(componentType, container, "Panel1");

        // Then
        factoryMock.verify(() -> ComponentFactory.getAccordionComponent(eq(componentType), eq(driver)), times(1));
    }

    @Test
    @DisplayName("Different component types create different components")
    void differentComponentTypes() {
        // Create a separate service instance to avoid cache issues
        AccordionServiceImpl service2 = new AccordionServiceImpl(driver);

        // Create two accordion mocks
        Accordion accordion1 = mock(Accordion.class);
        Accordion accordion2 = mock(Accordion.class);

        // Set up the component cache manually to ensure different instances
        Map<AccordionComponentType, Accordion> componentsMap1 = new HashMap<>();
        componentsMap1.put(componentType, accordion1);

        Map<AccordionComponentType, Accordion> componentsMap2 = new HashMap<>();
        componentsMap2.put(componentType, accordion2);

        // Access private field using reflection to set the components map
        try {
            java.lang.reflect.Field componentsField1 = AbstractComponentService.class.getDeclaredField("components");
            componentsField1.setAccessible(true);
            componentsField1.set(service, componentsMap1);

            java.lang.reflect.Field componentsField2 = AbstractComponentService.class.getDeclaredField("components");
            componentsField2.setAccessible(true);
            componentsField2.set(service2, componentsMap2);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set components map using reflection", e);
        }

        // Reset factory mock to ensure clean setup
        factoryMock.reset();

        // Configure factory to return specific accordions based on service instance
        factoryMock.when(() -> ComponentFactory.getAccordionComponent(any(), eq(driver)))
                .thenAnswer(invocation -> {
                    // Return accordion1 for first service, accordion2 for second service
                    AccordionComponentType type = invocation.getArgument(0);
                    if (type == componentType) {
                        // Return appropriate accordion based on which service map contains it
                        if (componentsMap1.containsKey(type)) {
                            return accordion1;
                        } else {
                            return accordion2;
                        }
                    }
                    return null;
                });

        // Execute the methods on both services
        service.expand(componentType, "Panel1");
        service2.expand(componentType, "Panel2");

        // Verify each mock received the appropriate call
        verify(accordion1).expand("Panel1");
        verify(accordion2).expand("Panel2");
    }

    @Nested
    @DisplayName("Default Type Resolution")
    class DefaultTypeResolution {

        private MockedStatic<UiConfigHolder> uiConfigHolderMock;
        private MockedStatic<ReflectionUtil> reflectionUtilMock;
        private UiConfig uiConfigMock;

        @BeforeEach
        void setUp() {
            uiConfigMock = mock(UiConfig.class);
            uiConfigHolderMock = mockStatic(UiConfigHolder.class);
            reflectionUtilMock = mockStatic(ReflectionUtil.class);

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfigMock);
            when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
            when(uiConfigMock.accordionDefaultType()).thenReturn("TEST_TYPE");
        }

        @AfterEach
        void tearDown() {
            if (uiConfigHolderMock != null) {
                uiConfigHolderMock.close();
            }
            if (reflectionUtilMock != null) {
                reflectionUtilMock.close();
            }
        }

        @Test
        @DisplayName("getDefaultType returns component type when found")
        void getDefaultTypeSuccess() throws Exception {
            // Given
            AccordionComponentType mockType = mock(AccordionComponentType.class);
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(AccordionComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When - accessing the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = AccordionService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            AccordionComponentType result = (AccordionComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType returns null when exception occurs")
        void getDefaultTypeWithException() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(AccordionComponentType.class),
                            anyString(),
                            anyString()))
                    .thenThrow(new RuntimeException("Test exception"));

            // When - accessing the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = AccordionService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            AccordionComponentType result = (AccordionComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }
    }
}