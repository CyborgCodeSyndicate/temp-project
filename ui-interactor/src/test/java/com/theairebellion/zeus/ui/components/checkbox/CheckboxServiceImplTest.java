package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CheckboxServiceImpl Test")
class CheckboxServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private CheckboxServiceImpl service;
   private SmartWebElement container;
   private Checkbox checkboxMock;
   private MockCheckboxComponentType mockCheckboxComponentType;
   private By locator;
   private Strategy strategy;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      driver = mock(SmartWebDriver.class);
      service = new CheckboxServiceImpl(driver);
      container = mock(SmartWebElement.class);
      checkboxMock = mock(Checkbox.class);
      mockCheckboxComponentType = MockCheckboxComponentType.DUMMY;
      locator = By.id("checkbox");
      strategy = Strategy.FIRST;
      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)))
            .thenReturn(checkboxMock);
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   @Nested
   @DisplayName("Select Method Tests")
   class SelectMethodTests {

      @Test
      @DisplayName("select with container and text delegates to component correctly")
      void testSelectWithContainerAndText() {
         // When
         service.select(mockCheckboxComponentType, container, "A", "B");

         // Then
         verify(checkboxMock).select(container, "A", "B");
      }

      @Test
      @DisplayName("select with container and strategy delegates to component correctly")
      void testSelectWithContainerAndStrategy() {
         // Given
         when(checkboxMock.select(container, strategy)).thenReturn("StrategySelected");

         // When
         String result = service.select(mockCheckboxComponentType, container, strategy);

         // Then
         assertThat(result).isEqualTo("StrategySelected");
         verify(checkboxMock).select(container, strategy);
      }

      @Test
      @DisplayName("select with text only delegates to component correctly")
      void testSelectWithTextOnly() {
         // When
         service.select(mockCheckboxComponentType, "A");

         // Then
         verify(checkboxMock).select("A");
      }

      @Test
      @DisplayName("select with locator delegates to component correctly")
      void testSelectWithLocator() {
         // When
         service.select(mockCheckboxComponentType, locator);

         // Then
         verify(checkboxMock).select(locator);
      }

      @Test
      @DisplayName("default select with container and text delegates correctly")
      void testDefaultSelectWithContainerAndText() {
         // When
         service.select(container, "A", "B");

         // Then
         verify(checkboxMock).select(container, "A", "B");
      }

      @Test
      @DisplayName("default select with container and strategy delegates correctly")
      void testDefaultSelectWithContainerAndStrategy() {
         // Given
         when(checkboxMock.select(container, strategy)).thenReturn("DefaultStrategySelected");

         // When
         String result = service.select(container, strategy);

         // Then
         assertThat(result).isEqualTo("DefaultStrategySelected");
         verify(checkboxMock).select(container, strategy);
      }

      @Test
      @DisplayName("default select with text only delegates correctly")
      void testDefaultSelectWithTextOnly() {
         // When
         service.select("DefaultA");

         // Then
         verify(checkboxMock).select("DefaultA");
      }

      @Test
      @DisplayName("default select with locator delegates correctly")
      void testDefaultSelectWithLocator() {
         // When
         service.select(locator);

         // Then
         verify(checkboxMock).select(locator);
      }
   }

   @Nested
   @DisplayName("DeSelect Method Tests")
   class DeSelectMethodTests {

      @Test
      @DisplayName("deSelect with container and text delegates to component correctly")
      void testDeSelectWithContainerAndText() {
         // When
         service.deSelect(mockCheckboxComponentType, container, "A", "B");

         // Then
         verify(checkboxMock).deSelect(container, "A", "B");
      }

      @Test
      @DisplayName("deSelect with container and strategy delegates to component correctly")
      void testDeSelectWithContainerAndStrategy() {
         // Given
         when(checkboxMock.deSelect(container, strategy)).thenReturn("StrategyDeSelected");

         // When
         String result = service.deSelect(mockCheckboxComponentType, container, strategy);

         // Then
         assertThat(result).isEqualTo("StrategyDeSelected");
         verify(checkboxMock).deSelect(container, strategy);
      }

      @Test
      @DisplayName("deSelect with text only delegates to component correctly")
      void testDeSelectWithTextOnly() {
         // When
         service.deSelect(mockCheckboxComponentType, "A");

         // Then
         verify(checkboxMock).deSelect("A");
      }

      @Test
      @DisplayName("deSelect with locator delegates to component correctly")
      void testDeSelectWithLocator() {
         // When
         service.deSelect(mockCheckboxComponentType, locator);

         // Then
         verify(checkboxMock).deSelect(locator);
      }

      @Test
      @DisplayName("default deSelect with container and text delegates correctly")
      void testDefaultDeSelectWithContainerAndText() {
         // When
         service.deSelect(container, "A", "B");

         // Then
         verify(checkboxMock).deSelect(container, "A", "B");
      }

      @Test
      @DisplayName("default deSelect with container and strategy delegates correctly")
      void testDefaultDeSelectWithContainerAndStrategy() {
         // Given
         when(checkboxMock.deSelect(container, strategy)).thenReturn("DefaultStrategyDeSelected");

         // When
         String result = service.deSelect(container, strategy);

         // Then
         assertThat(result).isEqualTo("DefaultStrategyDeSelected");
         verify(checkboxMock).deSelect(container, strategy);
      }

      @Test
      @DisplayName("default deSelect with text only delegates correctly")
      void testDefaultDeSelectWithTextOnly() {
         // When
         service.deSelect("DefaultA");

         // Then
         verify(checkboxMock).deSelect("DefaultA");
      }

      @Test
      @DisplayName("default deSelect with locator delegates correctly")
      void testDefaultDeSelectWithLocator() {
         // When
         service.deSelect(locator);

         // Then
         verify(checkboxMock).deSelect(locator);
      }
   }

   @Nested
   @DisplayName("AreSelected Method Tests")
   class AreSelectedMethodTests {

      @Test
      @DisplayName("areSelected with container and text delegates to component correctly")
      void testAreSelectedWithContainerAndText() {
         // Given
         when(checkboxMock.areSelected(container, "A", "B")).thenReturn(true);

         // When
         boolean result = service.areSelected(mockCheckboxComponentType, container, "A", "B");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(container, "A", "B");
      }

      @Test
      @DisplayName("areSelected with text only delegates to component correctly")
      void testAreSelectedWithTextOnly() {
         // Given
         when(checkboxMock.areSelected("A")).thenReturn(true);

         // When
         boolean result = service.areSelected(mockCheckboxComponentType, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected("A");
      }

      @Test
      @DisplayName("areSelected with locator delegates to component correctly")
      void testAreSelectedWithLocator() {
         // Given
         when(checkboxMock.areSelected(locator)).thenReturn(true);

         // When
         boolean result = service.areSelected(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(locator);
      }

      @Test
      @DisplayName("default areSelected with container and text delegates correctly")
      void testDefaultAreSelectedWithContainerAndText() {
         // Given
         when(checkboxMock.areSelected(container, "A", "B")).thenReturn(true);

         // When
         boolean result = service.areSelected(container, "A", "B");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(container, "A", "B");
      }

      @Test
      @DisplayName("default areSelected with text only delegates correctly")
      void testDefaultAreSelectedWithTextOnly() {
         // Given
         when(checkboxMock.areSelected("A")).thenReturn(true);

         // When
         boolean result = service.areSelected("A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected("A");
      }

      @Test
      @DisplayName("default areSelected with locator delegates correctly")
      void testDefaultAreSelectedWithLocator() {
         // Given
         when(checkboxMock.areSelected(locator)).thenReturn(true);

         // When
         boolean result = service.areSelected(locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(locator);
      }
   }

   @Nested
   @DisplayName("IsSelected Method Tests")
   class IsSelectedMethodTests {

      @Test
      @DisplayName("isSelected with container and text delegates to component correctly")
      void testIsSelectedWithContainer() {
         // Given
         when(checkboxMock.areSelected(container, "A")).thenReturn(true);

         // When
         boolean result = service.isSelected(mockCheckboxComponentType, container, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(container, "A");
      }

      @Test
      @DisplayName("isSelected with text only delegates to component correctly")
      void testIsSelectedWithTextOnly() {
         // Given
         when(checkboxMock.areSelected("A")).thenReturn(true);

         // When
         boolean result = service.isSelected(mockCheckboxComponentType, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected("A");
      }

      @Test
      @DisplayName("isSelected with locator delegates to component correctly")
      void testIsSelectedWithLocator() {
         // Given
         when(checkboxMock.areSelected(locator)).thenReturn(true);

         // When
         boolean result = service.isSelected(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(locator);
      }

      @Test
      @DisplayName("default isSelected with container and text delegates correctly")
      void testDefaultIsSelectedWithContainer() {
         // Given
         when(checkboxMock.areSelected(container, "A")).thenReturn(true);

         // When
         boolean result = service.isSelected(container, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(container, "A");
      }

      @Test
      @DisplayName("default isSelected with text only delegates correctly")
      void testDefaultIsSelectedWithTextOnly() {
         // Given
         when(checkboxMock.areSelected("A")).thenReturn(true);

         // When
         boolean result = service.isSelected("A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected("A");
      }

      @Test
      @DisplayName("default isSelected with locator delegates correctly")
      void testDefaultIsSelectedWithLocator() {
         // Given
         when(checkboxMock.areSelected(locator)).thenReturn(true);

         // When
         boolean result = service.isSelected(locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areSelected(locator);
      }
   }

   @Nested
   @DisplayName("AreEnabled Method Tests")
   class AreEnabledMethodTests {

      @Test
      @DisplayName("areEnabled with container and text delegates to component correctly")
      void testAreEnabledWithContainerAndText() {
         // Given
         when(checkboxMock.areEnabled(container, "A", "B")).thenReturn(true);

         // When
         boolean result = service.areEnabled(mockCheckboxComponentType, container, "A", "B");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(container, "A", "B");
      }

      @Test
      @DisplayName("areEnabled with text only delegates to component correctly")
      void testAreEnabledWithTextOnly() {
         // Given
         when(checkboxMock.areEnabled("A")).thenReturn(true);

         // When
         boolean result = service.areEnabled(mockCheckboxComponentType, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled("A");
      }

      @Test
      @DisplayName("areEnabled with locator delegates to component correctly")
      void testAreEnabledWithLocator() {
         // Given
         when(checkboxMock.areEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.areEnabled(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(locator);
      }

      @Test
      @DisplayName("default areEnabled with container and text delegates correctly")
      void testDefaultAreEnabledWithContainerAndText() {
         // Given
         when(checkboxMock.areEnabled(container, "A", "B")).thenReturn(true);

         // When
         boolean result = service.areEnabled(container, "A", "B");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(container, "A", "B");
      }

      @Test
      @DisplayName("default areEnabled with text only delegates correctly")
      void testDefaultAreEnabledWithTextOnly() {
         // Given
         when(checkboxMock.areEnabled("A")).thenReturn(true);

         // When
         boolean result = service.areEnabled("A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled("A");
      }

      @Test
      @DisplayName("default areEnabled with locator delegates correctly")
      void testDefaultAreEnabledWithLocator() {
         // Given
         when(checkboxMock.areEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.areEnabled(locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(locator);
      }
   }

   @Nested
   @DisplayName("IsEnabled Method Tests")
   class IsEnabledMethodTests {

      @Test
      @DisplayName("isEnabled with container and text delegates to component correctly")
      void testIsEnabledWithContainer() {
         // Given
         when(checkboxMock.areEnabled(container, "A")).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockCheckboxComponentType, container, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(container, "A");
      }

      @Test
      @DisplayName("isEnabled with text only delegates to component correctly")
      void testIsEnabledWithTextOnly() {
         // Given
         when(checkboxMock.areEnabled("A")).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockCheckboxComponentType, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled("A");
      }

      @Test
      @DisplayName("isEnabled with locator delegates to component correctly")
      void testIsEnabledWithLocator() {
         // Given
         when(checkboxMock.areEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(locator);
      }

      @Test
      @DisplayName("default isEnabled with container and text delegates correctly")
      void testDefaultIsEnabledWithContainer() {
         // Given
         when(checkboxMock.areEnabled(container, "A")).thenReturn(true);

         // When
         boolean result = service.isEnabled(container, "A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(container, "A");
      }

      @Test
      @DisplayName("default isEnabled with text only delegates correctly")
      void testDefaultIsEnabledWithTextOnly() {
         // Given
         when(checkboxMock.areEnabled("A")).thenReturn(true);

         // When
         boolean result = service.isEnabled("A");

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled("A");
      }

      @Test
      @DisplayName("default isEnabled with locator delegates correctly")
      void testDefaultIsEnabledWithLocator() {
         // Given
         when(checkboxMock.areEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.isEnabled(locator);

         // Then
         assertThat(result).isTrue();
         verify(checkboxMock).areEnabled(locator);
      }
   }

   @Nested
   @DisplayName("Get Methods Tests")
   class GetMethodsTests {

      @Test
      @DisplayName("getSelected with container delegates to component correctly")
      void testGetSelectedWithContainer() {
         // Given
         List<String> list = Collections.singletonList("A");
         when(checkboxMock.getSelected(container)).thenReturn(list);

         // When
         List<String> result = service.getSelected(mockCheckboxComponentType, container);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getSelected(container);
      }

      @Test
      @DisplayName("getSelected with locator delegates to component correctly")
      void testGetSelectedWithLocator() {
         // Given
         List<String> list = Arrays.asList("A", "B");
         when(checkboxMock.getSelected(locator)).thenReturn(list);

         // When
         List<String> result = service.getSelected(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getSelected(locator);
      }

      @Test
      @DisplayName("getAll with container delegates to component correctly")
      void testGetAllWithContainer() {
         // Given
         List<String> list = Arrays.asList("A", "B", "C");
         when(checkboxMock.getAll(container)).thenReturn(list);

         // When
         List<String> result = service.getAll(mockCheckboxComponentType, container);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getAll(container);
      }

      @Test
      @DisplayName("getAll with locator delegates to component correctly")
      void testGetAllWithLocator() {
         // Given
         List<String> list = Arrays.asList("A", "B", "C", "D");
         when(checkboxMock.getAll(locator)).thenReturn(list);

         // When
         List<String> result = service.getAll(mockCheckboxComponentType, locator);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getAll(locator);
      }

      @Test
      @DisplayName("default getSelected with container delegates correctly")
      void testDefaultGetSelectedWithContainer() {
         // Given
         List<String> list = Collections.singletonList("A");
         when(checkboxMock.getSelected(container)).thenReturn(list);

         // When
         List<String> result = service.getSelected(container);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getSelected(container);
      }

      @Test
      @DisplayName("default getSelected with locator delegates correctly")
      void testDefaultGetSelectedWithLocator() {
         // Given
         List<String> list = Arrays.asList("A", "B");
         when(checkboxMock.getSelected(locator)).thenReturn(list);

         // When
         List<String> result = service.getSelected(locator);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getSelected(locator);
      }

      @Test
      @DisplayName("default getAll with container delegates correctly")
      void testDefaultGetAllWithContainer() {
         // Given
         List<String> list = Arrays.asList("A", "B", "C");
         when(checkboxMock.getAll(container)).thenReturn(list);

         // When
         List<String> result = service.getAll(container);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getAll(container);
      }

      @Test
      @DisplayName("default getAll with locator delegates correctly")
      void testDefaultGetAllWithLocator() {
         // Given
         List<String> list = Arrays.asList("A", "B", "C", "D");
         when(checkboxMock.getAll(locator)).thenReturn(list);

         // When
         List<String> result = service.getAll(locator);

         // Then
         assertThat(result).isEqualTo(list);
         verify(checkboxMock).getAll(locator);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused between method calls")
      void testComponentCaching() {
         // When
         service.select(mockCheckboxComponentType, container, "A");
         service.deSelect(mockCheckboxComponentType, container, "B");
         service.areSelected(mockCheckboxComponentType, container, "C");

         // Then
         factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)),
               times(1));
      }

      @Test
      @DisplayName("Multiple service instances don't share component cache")
      void testMultipleServiceInstances() throws Exception {
         // Given - Create two service instances
         CheckboxServiceImpl service1 = new CheckboxServiceImpl(driver);
         CheckboxServiceImpl service2 = new CheckboxServiceImpl(driver);

         // Reset factory mock behavior
         factoryMock.close();
         factoryMock = Mockito.mockStatic(ComponentFactory.class);

         // We need to ensure different component instances are returned for each service
         factoryMock.when(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)))
               .thenAnswer(invocation -> mock(Checkbox.class));

         // When
         service1.select(mockCheckboxComponentType, container, "ClickMe");
         service2.select(mockCheckboxComponentType, container, "ClickMe");

         // Then - We need to check internal state via reflection
         Field componentsField1 = AbstractComponentService.class.getDeclaredField("components");
         componentsField1.setAccessible(true);
         Map<?, ?> componentsMap1 = (Map<?, ?>) componentsField1.get(service1);

         Field componentsField2 = AbstractComponentService.class.getDeclaredField("components");
         componentsField2.setAccessible(true);
         Map<?, ?> componentsMap2 = (Map<?, ?>) componentsField2.get(service2);

         // Assert the maps are different objects
         assertThat(componentsMap1).isNotSameAs(componentsMap2);

         // The component map in each service should have keys (after select was called)
         assertThat(componentsMap1).isNotEmpty();
         assertThat(componentsMap2).isNotEmpty();
      }
   }

   @Nested
   @DisplayName("Insertion Method Tests")
   class InsertionMethodTests {

      @Test
      @DisplayName("insertion method delegates to checkbox component's select")
      void testInsertion() {
         // When
         service.insertion(mockCheckboxComponentType, locator, "InsertedValue");

         // Then
         verify(checkboxMock).select("InsertedValue");
      }

      @Test
      @DisplayName("insertion with non-CheckboxComponentType throws ClassCastException")
      void testInsertionWithNonCheckboxComponentType() {
         // Given
         ComponentType nonCheckboxType = mock(ComponentType.class);

         try {
            // When
            service.insertion(nonCheckboxType, locator, "value1");
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
         Method createComponentMethod =
               CheckboxServiceImpl.class.getDeclaredMethod("createComponent", CheckboxComponentType.class);
         createComponentMethod.setAccessible(true);

         // When
         createComponentMethod.invoke(service, mockCheckboxComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)),
               times(1));
      }

      @Test
      @DisplayName("checkboxComponent delegates to getOrCreateComponent")
      void testCheckboxComponent() throws Exception {
         // Given
         Method checkboxComponentMethod =
               CheckboxServiceImpl.class.getDeclaredMethod("checkboxComponent", CheckboxComponentType.class);
         checkboxComponentMethod.setAccessible(true);

         // Clear components map to ensure component creation
         Field componentsField = AbstractComponentService.class.getDeclaredField("components");
         componentsField.setAccessible(true);
         Map<CheckboxComponentType, Checkbox> componentsMap = new HashMap<>();
         componentsField.set(service, componentsMap);

         // When
         checkboxComponentMethod.invoke(service, mockCheckboxComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)),
               times(1));
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
            service.select(null, container, "ClickMe");
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
         service.select(mockCheckboxComponentType, (SmartWebElement) null, "ClickMe");

         // Then
         verify(checkboxMock).select((SmartWebElement) null, "ClickMe");
      }

      @Test
      @DisplayName("Method with null text delegates to component")
      void testMethodWithNullText() {
         // Given
         String[] nullTexts = null;

         // When
         service.select(mockCheckboxComponentType, container, nullTexts);

         // Then
         verify(checkboxMock).select(container, (String[]) null);
      }
   }

   @Test
   @DisplayName("Checkbox interface implementation test")
   void testCheckboxInterfaceImplementation() {
      // Create a concrete implementation of Checkbox that uses the default implementations
      Checkbox defaultCheckbox = new Checkbox() {
         @Override
         public void select(SmartWebElement container, String... checkBoxText) {
         }

         @Override
         public String select(SmartWebElement container, Strategy strategy) {
            return "";
         }

         @Override
         public void select(String... checkBoxText) {
         }

         @Override
         public void select(By... checkBoxLocator) {
         }

         @Override
         public void deSelect(SmartWebElement container, String... checkBoxText) {
         }

         @Override
         public String deSelect(SmartWebElement container, Strategy strategy) {
            return "";
         }

         @Override
         public void deSelect(String... checkBoxText) {
         }

         @Override
         public void deSelect(By... checkBoxLocator) {
         }

         @Override
         public boolean areSelected(SmartWebElement container, String... checkBoxText) {
            return false;
         }

         @Override
         public boolean areSelected(String... checkBoxText) {
            return false;
         }

         @Override
         public boolean areSelected(By... checkBoxLocator) {
            return false;
         }

         @Override
         public boolean areEnabled(SmartWebElement container, String... checkBoxText) {
            return false;
         }

         @Override
         public boolean areEnabled(String... checkBoxText) {
            return false;
         }

         @Override
         public boolean areEnabled(By... checkBoxLocator) {
            return false;
         }

         @Override
         public List<String> getSelected(SmartWebElement container) {
            return Collections.emptyList();
         }

         @Override
         public List<String> getSelected(By containerLocator) {
            return Collections.emptyList();
         }

         @Override
         public List<String> getAll(SmartWebElement container) {
            return Collections.emptyList();
         }

         @Override
         public List<String> getAll(By containerLocator) {
            return Collections.emptyList();
         }
      };

      // Just verifying the implementation works without exceptions
      assertThat(defaultCheckbox).isNotNull();
   }
}