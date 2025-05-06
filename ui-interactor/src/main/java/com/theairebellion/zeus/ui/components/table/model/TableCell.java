package com.theairebellion.zeus.ui.components.table.model;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a table cell in the UI, encapsulating both the underlying web element
 * and its textual content.
 *
 * <p>This class is used for handling table cell interactions in the test automation framework.
 * It provides access to the {@link SmartWebElement} representing the cell and its extracted text.
 *
 * <p>Used in table processing and data validation operations.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@AllArgsConstructor
@Getter
@Setter
public class TableCell {

   /**
    * The {@link SmartWebElement} representing the table cell.
    */
   private SmartWebElement element;

   /**
    * The textual content of the table cell.
    */
   private String text;

   /**
    * Constructs a {@code TableCell} with only a text value.
    * This is useful when the actual element reference is not required.
    *
    * @param value The text content of the cell.
    */
   public TableCell(final String value) {
      this.text = value;
   }
}
