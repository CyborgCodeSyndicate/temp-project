package com.theairebellion.zeus.validator.core;

/**
 * Represents a type of assertion used for validation.
 *
 * <p>Implementations of this interface define specific assertion types
 * that determine how validation is performed across different domains,
 * such as API responses, database queries, and UI components.
 *
 * <p>Each assertion type specifies:
 * <ul>
 *     <li>A unique identifier for the assertion type.</li>
 *     <li>The expected data type that the assertion operates on.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface AssertionType<T extends Enum<T>> {

   /**
    * Retrieves the unique identifier of the assertion type.
    *
    * @return The enum representing the assertion type.
    */
   T type();

   /**
    * Retrieves the supported data type for this assertion.
    *
    * <p>Assertions are designed to validate specific data types.
    * This method ensures compatibility by defining which data type
    * the assertion operates on.
    *
    * @return The class representing the supported data type.
    */
   Class<?> getSupportedType();

}
