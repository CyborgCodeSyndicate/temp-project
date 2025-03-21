package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.selenium.UIElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define UI elements used for data insertion in automated tests.
 * <p>
 * This annotation is applied to fields in page objects or test components to specify
 * UI elements that should be used for inserting data. It provides details such as the locator class,
 * the specific element within that class, and the order in which elements should be processed.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InsertionElement {

    /**
     * Specifies the class that defines the UI element's locator.
     *
     * @return The enum class representing UI element locators.
     */
    Class<? extends UIElement> locatorClass();

    /**
     * Specifies the name of the UI element within the locator class.
     *
     * @return The string representing the enum constant of the UI element.
     */
    String elementEnum();

    /**
     * Defines the order in which elements should be processed during data insertion.
     * <p>
     * Lower values are processed first, allowing sequential interactions with form fields.
     * </p>
     *
     * @return The order of the element in the insertion process.
     */
    int order();
}
