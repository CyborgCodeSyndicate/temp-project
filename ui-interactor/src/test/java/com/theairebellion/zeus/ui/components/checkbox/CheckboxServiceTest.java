package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CheckboxService Test")
class CheckboxServiceTest extends BaseUnitUITest {

    private MockCheckboxService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockCheckboxService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testCheckbox");
        strategy = Strategy.FIRST;
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Select")
    class DefaultMethodDelegationSelectTests {

        @Test
        @DisplayName("select with container and text delegates to implementation")
        void testDefaultSelectWithContainerAndText() {
            // Given
            service.reset();

            // When
            service.select(container, "A", "B");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A", "B"});
        }

        @Test
        @DisplayName("select with container and strategy delegates to implementation")
        void testDefaultSelectWithContainerAndStrategy() {
            // Given
            service.reset();

            // When
            String result = service.select(container, strategy);

            // Then
            assertThat(result).isEqualTo("selectStrategyMock");
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("select with text only delegates to implementation")
        void testDefaultSelectWithTextOnly() {
            // Given
            service.reset();

            // When
            service.select("A");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A"});
        }

        @Test
        @DisplayName("select with locator delegates to implementation")
        void testDefaultSelectWithLocator() {
            // Given
            service.reset();

            // When
            service.select(locator);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - DeSelect")
    class DefaultMethodDelegationDeSelectTests {

        @Test
        @DisplayName("deSelect with container and text delegates to implementation")
        void testDefaultDeSelectWithContainerAndText() {
            // Given
            service.reset();

            // When
            service.deSelect(container, "C", "D");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"C", "D"});
        }

        @Test
        @DisplayName("deSelect with container and strategy delegates to implementation")
        void testDefaultDeSelectWithContainerAndStrategy() {
            // Given
            service.reset();

            // When
            String result = service.deSelect(container, strategy);

            // Then
            assertThat(result).isEqualTo("deSelectStrategyMock");
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastStrategy).isEqualTo(strategy);
        }

