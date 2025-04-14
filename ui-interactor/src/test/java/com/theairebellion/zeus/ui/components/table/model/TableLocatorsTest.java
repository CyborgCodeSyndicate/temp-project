package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableLocatorsTest extends BaseUnitUITest {

   @Test
   void testConstructorAndGetters() {
      By containerLocator = By.id("container");
      By rowsLocator = By.tagName("tr");
      By headerLocator = By.className("header");
      TableLocators locators = new TableLocators(containerLocator, rowsLocator, headerLocator);
      assertEquals(containerLocator, locators.getTableContainerLocator());
      assertEquals(rowsLocator, locators.getTableRowsLocator());
      assertEquals(headerLocator, locators.getHeaderRowLocator());
   }
}