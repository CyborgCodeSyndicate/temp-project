package com.theairebellion.zeus.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;

/**
 * Marks a test or test class as part of the Smoke Test Suite.
 *
 * <p>This annotation is used to categorize test methods or entire test classes
 * as "Smoke" tests. Smoke tests are typically a subset of test cases
 * that verify the most critical functionalities of an application.
 *
 * <p>The annotation is retained at runtime and applies to both class and method levels.
 * It also integrates with JUnit 5 by applying the {@code @Tag("Smoke")} annotation,
 * allowing test filtering via tags.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Tag("Smoke")
public @interface Smoke {

}
