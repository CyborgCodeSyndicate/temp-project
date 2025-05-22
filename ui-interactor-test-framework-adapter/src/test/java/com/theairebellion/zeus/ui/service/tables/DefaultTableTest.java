package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.service.TableImpl;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DefaultTableTest {

   @Test
   void constructor_shouldCreateInstanceOfDefaultTable() {
      // Arrange
      SmartWebDriver mockDriver = mock(SmartWebDriver.class);

      // Act
      DefaultTable table = new DefaultTable(mockDriver);

      // Assert
      assertNotNull(table, "DefaultTable should be instantiated");
      assertInstanceOf(DefaultTable.class, table, "Should be instance of DefaultTable");
      assertInstanceOf(TableImpl.class, table, "Should also be instance of TableImpl");
   }
}