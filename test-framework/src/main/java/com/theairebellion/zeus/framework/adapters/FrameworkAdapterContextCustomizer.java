package com.theairebellion.zeus.framework.adapters;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import java.util.Arrays;

public class FrameworkAdapterContextCustomizer implements ContextCustomizer {

    private final String[] basePackages;

    public FrameworkAdapterContextCustomizer(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void customizeContext(
        ConfigurableApplicationContext context,
        MergedContextConfiguration mergedConfig) {

        if (context instanceof AnnotationConfigApplicationContext) {
            AnnotationConfigApplicationContext annCtx = (AnnotationConfigApplicationContext) context;
            annCtx.scan(basePackages);
            // Do NOT call annCtx.refresh(); Spring Test will do the refresh at the right time
        }
    }

    // Implement equals() & hashCode() if you want Spring to cache based on these packages
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrameworkAdapterContextCustomizer)) return false;
        FrameworkAdapterContextCustomizer other = (FrameworkAdapterContextCustomizer) o;
        return Arrays.equals(this.basePackages, other.basePackages);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(basePackages);
    }
}
