package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining a framework adapter that configures base packages for test execution.
 *
 * <p>This annotation is used to specify the base packages that should be included
 * when setting up a test context. It is typically applied to custom meta-annotations
 * that define test execution behavior by integrating different testing extensions.
 *
 * <p>The framework automatically detects and processes classes annotated with {@code @FrameworkAdapter}
 * using reflection, ensuring that the appropriate test setup is applied.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FrameworkAdapter {

   /**
    * Specifies the base packages to be included in the test context.
    *
    * <p>These packages define the scope of the test adapter and ensure
    * that the necessary components are scanned and configured for execution.
    *
    * @return An array of base package names for the test adapter.
    */
   String[] basePackages();

}
