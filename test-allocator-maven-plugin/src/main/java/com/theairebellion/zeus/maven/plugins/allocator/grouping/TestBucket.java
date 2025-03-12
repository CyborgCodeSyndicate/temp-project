package com.theairebellion.zeus.maven.plugins.allocator.grouping;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Represents a bucket of test classes grouped together for parallel execution.
 * <p>
 * This class stores a list of test class names and the total number of test methods
 * contained within those classes. It is primarily used by the {@code TestBucketAllocator}
 * to efficiently distribute tests across parallel execution threads.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@AllArgsConstructor
@Getter
public class TestBucket {

    /** List of fully qualified class names contained in this test bucket. */
    private final List<String> classNames;

    /** Total number of test methods across all classes in this bucket. */
    private final int totalMethods;

}
