package com.theairebellion.zeus.maven.plugins.allocator.grouping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TestBucketAllocator {

    private TestBucketAllocator() {
    }


    public static List<TestBucket> groupClasses(
        Map<String, Integer> classMethodCounts,
        int maxMethodsPerBucket
    ) {
        List<Map.Entry<String, Integer>> sortedClasses = sortClassesByMethodCount(classMethodCounts);

        List<TestBucket> buckets = new ArrayList<>();
        List<String> currentBucketClasses = new ArrayList<>();
        int currentBucketMethodCount = 0;

        for (Map.Entry<String, Integer> entry : sortedClasses) {
            int methods = entry.getValue();
            String className = entry.getKey();

            if (methods > maxMethodsPerBucket) {
                buckets.add(createSingleClassBucket(className, methods));
                continue;
            }

            if (currentBucketMethodCount + methods <= maxMethodsPerBucket) {
                currentBucketClasses.add(className);
                currentBucketMethodCount += methods;
            } else {
                buckets.add(createBucket(currentBucketClasses, currentBucketMethodCount));

                currentBucketClasses = new ArrayList<>(List.of(className));
                currentBucketMethodCount = methods;
            }
        }

        if (!currentBucketClasses.isEmpty()) {
            buckets.add(createBucket(currentBucketClasses, currentBucketMethodCount));
        }

        return buckets;
    }


    private static List<Map.Entry<String, Integer>> sortClassesByMethodCount(
        Map<String, Integer> classMethodCounts
    ) {
        return classMethodCounts.entrySet().stream()
                   .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                   .collect(Collectors.toList());
    }


    private static TestBucket createSingleClassBucket(String className, int methods) {
        return new TestBucket(List.of(className), methods);
    }


    private static TestBucket createBucket(List<String> classes, int totalMethods) {
        return new TestBucket(classes, totalMethods);
    }

}
