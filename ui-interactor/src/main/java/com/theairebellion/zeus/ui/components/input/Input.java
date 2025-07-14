package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Defines operations for interacting with input fields.
 *
 * <p>This interface provides methods to insert, clear, retrieve values,
 * check enabled states, and handle error messages for input fields.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface Input {

   /**
    * Inserts a value into the input field within the specified container.
    *
    * @param container The container holding the input field.
    * @param value     The value to insert.
    */
   void insert(SmartWebElement container, String value);

   /**
    * Inserts a value into the input field identified by its label inside a container.
    *
    * @param container       The container holding the input field.
    * @param inputFieldLabel The label of the input field.
    * @param value           The value to insert.
    */
   void insert(SmartWebElement container, String inputFieldLabel, String value);

   /**
    * Inserts a value into the input field identified by its label.
    *
    * @param inputFieldLabel The label of the input field.
    * @param value           The value to insert.
    */
   void insert(String inputFieldLabel, String value);

   /**
    * Inserts a value into the input field located by the specified locator.
    *
    * @param inputFieldContainerLocator The locator identifying the input field.
    * @param value                      The value to insert.
    */
   void insert(By inputFieldContainerLocator, String value);

   /**
    * Clears the input field within the specified container.
    *
    * @param container The container holding the input field.
    */
   void clear(SmartWebElement container);

   /**
    * Clears the input field identified by its label inside a container.
    *
    * @param container       The container holding the input field.
    * @param inputFieldLabel The label of the input field.
    */
   void clear(SmartWebElement container, String inputFieldLabel);

   /**
    * Clears the input field identified by its label.
    *
    * @param inputFieldLabel The label of the input field.
    */
   void clear(String inputFieldLabel);

   /**
    * Clears the input field located by the specified locator.
    *
    * @param inputFieldContainerLocator The locator identifying the input field.
    */
   void clear(By inputFieldContainerLocator);

   /**
    * Retrieves the current value of the input field within the specified container.
    *
    * @param container The container holding the input field.
    * @return The value of the input field as a string.
    */
   String getValue(SmartWebElement container);

   /**
    * Retrieves the current value of the input field identified by its label inside a container.
    *
    * @param container       The container holding the input field.
    * @param inputFieldLabel The label of the input field.
    * @return The value of the input field as a string.
    */
   String getValue(SmartWebElement container, String inputFieldLabel);

   /**
    * Retrieves the current value of the input field identified by its label.
    *
    * @param inputFieldLabel The label of the input field.
    * @return The value of the input field as a string.
    */
   String getValue(String inputFieldLabel);

   /**
    * Retrieves the current value of the input field located by the specified locator.
    *
    * @param inputFieldContainerLocator The locator identifying the input field.
    * @return The value of the input field as a string.
    */
   String getValue(By inputFieldContainerLocator);

   /**
    * Checks if the input field within the specified container is enabled.
    *
    * @param container The container holding the input field.
    * @return true if the input field is enabled, false otherwise.
    */
   boolean isEnabled(SmartWebElement container);

   /**
    * Checks if the input field identified by its label inside a container is enabled.
    *
    * @param container       The container holding the input field.
    * @param inputFieldLabel The label of the input field.
    * @return true if the input field is enabled, false otherwise.
    */
   boolean isEnabled(SmartWebElement container, String inputFieldLabel);

   /**
    * Checks if the input field identified by its label is enabled.
    *
    * @param inputFieldLabel The label of the input field.
    * @return true if the input field is enabled, false otherwise.
    */
   boolean isEnabled(String inputFieldLabel);

   /**
    * Checks if the input field located by the specified locator is enabled.
    *
    * @param inputFieldContainerLocator The locator identifying the input field.
    * @return true if the input field is enabled, false otherwise.
    */
   boolean isEnabled(By inputFieldContainerLocator);

   /**
    * Retrieves the error message displayed for the input field within the specified container.
    *
    * @param container The container holding the input field.
    * @return The error message as a string, or null if no error message is displayed.
    */
   String getErrorMessage(SmartWebElement container);

   /**
    * Retrieves the error message displayed for the input field identified by its label inside a container.
    *
    * @param container       The container holding the input field.
    * @param inputFieldLabel The label of the input field.
    * @return The error message as a string, or null if no error message is displayed.
    */
   String getErrorMessage(SmartWebElement container, String inputFieldLabel);

   /**
    * Retrieves the error message displayed for the input field identified by its label.
    *
    * @param inputFieldLabel The label of the input field.
    * @return The error message as a string, or null if no error message is displayed.
    */
   String getErrorMessage(String inputFieldLabel);

   /**
    * Retrieves the error message displayed for the input field located by the specified locator.
    *
    * @param inputFieldContainerLocator The locator identifying the input field.
    * @return The error message as a string, or null if no error message is displayed.
    */
   String getErrorMessage(By inputFieldContainerLocator);

   /**
    * Handles table insertion for an input field.
    *
    * @param cell   The table cell containing the input field.
    * @param values The values to be inserted.
    */
   default void tableInsertion(SmartWebElement cell, String... values) {
   }

   /**
    * Applies a filter to an input field in a table header.
    *
    * @param headerCell     The table header cell containing the input field.
    * @param filterStrategy The filter strategy to apply.
    * @param values         The values to be filtered.
    */
   default void tableFilter(SmartWebElement headerCell, FilterStrategy filterStrategy, String... values) {
   }

}
