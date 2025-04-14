package com.theairebellion.zeus.ui.components.table.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field should be used as a filterable column or property
 * in a table. When this annotation is present, the table logic will apply filtering
 * logic based on the associated {@link #type()} and {@link #componentType()}.
 *
 * <p>Typical usage involves annotating a field within a row model class to specify
 * which UI component (e.g., a text input, dropdown) is responsible for filtering
 * that column. The table framework then retrieves this information at runtime
 * (via reflection) to execute the proper filtering actions.</p>
 *
 * <p>Where {@code InputComponentType} is an enum or class implementing
 * {@link ComponentType}, and {@code TEXT_INPUT} is one of its constants.</p>
 *
 * <p>The filtering mechanism is typically performed by a {@code TableFilter}
 * implementation, which uses the specified component type to determine how
 * to filter table rows based on user-provided input or predefined values.</p>
 *
 * <p>This annotation is processed at runtime, and the table logic uses it
 * alongside other annotations (e.g., {@link CellInsertion}) to build a complete
 * picture of how each field in a row model should be handled.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellFilter {

   /**
    * Specifies the UI component type (e.g., an enum or class implementing
    * {@link ComponentType}) that will handle filtering for this field.
    *
    * @return the class representing the component type.
    */
   Class<? extends ComponentType> type();

   /**
    * Defines the component type identifier (e.g., an enum constant or
    * string) to distinguish which specific filter mechanism to apply.
    *
    * @return a string matching a known component type identifier.
    */
   String componentType();
}
