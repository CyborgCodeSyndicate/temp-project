package com.theairebellion.zeus.framework.adapters;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FrameworkAdapterContextCustomizerFactory implements ContextCustomizerFactory {


    @Override
    public ContextCustomizer createContextCustomizer(
        Class<?> testClass,
        List<ContextConfigurationAttributes> configAttributes) {

        // Collect *all* @FrameworkAdapter annotations (including meta-annotations)
        Set<FrameworkAdapter> annotations =
            AnnotatedElementUtils.findAllMergedAnnotations(testClass, FrameworkAdapter.class);

        if (annotations.isEmpty()) {
            // If no @FrameworkAdapter is found, return null so Spring knows we have no customization
            return null;
        }

        // Merge all basePackages into a set (removing duplicates)
        Set<String> mergedPackages = new LinkedHashSet<>();
        for (FrameworkAdapter adapter : annotations) {
            mergedPackages.addAll(Arrays.asList(adapter.basePackages()));
        }

        // Create a single ContextCustomizer that knows to scan all of the merged packages
        return new FrameworkAdapterContextCustomizer(mergedPackages.toArray(new String[0]));
    }

}
