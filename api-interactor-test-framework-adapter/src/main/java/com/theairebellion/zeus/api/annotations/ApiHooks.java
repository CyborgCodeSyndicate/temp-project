package com.theairebellion.zeus.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation that allows multiple {@link ApiHook} annotations
 * to be applied to a test class.
 *
 * <p>This annotation is used internally by Java’s repeatable annotation mechanism;
 * you normally use {@code @ApiHook} directly.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiHooks {

   /**
    * The wrapped array of {@link ApiHook} annotations.
    *
    * @return one or more ApiHook annotations
    */
   ApiHook[] value() default {};

}
