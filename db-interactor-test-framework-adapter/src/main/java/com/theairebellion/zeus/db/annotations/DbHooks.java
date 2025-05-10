package com.theairebellion.zeus.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation that allows multiple {@link DbHook} annotations
 * to be applied to a test class
 *
 * <p>This annotation is used internally by Java’s repeatable annotation mechanism;
 * you normally use {@code @DbHook} directly
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbHooks {

   /**
    * The wrapped array of {@link DbHook} annotations
    *
    * <p>Each element represents a single {@code @DbHook} on the test class
    *
    * @return one or more DbHook annotations
    */
   DbHook[] value() default {};

}
