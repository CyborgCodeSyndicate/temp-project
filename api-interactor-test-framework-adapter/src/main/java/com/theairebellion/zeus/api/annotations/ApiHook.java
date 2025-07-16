package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.framework.hooks.HookExecution;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a hook to be executed before or after all tests in a test class.
 *
 * <p>Each {@code @ApiHook} specifies a hook type (looked up via
 * {@link com.theairebellion.zeus.util.reflections.ReflectionUtil#findEnumImplementationsOfInterface}),
 * the timing of execution, optional arguments to pass to the hook, and an execution order.
 * Hooks of the same timing are sorted by {@linkplain #order()} before invocation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Repeatable(ApiHooks.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiHook {

   /**
    * The identifier of the hook flow implementation to invoke.
    *
    * <p>This will be passed as the {@code type} argument to
    * {@link com.theairebellion.zeus.util.reflections.ReflectionUtil#findEnumImplementationsOfInterface}.
    *
    * @return the hook flow type name
    */
   String type();

   /**
    * When to execute the hook relative to the test lifecycle.
    *
    * @return {
    *     @link HookExecution#BEFORE} to run before all tests,
    *     {@link HookExecution#AFTER} to run after all tests
    */
   HookExecution when();

   /**
    * Optional arguments to pass into the hook flow.
    *
    * @return an array of {@code String} arguments
    */
   String[] arguments() default {};

   /**
    * Execution order of this hook among others with the same {@linkplain #when timing}.
    *
    * <p>Hooks with lower order values run earlier.
    *
    * @return an integer defining the sort order
    */
   int order() default 0;

}
