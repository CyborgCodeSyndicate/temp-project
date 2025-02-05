package com.theairebellion.zeus.framework.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;
import java.util.Objects;

public class FrameworkAdapterContextCustomizer implements ContextCustomizer {


    private final String[] basePackages;


    public FrameworkAdapterContextCustomizer(String[] basePackages) {
        this.basePackages = Objects.requireNonNull(basePackages, "Base packages must not be null");
    }


    @Override
    public void customizeContext(@NonNull ConfigurableApplicationContext context,
                                 @NonNull MergedContextConfiguration mergedConfig) {
        if (context instanceof final AnnotationConfigApplicationContext annotationContext) {
            annotationContext.scan(basePackages);
        }
    }


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


    @Override
    public int hashCode() {
        return Arrays.hashCode(basePackages);
    }


    @Override
    public String toString() {
        return "FrameworkAdapterContextCustomizer{" +
                   "basePackages=" + Arrays.toString(basePackages) +
                   '}';
    }

}
