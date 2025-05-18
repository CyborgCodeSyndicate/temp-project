package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test method or class for post-execution cleanup.
 *
 * <p>This annotation is used to specify cleanup actions that should be executed after a test completes.
 * The framework processes this annotation through the {@code RipperMan} extension,
 * ensuring that necessary tear down operations, such as deleting test data or closing resources, are performed.
 *
 * <p>The specified targets correspond to predefined cleanup actions,
 * which are resolved dynamically and executed after the test method finishes.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Ripper {

   /**
    * Defines the cleanup actions to be performed after test execution.
    *
    * <p>Each target represents a predefined cleanup operation that is dynamically resolved
    * and executed once the test method has completed.
    *
    * @return An array of target identifiers specifying cleanup actions.
    */
   String[] targets();

}
