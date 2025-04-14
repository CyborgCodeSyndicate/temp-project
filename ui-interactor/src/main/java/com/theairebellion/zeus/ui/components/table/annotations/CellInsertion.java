package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a table cell supports value insertion using a defined UI component.
 * This annotation is used within row model classes to automate cell interactions.
 *
 * <p>It works alongside table processing logic to determine how and where values
 * should be inserted using the specified {@link #type()} and {@link #componentType()}.</p>
 *
 * <p>The {@code order} parameter controls execution priority when multiple insertions exist.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellInsertion {

   /**
    * The component type class responsible for handling the insertion process.
    *
    * @return the class representing the component type.
    */
   Class<? extends ComponentType> type();

   /**
    * The specific component type identifier for insertion.
    *
    * @return a string representing the component type.
    */
   String componentType();

   /**
    * The execution order of the insertion when multiple insertions exist in a row.
    * A lower value executes earlier. Defaults to 0.
    *
    * @return the insertion order.
    */
   int order() default 0;

}
