package com.theairebellion.zeus.ui.components.table.base;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;

/**
 * Represents a field in a table row that can be dynamically accessed and modified.
 * This functional interface allows for invoking a setter method on a given instance
 * to set a value for a specific table column.
 *
 * <p>Primarily used for reading, writing, and updating table data within
 * the {@code Table} and {@code TableService} implementations.
 *
 * <p>The row model type is represented by {@code T}, which defines the structure of a table row.
 * The type {@code P} represents the field type being set within the table row model.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@FunctionalInterface
public interface TableField<T> {

   /**
    * Invokes the setter method for the corresponding field in the provided instance.
    *
    * @param instance The object instance on which to set the field value.
    * @param o        The value to be set.
    * @throws IllegalAccessException    If the method is not accessible.
    * @throws InvocationTargetException If the method invocation fails.
    */
   void invoke(T instance, Object o) throws IllegalAccessException, InvocationTargetException;

   /**
    * Creates a {@code TableField} from a given {@link BiConsumer}, which represents
    * a setter method for a specific field.
    *
    * @param consumer The setter method reference.
    * @param <T>      The type of the row model.
    * @param <P>      The type of the field being set.
    * @return A {@code TableField} instance.
    */
   static <T, P> TableField<T> of(BiConsumer<T, P> consumer) {
      return (t, obj) -> consumer.accept(t, (P) obj);
   }

}
