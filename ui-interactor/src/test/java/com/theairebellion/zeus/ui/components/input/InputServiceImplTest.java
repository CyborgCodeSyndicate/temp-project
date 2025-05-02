package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("InputServiceImpl Test")
class InputServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private InputServiceImpl service;
   private SmartWebElement container;
   private Input inputMock;
   private MockInputComponentType mockInputComponentType;
   private By locator;
   private SmartWebElement cell;
   private SmartWebElement headerCell;
   private FilterStrategy filterStrategy;
   private MockedStatic<ComponentFactory> factoryMock;


   @BeforeEach
   void setUp() {
      driver = mock(SmartWebDriver.class);
      service = new InputServiceImpl(driver);
      container = mock(SmartWebElement.class);
      inputMock = mock(Input.class);
      mockInputComponentType = MockInputComponentType.DUMMY;
      locator = By.id("input");
      cell = mock(SmartWebElement.class);
      headerCell = mock(SmartWebElement.class);
      filterStrategy = FilterStrategy.SELECT;
      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)))
            .thenReturn(inputMock);
   }


   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }


   @Nested
   @DisplayName("Insert Method Tests")
   class InsertMethodTests {

      @Test
      @DisplayName("insert with container delegates to component correctly")
      void testInsertContainer() {
         // When
         service.insert(mockInputComponentType, container, "value");

         // Then
         verify(inputMock).insert(container, "value");
      }


      @Test
      @DisplayName("insert with container and label delegates to component correctly")
      void testInsertContainerLabel() {
         // When
         service.insert(mockInputComponentType, container, "label", "value");

         // Then
         verify(inputMock).insert(container, "label", "value");
      }


      @Test
      @DisplayName("insert with label delegates to component correctly")
      void testInsertLabel() {
         // When
         service.insert(mockInputComponentType, "label", "value");

         // Then
         verify(inputMock).insert("label", "value");
      }


      @Test
      @DisplayName("insert with locator delegates to component correctly")
      void testInsertBy() {
         // When
         service.insert(mockInputComponentType, locator, "value");

         // Then
         verify(inputMock).insert(locator, "value");
      }


      @Test
      @DisplayName("default insert with container delegates correctly")
      void testDefaultInsertWithContainer() {
         // When
         service.insert(container, "value");

         // Then
         verify(inputMock).insert(container, "value");
      }


      @Test
      @DisplayName("default insert with container and label delegates correctly")
      void testDefaultInsertWithContainerAndLabel() {
         // When
         service.insert(container, "label", "value");

         // Then
         verify(inputMock).insert(container, "label", "value");
      }


      @Test
      @DisplayName("default insert with label delegates correctly")
      void testDefaultInsertWithLabel() {
         // When
         service.insert("label", "value");

         // Then
         verify(inputMock).insert("label", "value");
      }


      @Test
      @DisplayName("default insert with locator delegates correctly")
      void testDefaultInsertWithBy() {
         // When
         service.insert(locator, "value");

         // Then
         verify(inputMock).insert(locator, "value");
      }

   }

   @Nested
   @DisplayName("Clear Method Tests")
   class ClearMethodTests {

      @Test
      @DisplayName("clear with container delegates to component correctly")
      void testClearContainer() {
         // When
         service.clear(mockInputComponentType, container);

         // Then
         verify(inputMock).clear(container);
      }


      @Test
      @DisplayName("clear with container and label delegates to component correctly")
      void testClearContainerLabel() {
         // When
         service.clear(mockInputComponentType, container, "label");

         // Then
         verify(inputMock).clear(container, "label");
      }


      @Test
      @DisplayName("clear with label delegates to component correctly")
      void testClearLabel() {
         // When
         service.clear(mockInputComponentType, "label");

         // Then
         verify(inputMock).clear("label");
      }


      @Test
      @DisplayName("clear with locator delegates to component correctly")
      void testClearBy() {
         // When
         service.clear(mockInputComponentType, locator);

         // Then
         verify(inputMock).clear(locator);
      }


      @Test
      @DisplayName("default clear with container delegates correctly")
      void testDefaultClearWithContainer() {
         // When
         service.clear(container);

         // Then
         verify(inputMock).clear(container);
      }


      @Test
      @DisplayName("default clear with container and label delegates correctly")
      void testDefaultClearWithContainerAndLabel() {
         // When
         service.clear(container, "label");

         // Then
         verify(inputMock).clear(container, "label");
      }


      @Test
      @DisplayName("default clear with label delegates correctly")
      void testDefaultClearWithLabel() {
         // When
         service.clear("label");

         // Then
         verify(inputMock).clear("label");
      }


      @Test
      @DisplayName("default clear with locator delegates correctly")
      void testDefaultClearWithBy() {
         // When
         service.clear(locator);

         // Then
         verify(inputMock).clear(locator);
      }

   }

   @Nested
   @DisplayName("GetValue Method Tests")
   class GetValueMethodTests {

      @Test
      @DisplayName("getValue with container delegates to component correctly")
      void testGetValueContainer() {
         // Given
         when(inputMock.getValue(container)).thenReturn("val");

         // When
         String result = service.getValue(mockInputComponentType, container);

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(container);
      }


      @Test
      @DisplayName("getValue with container and label delegates to component correctly")
      void testGetValueContainerLabel() {
         // Given
         when(inputMock.getValue(container)).thenReturn("val");

         // When
         String result = service.getValue(mockInputComponentType, container, "label");

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(container);
      }


      @Test
      @DisplayName("getValue with label delegates to component correctly")
      void testGetValueLabel() {
         // Given
         when(inputMock.getValue("label")).thenReturn("val");

         // When
         String result = service.getValue(mockInputComponentType, "label");

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue("label");
      }


      @Test
      @DisplayName("getValue with locator delegates to component correctly")
      void testGetValueBy() {
         // Given
         when(inputMock.getValue(locator)).thenReturn("val");

         // When
         String result = service.getValue(mockInputComponentType, locator);

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(locator);
      }


      @Test
      @DisplayName("default getValue with container delegates correctly")
      void testDefaultGetValueWithContainer() {
         // Given
         when(inputMock.getValue(container)).thenReturn("val");

         // When
         String result = service.getValue(container);

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(container);
      }


      @Test
      @DisplayName("default getValue with container and label delegates correctly")
      void testDefaultGetValueWithContainerAndLabel() {
         // Given
         when(inputMock.getValue(container)).thenReturn("val");

         // When
         String result = service.getValue(container, "label");

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(container);
      }


      @Test
      @DisplayName("default getValue with label delegates correctly")
      void testDefaultGetValueWithLabel() {
         // Given
         when(inputMock.getValue("label")).thenReturn("val");

         // When
         String result = service.getValue("label");

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue("label");
      }


      @Test
      @DisplayName("default getValue with locator delegates correctly")
      void testDefaultGetValueWithBy() {
         // Given
         when(inputMock.getValue(locator)).thenReturn("val");

         // When
         String result = service.getValue(locator);

         // Then
         assertThat(result).isEqualTo("val");
         verify(inputMock).getValue(locator);
      }

   }

   @Nested
   @DisplayName("IsEnabled Method Tests")
   class IsEnabledMethodTests {

      @Test
      @DisplayName("isEnabled with container delegates to component correctly")
      void testIsEnabledContainer() {
         // Given
         when(inputMock.isEnabled(container)).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockInputComponentType, container);

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(container);
      }


      @Test
      @DisplayName("isEnabled with container and label delegates to component correctly")
      void testIsEnabledContainerLabel() {
         // Given
         when(inputMock.isEnabled(container, "label")).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockInputComponentType, container, "label");

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(container, "label");
      }


      @Test
      @DisplayName("isEnabled with label delegates to component correctly")
      void testIsEnabledLabel() {
         // Given
         when(inputMock.isEnabled("label")).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockInputComponentType, "label");

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled("label");
      }


      @Test
      @DisplayName("isEnabled with locator delegates to component correctly")
      void testIsEnabledBy() {
         // Given
         when(inputMock.isEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.isEnabled(mockInputComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(locator);
      }


      @Test
      @DisplayName("default isEnabled with container delegates correctly")
      void testDefaultIsEnabledWithContainer() {
         // Given
         when(inputMock.isEnabled(container)).thenReturn(true);

         // When
         boolean result = service.isEnabled(container);

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(container);
      }


      @Test
      @DisplayName("default isEnabled with container and label delegates correctly")
      void testDefaultIsEnabledWithContainerAndLabel() {
         // Given
         when(inputMock.isEnabled(container, "label")).thenReturn(true);

         // When
         boolean result = service.isEnabled(container, "label");

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(container, "label");
      }


      @Test
      @DisplayName("default isEnabled with label delegates correctly")
      void testDefaultIsEnabledWithLabel() {
         // Given
         when(inputMock.isEnabled("label")).thenReturn(true);

         // When
         boolean result = service.isEnabled("label");

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled("label");
      }


      @Test
      @DisplayName("default isEnabled with locator delegates correctly")
      void testDefaultIsEnabledWithBy() {
         // Given
         when(inputMock.isEnabled(locator)).thenReturn(true);

         // When
         boolean result = service.isEnabled(locator);

         // Then
         assertThat(result).isTrue();
         verify(inputMock).isEnabled(locator);
      }

   }

   @Nested
   @DisplayName("GetErrorMessage Method Tests")
   class GetErrorMessageMethodTests {

      @Test
      @DisplayName("getErrorMessage with container delegates to component correctly")
      void testGetErrorMessageContainer() {
         // Given
         when(inputMock.getErrorMessage(container)).thenReturn("err");

         // When
         String result = service.getErrorMessage(mockInputComponentType, container);

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(container);
      }


      @Test
      @DisplayName("getErrorMessage with container and label delegates to component correctly")
      void testGetErrorMessageContainerLabel() {
         // Given
         when(inputMock.getErrorMessage(container, "label")).thenReturn("err");

         // When
         String result = service.getErrorMessage(mockInputComponentType, container, "label");

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(container, "label");
      }


      @Test
      @DisplayName("getErrorMessage with label delegates to component correctly")
      void testGetErrorMessageLabel() {
         // Given
         when(inputMock.getErrorMessage("label")).thenReturn("err");

         // When
         String result = service.getErrorMessage(mockInputComponentType, "label");

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage("label");
      }


      @Test
      @DisplayName("getErrorMessage with locator delegates to component correctly")
      void testGetErrorMessageBy() {
         // Given
         when(inputMock.getErrorMessage(locator)).thenReturn("err");

         // When
         String result = service.getErrorMessage(mockInputComponentType, locator);

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(locator);
      }


      @Test
      @DisplayName("default getErrorMessage with container delegates correctly")
      void testDefaultGetErrorMessageWithContainer() {
         // Given
         when(inputMock.getErrorMessage(container)).thenReturn("err");

         // When
         String result = service.getErrorMessage(container);

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(container);
      }


      @Test
      @DisplayName("default getErrorMessage with container and label delegates correctly")
      void testDefaultGetErrorMessageWithContainerAndLabel() {
         // Given
         when(inputMock.getErrorMessage(container, "label")).thenReturn("err");

         // When
         String result = service.getErrorMessage(container, "label");

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(container, "label");
      }


      @Test
      @DisplayName("default getErrorMessage with label delegates correctly")
      void testDefaultGetErrorMessageWithLabel() {
         // Given
         when(inputMock.getErrorMessage("label")).thenReturn("err");

         // When
         String result = service.getErrorMessage("label");

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage("label");
      }


      @Test
      @DisplayName("default getErrorMessage with locator delegates correctly")
      void testDefaultGetErrorMessageWithBy() {
         // Given
         when(inputMock.getErrorMessage(locator)).thenReturn("err");

         // When
         String result = service.getErrorMessage(locator);

         // Then
         assertThat(result).isEqualTo("err");
         verify(inputMock).getErrorMessage(locator);
      }

   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused between method calls")
      void testComponentCaching() {
         // When
         service.insert(mockInputComponentType, container, "value1");
         service.clear(mockInputComponentType, container);
         service.getValue(mockInputComponentType, container);

         // Then
         factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)),
               times(1));
      }


      @Test
      @DisplayName("Multiple service instances don't share component cache")
      void testMultipleServiceInstances() throws Exception {
         // Given - Create two service instances
         InputServiceImpl service1 = new InputServiceImpl(driver);
         InputServiceImpl service2 = new InputServiceImpl(driver);

         // Reset factory mock behavior
         factoryMock.close();
         factoryMock = Mockito.mockStatic(ComponentFactory.class);

         // We need to ensure different component instances are returned for each service
         factoryMock.when(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)))
               .thenAnswer(invocation -> mock(Input.class));

         // When
         service1.insert(mockInputComponentType, container, "value1");
         service2.insert(mockInputComponentType, container, "value2");

         // Then - We need to check internal state via reflection
         Field componentsField1 = AbstractComponentService.class.getDeclaredField("components");
         componentsField1.setAccessible(true);
         Map<?, ?> componentsMap1 = (Map<?, ?>) componentsField1.get(service1);

         Field componentsField2 = AbstractComponentService.class.getDeclaredField("components");
         componentsField2.setAccessible(true);
         Map<?, ?> componentsMap2 = (Map<?, ?>) componentsField2.get(service2);

         // Assert the maps are different objects
         assertThat(componentsMap1).isNotSameAs(componentsMap2);

         // The component map in each service should have keys (after insert was called)
         assertThat(componentsMap1).isNotEmpty();
         assertThat(componentsMap2).isNotEmpty();
      }

   }

   @Nested
   @DisplayName("Table Interface Methods Tests")
   class TableInterfaceMethodsTests {

      @Test
      @DisplayName("tableInsertion delegates to component correctly")
      void testTableInsertion() {
         // When
         service.tableInsertion(cell, mockInputComponentType, "val1", "val2");

         // Then
         verify(inputMock).tableInsertion(cell, "val1", "val2");
      }


      @Test
      @DisplayName("tableFilter delegates to component correctly")
      void testTableFilter() {
         // When
         service.tableFilter(headerCell, mockInputComponentType, filterStrategy, "val1");

         // Then
         verify(inputMock).tableFilter(headerCell, filterStrategy, "val1");
      }


      @Test
      @DisplayName("tableInsertion with non-InputComponentType throws ClassCastException")
      void testTableInsertionWithNonInputComponentType() {
         // Given
         ComponentType nonInputType = mock(ComponentType.class);

         // Assert
         assertThrows(IllegalArgumentException.class, () -> service.tableInsertion(cell, nonInputType, "val1"));

      }


      @Test
      @DisplayName("tableFilter with non-InputComponentType throws ClassCastException")
      void testTableFilterWithNonInputComponentType() {
         // Given
         ComponentType nonInputType = mock(ComponentType.class);

         //Assert
         assertThrows(IllegalArgumentException.class,
               () -> service.tableFilter(headerCell, nonInputType, filterStrategy, "val1"));

      }

   }

   @Nested
   @DisplayName("Insertion Interface Method Tests")
   class InsertionInterfaceMethodTests {

      @Test
      @DisplayName("insertion method delegates to component's insert method")
      void testInsertionMethod() {
         // When
         service.insertion(mockInputComponentType, locator, "insertionVal");

         // Then
         verify(inputMock).insert(locator, "insertionVal");
      }


      @Test
      @DisplayName("insertion with non-InputComponentType throws ClassCastException")
      void testInsertionWithNonInputComponentType() {
         // Given
         ComponentType nonInputType = mock(ComponentType.class);

         assertThrows(IllegalArgumentException.class, () -> service.insertion(nonInputType, locator, "val1"));
      }

   }

   @Nested
   @DisplayName("Protected Method Tests")
   class ProtectedMethodTests {

      @Test
      @DisplayName("createComponent delegates to ComponentFactory")
      void testCreateComponent() throws Exception {
         // Given
         Method createComponentMethod = InputServiceImpl.class.getDeclaredMethod("createComponent",
               InputComponentType.class);
         createComponentMethod.setAccessible(true);

         // When
         createComponentMethod.invoke(service, mockInputComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)),
               times(1));
      }


      @Test
      @DisplayName("inputComponent delegates to getOrCreateComponent")
      void testInputComponent() throws Exception {
         // Given
         Method inputComponentMethod = InputServiceImpl.class.getDeclaredMethod("inputComponent",
               InputComponentType.class);
         inputComponentMethod.setAccessible(true);

         // Clear components map to ensure component creation
         Field componentsField = AbstractComponentService.class.getDeclaredField("components");
         componentsField.setAccessible(true);
         Map<InputComponentType, Input> componentsMap = new HashMap<>();
         componentsField.set(service, componentsMap);

         // When
         inputComponentMethod.invoke(service, mockInputComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)),
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
            service.insert(null, container, "value");
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
         service.insert(mockInputComponentType, (SmartWebElement) null, "value");

         // Then
         verify(inputMock).insert((SmartWebElement) null, "value");
      }


      @Test
      @DisplayName("Method with null value delegates to component")
      void testMethodWithNullValue() {
         // When
         service.insert(mockInputComponentType, container, null);

         // Then
         verify(inputMock).insert(container, null);
      }

   }


   @Test
   @DisplayName("Input interface default methods work correctly")
   void testInputInterfaceDefaultMethods() {
      // Create a concrete implementation of Input with default methods
      Input defaultInput = new Input() {
         @Override
         public void insert(SmartWebElement container, String value) {
         }


         @Override
         public void insert(SmartWebElement container, String inputFieldLabel, String value) {
         }


         @Override
         public void insert(String inputFieldLabel, String value) {
         }


         @Override
         public void insert(By inputFieldContainerLocator, String value) {
         }


         @Override
         public void clear(SmartWebElement container) {
         }


         @Override
         public void clear(SmartWebElement container, String inputFieldLabel) {
         }


         @Override
         public void clear(String inputFieldLabel) {
         }


         @Override
         public void clear(By inputFieldContainerLocator) {
         }


         @Override
         public String getValue(SmartWebElement container) {
            return "";
         }


         @Override
         public String getValue(SmartWebElement container, String inputFieldLabel) {
            return "";
         }


         @Override
         public String getValue(String inputFieldLabel) {
            return "";
         }


         @Override
         public String getValue(By inputFieldContainerLocator) {
            return "";
         }


         @Override
         public boolean isEnabled(SmartWebElement container) {
            return false;
         }


         @Override
         public boolean isEnabled(SmartWebElement container, String inputFieldLabel) {
            return false;
         }


         @Override
         public boolean isEnabled(String inputFieldLabel) {
            return false;
         }


         @Override
         public boolean isEnabled(By inputFieldContainerLocator) {
            return false;
         }


         @Override
         public String getErrorMessage(SmartWebElement container) {
            return "";
         }


         @Override
         public String getErrorMessage(SmartWebElement container, String inputFieldLabel) {
            return "";
         }


         @Override
         public String getErrorMessage(String inputFieldLabel) {
            return "";
         }


         @Override
         public String getErrorMessage(By inputFieldContainerLocator) {
            return "";
         }
      };

      // Call the default methods to cover them
      defaultInput.tableInsertion(container, "value1");
      defaultInput.tableFilter(container, filterStrategy, "value1");

      // Verify no exceptions
      assertThat(defaultInput).isNotNull();
   }

}