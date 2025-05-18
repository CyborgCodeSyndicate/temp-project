package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a custom filtering mechanism for a table cell.
 * This annotation allows specifying a {@link CellFilterFunction} to handle
 * filtering logic beyond the standard component-based approach.
 *
 * <p>Applied to fields in a row model, it enables dynamic filtering using a
 * function that executes at runtime.
 *
 * <p>The provided {@code cellFilterFunction} class must implement {@code CellFilterFunction}
 * to define the filtering logic.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomCellFilter {

   /**
    * Specifies the filtering function used to filter table data dynamically.
    *
    * @return the class implementing {@link CellFilterFunction}.
    */
   Class<? extends CellFilterFunction> cellFilterFunction();
}
