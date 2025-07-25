package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting test data into test method parameters.
 *
 * <p>This annotation is used in test methods to automatically generate
 * and provide test data objects based on predefined models.
 * The annotated parameter is resolved dynamically by the test framework,
 * which retrieves the corresponding data creator from an appropriate implementation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Craft {

   /**
    * Specifies the test data model to be injected.
    *
    * <p>The value should match a valid test data model identifier,
    * which the framework will use to generate the corresponding test object.
    *
    * @return The name of the test data model.
    */
   String model();

}
