package com.theairebellion.zeus.ui.components.table.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.openqa.selenium.support.FindBy;

/**
 * Defines metadata for locating and interacting with a table in the UI.
 * This annotation is applied to a row model class to specify how the table is
 * identified and structured.
 *
 * <p>It provides three key locators:</p>
 * <ul>
 *   <li>{@code tableContainerLocator} - Identifies the table container element.</li>
 *   <li>{@code rowsLocator} - Identifies the rows inside the table.</li>
 *   <li>{@code headerRowLocator} - Identifies the header row.</li>
 * </ul>
 *
 * <p>The table framework uses this annotation at runtime to extract table structure
 * and dynamically read or manipulate table data.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableInfo {

   /**
    * Locator for identifying the table container.
    *
    * @return the {@link FindBy} annotation specifying the table container locator.
    */
   FindBy tableContainerLocator();

   /**
    * Locator for identifying the rows inside the table.
    *
    * @return the {@link FindBy} annotation specifying the rows locator.
    */
   FindBy rowsLocator();

   /**
    * Locator for identifying the table's header row.
    *
    * @return the {@link FindBy} annotation specifying the header row locator.
    */
   FindBy headerRowLocator();

}
