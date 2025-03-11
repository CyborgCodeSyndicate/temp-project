package com.theairebellion.zeus.maven.plugins.allocator.filtering;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

public final class TestMethodFilter {

    private TestMethodFilter() {
    }


    public static int countMatchingTestMethods(
        Class<?> clazz,
        Set<String> includeTags,
        Set<String> excludeTags,
        boolean parallelMethods
    ) {
        int count = 0;

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Test.class)) {
                continue;
            }

            Set<String> methodTags = TestTagExtractor.extractTags(method);

            if (isMethodIncluded(methodTags, includeTags, excludeTags)) {
                count++;
            }
        }

        return (!parallelMethods || isSequentialClassTheRingFramework(clazz)) && count > 0 ? 1 : count;
    }


    private static boolean isMethodIncluded(
        Set<String> methodTags,
        Set<String> includeTags,
        Set<String> excludeTags
    ) {
        return !hasExcludedTag(methodTags, excludeTags)
                   && isIncludedByTagRules(methodTags, includeTags);
    }


    private static boolean hasExcludedTag(Set<String> methodTags, Set<String> excludeTags) {
        return excludeTags.stream().anyMatch(methodTags::contains);
    }


    private static boolean isIncludedByTagRules(Set<String> methodTags, Set<String> includeTags) {
        return includeTags.isEmpty() ||
                   includeTags.stream().anyMatch(methodTags::contains);
    }


    private static boolean isSequentialClassTheRingFramework(Class<?> clazz) {
        return clazz.getSuperclass() != null &&
                   clazz.getSuperclass().getSimpleName().contains("BaseTestSequential");
    }

}