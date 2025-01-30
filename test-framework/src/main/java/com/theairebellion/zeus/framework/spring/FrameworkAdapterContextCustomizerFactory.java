package com.theairebellion.zeus.framework.spring;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FrameworkAdapterContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(
        @NonNull Class<?> testClass,
        @Nullable List<ContextConfigurationAttributes> configAttributes) {

        Set<FrameworkAdapter> annotations = AnnotatedElementUtils.findAllMergedAnnotations(testClass,
            FrameworkAdapter.class);

        if (annotations.isEmpty()) {
            return null;
        }

        return new FrameworkAdapterContextCustomizer(annotations.stream()
                                                         .flatMap(adapter -> Arrays.stream(adapter.basePackages()))
                                                         .distinct().toArray(String[]::new));
    }

}

