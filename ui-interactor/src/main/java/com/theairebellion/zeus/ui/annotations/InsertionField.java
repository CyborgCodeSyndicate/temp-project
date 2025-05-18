package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.openqa.selenium.support.FindBy;

/**
 * Marks a UI field for dynamic component insertion.
 *
 * <p>This annotation is used to define UI input fields that require automated handling,
 * such as text fields, dropdowns, and checkboxes, by specifying their locator,
 * type, and order of interaction.
 *
 * <p>It is commonly applied to DTO-like classes representing UI forms, where it
 * helps automate interactions with form elements.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InsertionField {

   /**
    * Specifies the locator strategy for identifying the UI field.
    *
    * @return The Selenium {@link FindBy} locator used to locate the field.
    */
   FindBy locator();

   /**
    * Defines the component type for this field.
    *
    * <p>This should be class implementing {@link ComponentType},
    * allowing the framework to determine how to interact with the UI element.
    *
    * @return The component type class.
    */
   Class<? extends ComponentType> type();

   /**
    * Specifies the string identifier of the component type.
    *
    * <p>This identifier is usually an enum value representing the
    * UI framework component (e.g., "MD_INPUT_TYPE" for Material Design inputs).
    *
    * @return The string identifier for the component type.
    */
   String componentType();

   /**
    * Defines the order in which this field should be processed.
    *
    * <p>Lower values indicate higher priority in execution order.
    *
    * @return The order value for processing this field.
    */
   int order();

}
