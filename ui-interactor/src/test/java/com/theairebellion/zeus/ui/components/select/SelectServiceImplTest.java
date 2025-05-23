package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("SelectServiceImpl Unit Tests")
class SelectServiceImplTest extends BaseUnitUITest {

   private static final String[] OPTIONS_ARRAY = {"option1", "option2"};
   private static final String SINGLE_OPTION = "option1";
   private static final List<String> MOCK_OPTIONS_LIST = List.of("optA", "optB");
   private static final String OPTION_VALUE = "optionValue";
   private final MockSelectComponentType componentType = MockSelectComponentType.DUMMY_SELECT;
   @Mock
   private SmartWebDriver driver;
   @Mock
   private SmartWebElement container;
   @Mock
   private By locator;
   @Mock
   private Strategy strategy;
   @Mock
   private Select selectMock;
   private SelectServiceImpl service;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      service = new SelectServiceImpl(driver);
      locator = By.id("testSelect");

      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getSelectComponent(any(SelectComponentType.class), eq(driver)))
            .thenReturn(selectMock);
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   @Nested
   @DisplayName("Method Delegation Tests")
   class MethodDelegationTests {

      @Test
      @DisplayName("selectOptions with SmartWebElement correctly delegates")
      void selectOptionsWithSmartWebElement() {
         // Given - setup in @BeforeEach

         // When
         service.selectOptions(componentType, container, OPTIONS_ARRAY);

         // Then
         verify(selectMock).selectOptions(container, OPTIONS_ARRAY);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("selectOption with SmartWebElement correctly delegates")
      void selectOptionWithSmartWebElement() {
         // Given - setup in @BeforeEach

         // When
         service.selectOption(componentType, container, SINGLE_OPTION);

         // Then
         verify(selectMock).selectOptions(container, SINGLE_OPTION);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("selectOptions with By locator correctly delegates")
      void selectOptionsWithByLocator() {
         // Given - setup in @BeforeEach

         // When
         service.selectOptions(componentType, locator, OPTIONS_ARRAY);

         // Then
         verify(selectMock).selectOptions(locator, OPTIONS_ARRAY);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("selectOption with By locator correctly delegates")
      void selectOptionWithByLocator() {
         // Given - setup in @BeforeEach

         // When
         service.selectOption(componentType, locator, SINGLE_OPTION);

         // Then
         verify(selectMock).selectOptions(locator, SINGLE_OPTION);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("selectOptions with SmartWebElement and Strategy correctly delegates")
      void selectOptionsWithSmartWebElementAndStrategy() {
         // Given
         when(selectMock.selectOptions(container, strategy)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.selectOptions(componentType, container, strategy);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).selectOptions(container, strategy);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("selectOptions with By locator and Strategy correctly delegates")
      void selectOptionsWithByLocatorAndStrategy() {
         // Given
         when(selectMock.selectOptions(locator, strategy)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.selectOptions(componentType, locator, strategy);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).selectOptions(locator, strategy);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("getAvailableOptions with SmartWebElement correctly delegates")
      void getAvailableOptionsWithSmartWebElement() {
         // Given
         when(selectMock.getAvailableOptions(container)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.getAvailableOptions(componentType, container);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).getAvailableOptions(container);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("getAvailableOptions with By locator correctly delegates")
      void getAvailableOptionsWithByLocator() {
         // Given
         when(selectMock.getAvailableOptions(locator)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.getAvailableOptions(componentType, locator);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).getAvailableOptions(locator);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("getSelectedOptions with SmartWebElement correctly delegates")
      void getSelectedOptionsWithSmartWebElement() {
         // Given
         when(selectMock.getSelectedOptions(container)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.getSelectedOptions(componentType, container);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).getSelectedOptions(container);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("getSelectedOptions with By locator correctly delegates")
      void getSelectedOptionsWithByLocator() {
         // Given
         when(selectMock.getSelectedOptions(locator)).thenReturn(MOCK_OPTIONS_LIST);

         // When
         var result = service.getSelectedOptions(componentType, locator);

         // Then
         assertThat(result).isEqualTo(MOCK_OPTIONS_LIST);
         verify(selectMock).getSelectedOptions(locator);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("isOptionVisible with SmartWebElement correctly delegates")
      void isOptionVisibleWithSmartWebElement() {
         // Given
         when(selectMock.isOptionVisible(container, OPTION_VALUE)).thenReturn(true);

         // When
         var result = service.isOptionVisible(componentType, container, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(container, OPTION_VALUE);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("isOptionVisible with By locator correctly delegates")
      void isOptionVisibleWithByLocator() {
         // Given
         when(selectMock.isOptionVisible(locator, OPTION_VALUE)).thenReturn(true);

         // When
         var result = service.isOptionVisible(componentType, locator, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(locator, OPTION_VALUE);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("isOptionEnabled with SmartWebElement correctly delegates")
      void isOptionEnabledWithSmartWebElement() {
         // Given
         when(selectMock.isOptionEnabled(container, OPTION_VALUE)).thenReturn(true);

         // When
         var result = service.isOptionEnabled(componentType, container, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(container, OPTION_VALUE);
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("isOptionEnabled with By locator correctly delegates")
      void isOptionEnabledWithByLocator() {
         // Given
         when(selectMock.isOptionEnabled(locator, OPTION_VALUE)).thenReturn(true);

         // When
         var result = service.isOptionEnabled(componentType, locator, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(locator, OPTION_VALUE);
         verifyNoMoreInteractions(selectMock);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused")
      void componentCaching() {
         // Given - setup in @BeforeEach

         // When
         service.selectOption(componentType, container, SINGLE_OPTION);
         service.getAvailableOptions(componentType, container);

         // Then
         factoryMock.verify(() -> ComponentFactory.getSelectComponent(eq(componentType), eq(driver)), times(1));
      }
   }

   @Nested
   @DisplayName("Insertion Interface Method Tests")
   class InsertionInterfaceMethodTests {

      @Test
      @DisplayName("insertion method converts objects to strings and delegates")
      void insertionConvertsObjectsAndDelegates() {
         // Given
         var obj1 = 42;
         var obj2 = true;
         var obj3 = 3.14;
         var expectedStrings = new String[] {"42", "true", "3.14"};

         // When
         service.insertion(componentType, locator, obj1, obj2, obj3);

         // Then
         verify(selectMock).selectOptions(eq(locator), eq(expectedStrings));
         factoryMock.verify(() -> ComponentFactory.getSelectComponent(eq(componentType), eq(driver)), times(1));
         verifyNoMoreInteractions(selectMock);
      }

      @Test
      @DisplayName("insertion with non-SelectComponentType throws exception")
      void testInsertionWithNonSelectComponentType() {
         // Given
         ComponentType nonSelectType = mock(ComponentType.class);

         // When / Then
         assertThatThrownBy(() -> service.insertion(nonSelectType, locator, SINGLE_OPTION))
               .isInstanceOf(IllegalArgumentException.class);

         factoryMock.verifyNoInteractions();
         verifyNoInteractions(selectMock);
      }
   }
}