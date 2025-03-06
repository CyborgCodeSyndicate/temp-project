package com.theairebellion.zeus.framework.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;
import java.util.Objects;

/**
 * A custom Spring {@link ContextCustomizer} that scans additional base packages for Spring components.
 * <p>
 * This class is used to dynamically include extra packages during the Spring context initialization,
 * ensuring that relevant beans and configurations are detected. It is particularly useful when
 * extending the test context beyond the default package scanning.
 * </p>
 *
 * <p>
 * The customizer is applied by scanning the specified {@code basePackages} in an {@link AnnotationConfigApplicationContext},
 * enabling the framework to discover and register additional beans for test execution.
 * </p>
 *
 * <p>
 * Instances of this class compare equality based on the {@code basePackages} array, ensuring that
 * duplicate customizers are not applied unnecessarily.
 * </p>
 *
 * <p>
 * Author: Cyborg Code Syndicate
 * </p>
 */
public class FrameworkAdapterContextCustomizer implements ContextCustomizer {

    private final String[] basePackages;

    /**
     * Creates a new {@code FrameworkAdapterContextCustomizer} with the specified base packages.
     *
     * @param basePackages The packages to scan for Spring components.
     *                     Must not be {@code null}.
     */
    public FrameworkAdapterContextCustomizer(String[] basePackages) {
        this.basePackages = Objects.requireNonNull(basePackages, "Base packages must not be null");
    }

    /**
     * Customizes the given Spring application context by scanning the specified base packages.
     *
     * @param context      The configurable application context.
     * @param mergedConfig The merged context configuration.
     */
    @Override
    public void customizeContext(@NonNull ConfigurableApplicationContext context,
                                 @NonNull MergedContextConfiguration mergedConfig) {
        if (context instanceof final AnnotationConfigApplicationContext annotationContext) {
            annotationContext.scan(basePackages);
        }
    }

    /**
     * Compares this customizer to another object, returning {@code true} if they share the same base packages.
     *
     * @param obj The object to compare.
     * @return {@code true} if both customizers have the same {@code basePackages}, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof final FrameworkAdapterContextCustomizer other)) {
            return false;
        }
        return Arrays.equals(basePackages, other.basePackages);
    }

    /**
     * Returns the hash code of this customizer, based on the {@code basePackages}.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(basePackages);
    }

    /**
     * Returns a string representation of this customizer, including the {@code basePackages}.
     *
     * @return A string describing the customizer.
     */
    @Override
    public String toString() {
        return "FrameworkAdapterContextCustomizer{" +
                "basePackages=" + Arrays.toString(basePackages) +
                '}';
    }

}
