package com.theairebellion.zeus.ui.components.table.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * Stores locator information for identifying key table elements in the UI.
 *
 * <p>This class is used to define locators for interacting with tables, including
 * the table container, rows, and header row. It is primarily utilized in
 * table-related operations within the test automation framework.
 *
 * <p>Used in table processing, row extraction, and validation operations.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@AllArgsConstructor
@Getter
public class TableLocators {

   /**
    * Locator for the table container element.
    */
   private By tableContainerLocator;

   /**
    * Locator for identifying all rows within the table.
    */
   private By tableRowsLocator;

   /**
    * Locator for the table's header row.
    */
   private By headerRowLocator;
}