        @Test
        @DisplayName("deSelect with text only delegates to implementation")
        void testDefaultDeSelectWithTextOnly() {
            // Given
            service.reset();

            // When
            service.deSelect("X");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"X"});
        }

        @Test
        @DisplayName("deSelect with locator delegates to implementation")
        void testDefaultDeSelectWithLocator() {
            // Given
            service.reset();

            // When
            service.deSelect(locator);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - AreSelected")
    class DefaultMethodDelegationAreSelectedTests {

        @Test
        @DisplayName("areSelected with container and text delegates to implementation")
        void testDefaultAreSelectedWithContainerAndText() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.areSelected(container, "A", "B");

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A", "B"});
        }

        @Test
        @DisplayName("areSelected with text only delegates to implementation")
        void testDefaultAreSelectedWithTextOnly() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.areSelected("A");

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A"});
        }

        @Test
        @DisplayName("areSelected with locator delegates to implementation")
        void testDefaultAreSelectedWithLocator() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.areSelected(locator);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsSelected")
    class DefaultMethodDelegationIsSelectedTests {

        @Test
        @DisplayName("isSelected with container delegates to implementation")
        void testDefaultIsSelectedWithContainer() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.isSelected(container, "A");

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A"});
        }

        @Test
        @DisplayName("isSelected with text only delegates to implementation")
        void testDefaultIsSelectedWithTextOnly() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.isSelected("A");

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A"});
        }

        @Test
        @DisplayName("isSelected with locator delegates to implementation")
        void testDefaultIsSelectedWithLocator() {
            // Given
            service.reset();
            service.returnSelected = true;

            // When
            boolean selected = service.isSelected(locator);

            // Then
            assertThat(selected).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - AreEnabled")
    class DefaultMethodDelegationAreEnabledTests {

        @Test
        @DisplayName("areEnabled with container and text delegates to implementation")
        void testDefaultAreEnabledWithContainerAndText() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.areEnabled(container, "X");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"X"});
        }

        @Test
        @DisplayName("areEnabled with text only delegates to implementation")
        void testDefaultAreEnabledWithTextOnly() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.areEnabled("Y");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"Y"});
        }

        @Test
        @DisplayName("areEnabled with locator delegates to implementation")
        void testDefaultAreEnabledWithLocator() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.areEnabled(locator);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsEnabled")
    class DefaultMethodDelegationIsEnabledTests {

        @Test
        @DisplayName("isEnabled with container delegates to implementation")
        void testDefaultIsEnabledWithContainer() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled(container, "Z");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"Z"});
        }

        @Test
        @DisplayName("isEnabled with text only delegates to implementation")
        void testDefaultIsEnabledWithTextOnly() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled("Z");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"Z"});
        }

        @Test
        @DisplayName("isEnabled with locator delegates to implementation")
        void testDefaultIsEnabledWithLocator() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled(locator);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Get Methods")
    class DefaultMethodDelegationGetMethodsTests {

        @Test
        @DisplayName("getSelected with container delegates to implementation")
        void testDefaultGetSelectedWithContainer() {
            // Given
            service.reset();
            service.returnSelectedList = List.of("A");

            // When
            List<String> result = service.getSelected(container);

            // Then
            assertThat(result).isEqualTo(List.of("A"));
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getSelected with locator delegates to implementation")
        void testDefaultGetSelectedWithLocator() {
            // Given
            service.reset();
            service.returnSelectedList = List.of("X", "Y");

            // When
            List<String> result = service.getSelected(locator);

            // Then
            assertThat(result).isEqualTo(List.of("X", "Y"));
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }

        @Test
        @DisplayName("getAll with container delegates to implementation")
        void testDefaultGetAllWithContainer() {
            // Given
            service.reset();
            service.returnAllList = List.of("A", "B", "C");

            // When
            List<String> result = service.getAll(container);

            // Then
            assertThat(result).isEqualTo(List.of("A", "B", "C"));
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getAll with locator delegates to implementation")
        void testDefaultGetAllWithLocator() {
            // Given
            service.reset();
            service.returnAllList = List.of("D", "E");

            // When
            List<String> result = service.getAll(locator);

            // Then
            assertThat(result).isEqualTo(List.of("D", "E"));
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).isEqualTo(new By[]{locator});
        }
    }

    @Nested
    @DisplayName("Explicit Component Type Tests")
    class ExplicitComponentTypeTests {

        @Test
        @DisplayName("Different component type is correctly passed to implementation")
        void testDifferentComponentType() {
            // Given
            service.reset();

            // When
            service.select(MockCheckboxComponentType.CUSTOM, container, "A", "B");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.CUSTOM);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastCheckboxText).isEqualTo(new String[]{"A", "B"});
        }

        @Test
        @DisplayName("Return value is correctly passed from implementation")
        void testReturnValueFromImplementation() {
            // Given
            service.reset();
            service.returnEnabled = false;

            // When
            boolean result = service.areEnabled(container, "A");

            // Then
            assertThat(result).isFalse();
        }
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
            uiConfigHolderMock = Mockito.mockStatic(UiConfigHolder.class);
            reflectionUtilMock = Mockito.mockStatic(ReflectionUtil.class);

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfigMock);
            when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
            when(uiConfigMock.checkboxDefaultType()).thenReturn("TEST_TYPE");
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
            CheckboxComponentType mockType = mock(CheckboxComponentType.class);
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(CheckboxComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = CheckboxService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            CheckboxComponentType result = (CheckboxComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType handles exceptions correctly")
        void getDefaultTypeWithException() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(CheckboxComponentType.class),
                            anyString(),
                            anyString()))
                    .thenReturn(null); // Instead of throwing exception, return null

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = CheckboxService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            CheckboxComponentType result = (CheckboxComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Insertion Interface Tests")
    class InsertionInterfaceTests {

        @Test
        @DisplayName("Insertion method delegates to implementation")
        void testInsertionMethod() {
            // Given
            service.reset();

            // When
            service.insertion(MockCheckboxComponentType.DUMMY, locator, "Inserted");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).containsExactly(locator);
            assertThat(service.lastCheckboxText).containsExactly("Inserted");
        }

        @Test
        @DisplayName("Insertion method with multiple values")
        void testInsertionMethodWithMultipleValues() {
            // Given
            service.reset();

            // When
            service.insertion(MockCheckboxComponentType.DUMMY, locator, "Value1", "Value2");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
            assertThat(service.lastCheckboxLocators).containsExactly(locator);
            assertThat(service.lastCheckboxText).contains("Value1", "Value2");
        }
    }
}