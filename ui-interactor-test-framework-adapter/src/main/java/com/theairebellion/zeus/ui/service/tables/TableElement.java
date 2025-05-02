package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.util.function.Consumer;

/**
 * Represents a table element that can be interacted with in the UI.
 *
 * <p>This interface provides methods for retrieving table metadata, such as
 * its type, row representation class, and lifecycle hooks (before and after actions).
 *
 * <p>Implementations of this interface are used to define specific tables
 * and their associated behaviors within the test automation framework.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface TableElement<K extends Enum<K>> {

   /**
    * Retrieves the table type for the implementing table.
    *
    * <p>By default, this method returns {@link DefaultTableTypes#DEFAULT}.
    * Implementations may override this method to provide a custom table type.
    *
    * @param <T> The type of {@link TableComponentType}.
    * @return The table type associated with this table element.
    */
   @SuppressWarnings("unchecked")
   default <T extends TableComponentType> T tableType() {
      return (T) DefaultTableTypes.DEFAULT;
   }

   /**
    * Retrieves the class representing the rows of the table.
    *
    * @param <T> The type of the row representation class.
    * @return The {@link Class} representing the table rows.
    */
   <T> Class<T> rowsRepresentationClass();

   /**
    * Retrieves the enumeration instance representing this table element.
    *
    * @return An {@link Enum} instance associated with this table element.
    */
   K enumImpl();

   /**
    * Provides a before-action hook that is executed before interacting with the table.
    *
    * <p>This method allows implementations to define preconditions,
    * such as waiting for the table to load.
    *
    * @return A {@link Consumer} accepting {@link SmartWebDriver} for executing preconditions.
    */
   default Consumer<SmartWebDriver> before() {
      return smartWebDriver -> {
      };
   }

   /**
    * Provides an after-action hook that is executed after interacting with the table.
    *
    * <p>This method allows implementations to define post-interaction actions,
    * such as logging interactions or refreshing the UI state.
    *
    * @return A {@link Consumer} accepting {@link SmartWebDriver} for executing postconditions.
    */
   default Consumer<SmartWebDriver> after() {
      return smartWebDriver -> {
      };
   }

}
