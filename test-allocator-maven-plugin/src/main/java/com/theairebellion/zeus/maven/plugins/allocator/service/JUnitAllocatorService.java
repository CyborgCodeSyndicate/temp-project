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

public class JUnitAllocatorService extends BaseAllocatorService {

    public JUnitAllocatorService(final Log log) {
        super(log);
    }


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
