package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.annotations.InfoAI;
import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Regression} annotation is used to mark test methods or test classes
 * as part of the regression test suite. This annotation is a custom extension of
 * JUnit's {@link Tag} annotation and automatically assigns the tag "Regression"
 * to any test it is applied to.
 * <p>
 * By using this annotation, test execution frameworks that support tagged execution,
 * such as JUnit and Maven Surefire Plugin, can selectively run regression tests.
 * </p>

 * <h3>Integration:</h3>
 * This annotation is used in conjunction with test execution tools to filter
 * and execute regression-specific tests. It simplifies test categorization
 * and improves maintainability in large test suites.

 * @author Cyborg Code Syndicate
 */
@InfoAI(description = "Annotation used to mark test classes or test methods as part of the regression test suite. " +
        "Helps categorize tests for selective execution.")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Tag("Regression")
public @interface Regression {
}
