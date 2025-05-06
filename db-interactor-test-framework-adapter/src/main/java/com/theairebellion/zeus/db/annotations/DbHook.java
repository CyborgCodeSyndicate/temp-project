package com.theairebellion.zeus.db.annotations;

import com.theairebellion.zeus.framework.hooks.HookExecution;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a database hook to run before or after all tests in a class.
 *
 * <p>Annotate a test class with one or more {@code @DbHook} to execute
 * custom database logic (via {@link com.theairebellion.zeus.db.hooks.DbHookFlow})
 * at the specified lifecycle phase.
 * Hooks with the same {@linkplain #when() timing} are ordered by {@linkplain #order()}.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Repeatable(DbHooks.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbHook {

   /**
    * When to execute this database hook relative to the test lifecycle.
    *
    * @return {@link HookExecution#BEFORE} to run before all tests;
    *{@link HookExecution#AFTER} to run after all tests
    */
   HookExecution when();

   /**
    * The identifier of the hook implementation to invoke.
    *
    * <p>This value is passed into
    * {@link com.theairebellion.zeus.util.reflections.ReflectionUtil#findEnumImplementationsOfInterface}
    * to locate the {@link com.theairebellion.zeus.db.hooks.DbHookFlow} enum.
    *
    * @return the hook type name
    */
   String type();

   /**
    * Optional string arguments to pass to the hook logic.
    *
    * @return an array of argument strings
    */
   String[] arguments() default {};

   /**
    * Order of execution among hooks with the same {@linkplain #when timing}.
    * Lower values execute earlier.
    *
    * @return the execution order
    */
   int order() default 0;

}
