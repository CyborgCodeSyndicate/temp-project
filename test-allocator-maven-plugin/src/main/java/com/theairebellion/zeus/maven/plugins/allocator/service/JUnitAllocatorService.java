package com.theairebellion.zeus.maven.plugins.allocator.service;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfigurationJunit;
import com.theairebellion.zeus.maven.plugins.allocator.discovery.ClassFileDiscovery;
import com.theairebellion.zeus.maven.plugins.allocator.discovery.TestClassLoader;
import com.theairebellion.zeus.maven.plugins.allocator.filtering.TestMethodFilter;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Allocates JUnit test classes into execution groups based on method count and filtering rules.
 * <p>
 * This service extends {@link BaseAllocatorService} and applies JUnit-specific filtering rules,
 * such as including/excluding test methods based on tags and handling parallel execution settings.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class JUnitAllocatorService extends BaseAllocatorService {

    /**
     * Constructs a new {@code JUnitAllocatorService} instance.
     *
     * @param log The Maven logger instance for recording allocation details.
     */
    public JUnitAllocatorService(final Log log) {
        super(log);
    }

    /**
     * Calculates the number of matching test methods per JUnit test class.
     * <p>
     * This method:
     * <ul>
     *   <li>Retrieves the test classes from the provided list of class files.</li>
     *   <li>Loads each test class using the {@link TestClassLoader}.</li>
     *   <li>Filters test methods based on include/exclude tags.</li>
     *   <li>Determines if the test class should be treated as parallel or sequential.</li>
     *   <li>Returns a map of class names and their respective test method counts.</li>
     * </ul>
     * </p>
     *
     * @param classFiles      List of test class files.
     * @param testClassLoader The test class loader used to dynamically load test classes.
     * @param config          The JUnit-specific test allocation configuration.
     * @return A mapping of test class names to their number of executable test methods.
     */
    @Override
    public Map<String, Integer> calculateClassMethodCounts(
            List<File> classFiles,
            TestClassLoader testClassLoader,
            TestSplitterConfiguration config
    ) {
        TestSplitterConfigurationJunit configJunit = (TestSplitterConfigurationJunit) config;

        return classFiles.stream()
                .map(cf -> ClassFileDiscovery.fileToClassName(cf, configJunit.getTestOutputDirectory()))
                .map(className -> {
                    Class<?> clazz = testClassLoader.loadClass(className);
                    if (clazz == null) {
                        return null;
                    }

                    int matchingCount = TestMethodFilter.countMatchingTestMethods(
                            clazz,
                            configJunit.getIncludeTags(),
                            configJunit.getExcludeTags(),
                            configJunit.isParallelMethods()
                    );

                    return matchingCount > 0 ? Map.entry(className, matchingCount) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
