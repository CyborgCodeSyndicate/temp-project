package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a custom function for inserting data into a table cell.
 * This annotation allows defining a {@link CellInsertionFunction} that provides
 * a dynamic insertion mechanism instead of relying on a predefined component type.
 *
 * <p>Applied to fields in a row model, it enables executing custom logic
 * when inserting values into table cells.
 *
 * <p>The insertion function must implement {@code CellInsertionFunction}, defining
 * how data should be inserted into the cell.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomCellInsertion {

   /**
    * The custom function responsible for inserting values into the cell.
    *
    * @return the class implementing {@link CellInsertionFunction}.
    */
   Class<? extends CellInsertionFunction> insertionFunction();

   /**
    * Defines the execution order when multiple insertions exist within the same row.
    *
    * @return the order in which the insertion should be executed, default is 0.
    */
   int order() default 0;
}
