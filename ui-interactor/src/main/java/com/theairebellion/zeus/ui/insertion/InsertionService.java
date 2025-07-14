package com.theairebellion.zeus.ui.insertion;

/**
 * Defines the contract for inserting data into UI components.
 *
 * <p>This interface is responsible for processing objects and mapping their values to UI components.
 * Implementing classes should extract field values and perform the necessary UI interactions.
 *
 * <p>Typically, this service is used in test automation to dynamically insert data into forms,
 * tables, or other interactive UI elements based on annotated fields in a data object.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface InsertionService {

   /**
    * Inserts data into UI components based on the provided object.
    *
    * <p>Implementations should:
    * <ul>
    *     <li>Extract field values from the given object.</li>
    *     <li>Map each field to a corresponding UI component.</li>
    *     <li>Use an appropriate insertion strategy to input the data.</li>
    * </ul>
    *
    * @param data The object containing values to be inserted into UI components.
    *             The fields should be properly annotated for mapping to UI elements.
    */
   void insertData(Object data);
}
