package com.theairebellion.zeus.maven.plugins.allocator.filtering;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for extracting tags from test methods in JUnit 5.
 * <p>
 * This class retrieves tags applied directly to test methods using {@link Tag} or {@link Tags} annotations.
 * It also supports extracting meta-annotations (annotations that themselves contain {@link Tag}).
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class TestTagExtractor {

    /**
     * Extracts all tags associated with a given test method.
     * <p>
     * The method inspects annotations directly applied to the method, including:
     * </p>
     * <ul>
     *     <li>{@link Tag} - Extracts a single tag value.</li>
     *     <li>{@link Tags} - Extracts multiple tag values.</li>
     *     <li>Meta-annotations - Extracts tags from annotations that contain {@link Tag} annotations.</li>
     * </ul>
     *
     * @param method The test method to analyze.
     * @return A set of extracted tag values.
     */
    public static Set<String> extractTags(Method method) {
        Set<String> methodTags = new HashSet<>();

        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof Tag) {
                methodTags.add(((Tag) annotation).value());
            } else if (annotation instanceof Tags) {
                for (Tag tag : ((Tags) annotation).value()) {
                    methodTags.add(tag.value());
                }
            } else {
                extractMetaTags(annotation.annotationType(), methodTags);
            }
        }

        return methodTags;
    }

    /**
     * Extracts tags from meta-annotations (annotations that themselves contain {@link Tag}).
     *
     * @param annotationType The class of the annotation being checked.
     * @param methodTags     The set of extracted tags.
     */
    private static void extractMetaTags(Class<?> annotationType, Set<String> methodTags) {
        for (Annotation metaAnnotation : annotationType.getAnnotations()) {
            if (metaAnnotation instanceof Tag) {
                methodTags.add(((Tag) metaAnnotation).value());
            }
        }
    }

}
