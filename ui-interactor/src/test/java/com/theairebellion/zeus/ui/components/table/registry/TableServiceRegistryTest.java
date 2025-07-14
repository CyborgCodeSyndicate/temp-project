package com.theairebellion.zeus.ui.components.table.registry;

import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@DisplayName("TableServiceRegistry Tests")
class TableServiceRegistryTest extends BaseUnitUITest {

   private final Class<MockInputComponentType> testComponentTypeClass = MockInputComponentType.class;
   private TableServiceRegistry registry;

   @BeforeEach
   void setUp() {
      // Given
      registry = new TableServiceRegistry();
   }

   @Nested
   @DisplayName("Registration and Retrieval")
   class RegistrationTests {
      @Test
      @DisplayName("should register and retrieve TableInsertion service")
      void testRegisterAndGetTableInsertion() {
         // Given
         var insertionService = mock(TableInsertion.class);

         // When
         registry.registerService(testComponentTypeClass, insertionService);
         var retrievedService = registry.getTableService(testComponentTypeClass);

         // Then
         assertSame(insertionService, retrievedService);
      }

      @Test
      @DisplayName("should register and retrieve TableFilter service")
      void testRegisterAndGetTableFilter() {
         // Given
         var filterService = mock(TableFilter.class);

         // When
         registry.registerService(testComponentTypeClass, filterService);
         var retrievedService = registry.getFilterService(testComponentTypeClass);

         // Then
         assertSame(filterService, retrievedService);
      }
   }

   @Nested
   @DisplayName("Lookup Unregistered Services")
   class LookupTests {
      @Test
      @DisplayName("should return null for unregistered TableInsertion service")
      void testGetTableServiceNotRegistered() {
         // Given - registry is empty

         // When
         var retrievedService = registry.getTableService(testComponentTypeClass);

         // Then
         assertNull(retrievedService);
      }

      @Test
      @DisplayName("should return null for unregistered TableFilter service")
      void testGetFilterServiceNotRegistered() {
         // Given - registry is empty

         // When
         var retrievedService = registry.getFilterService(testComponentTypeClass);

         // Then
         assertNull(retrievedService);
      }
   }
}