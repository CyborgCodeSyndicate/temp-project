package com.theairebellion.zeus.maven.plugins.allocator.filtering;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class TestTagExtractor {

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


    private static void extractMetaTags(Class<?> annotationType, Set<String> methodTags) {
        for (Annotation metaAnnotation : annotationType.getAnnotations()) {
            if (metaAnnotation instanceof Tag) {
                methodTags.add(((Tag) metaAnnotation).value());
            }
        }
    }


}
