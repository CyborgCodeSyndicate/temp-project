package com.theairebellion.zeus.ui.components.table.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.openqa.selenium.support.FindBy;

/**
 * Defines locators for identifying a specific cell within a table.
 * This annotation is applied to fields in a row model class to specify how
 * table cells are located in the UI.
 *
 * <p>It provides three locators:
 * <ul>
 *   <li>{@code cellLocator} - Identifies the cell in a row.</li>
 *   <li>{@code headerCellLocator} - Identifies the corresponding column header.</li>
 *   <li>{@code cellTextLocator} - (Optional) Identifies the text element inside the cell.</li>
 * </ul>
 *
 * <p>The table framework processes this annotation at runtime to extract
 * and interact with table data dynamically.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableCellLocator {

   /**
    * Locator for identifying the cell within a table row.
    *
    * @return the {@link FindBy} annotation specifying the cell locator.
    */
   FindBy cellLocator();

   /**
    * (Optional) Specifies the table section if the table consists of multiple sections.
    *
    * @return the name of the table section, default is an empty string.
    */
   String tableSection() default "";

   /**
    * (Optional) Locator for extracting the text content inside the cell.
    * Defaults to the current element.
    *
    * @return the {@link FindBy} annotation specifying the text locator.
    */
   FindBy cellTextLocator() default @FindBy(xpath = ".");

   /**
    * Locator for identifying the corresponding column header.
    *
    * @return the {@link FindBy} annotation specifying the header locator.
    */
   FindBy headerCellLocator() default @FindBy(xpath = ".");

}
