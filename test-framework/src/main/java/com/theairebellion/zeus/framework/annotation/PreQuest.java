package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a set of pre-test setup steps ("journeys") to be executed before the test runs.
 *
 * <p>This annotation is applied to test methods to specify multiple {@code @Journey} annotations,
 * each representing a distinct precondition that must be met before the test execution.
 * The framework processes this annotation dynamically to ensure the required setup steps
 * are executed in the defined order.
 *
 * <p>The execution order of journeys is determined by the {@code order()} attribute within each {@code @Journey},
 * allowing for precise control over the test setup sequence.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreQuest {

   /**
    * Specifies the set of journeys to be executed before the test.
    *
    * <p>Each journey defines a precondition, including associated test data,
    * that must be fulfilled before the test method runs.
    *
    * @return An array of {@code Journey} annotations representing the pre-test setup steps.
    */
   Journey[] value();

}
