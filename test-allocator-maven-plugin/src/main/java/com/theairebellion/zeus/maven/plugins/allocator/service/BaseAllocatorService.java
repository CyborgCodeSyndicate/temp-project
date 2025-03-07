package com.theairebellion.zeus.maven.plugins.allocator.service;

import com.google.gson.Gson;
import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import com.theairebellion.zeus.maven.plugins.allocator.discovery.ClassFileDiscovery;
import com.theairebellion.zeus.maven.plugins.allocator.discovery.TestClassLoader;
import com.theairebellion.zeus.maven.plugins.allocator.grouping.TestBucket;
import com.theairebellion.zeus.maven.plugins.allocator.grouping.TestBucketAllocator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseAllocatorService implements TestAllocatorService {

    private final Log log;


    public BaseAllocatorService(final Log log) {
        this.log = log;
    }


    @Override
    public void allocateTests(final TestSplitterConfiguration configuration) throws MojoExecutionException {
        TestClassLoader testClassLoader = new TestClassLoader(configuration);
        List<File> classFiles = ClassFileDiscovery.findClassFiles(configuration.getTestOutputDirectory());

        log.info("[TestSplitter] Found " + classFiles.size() + " class files.");

        Map<String, Integer> classMethodCounts = calculateClassMethodCounts(
            classFiles,
            testClassLoader,
            configuration
        );

        log.info("[TestSplitter] classMethodCount size=" + classMethodCounts.size());

        List<TestBucket> buckets =
            TestBucketAllocator.groupClasses(
                classMethodCounts,
                configuration.getMaxMethodsPerBucket()
            );

        writeGroupedTestsToFile(buckets, configuration.getJsonOutputFile());
    }


    public abstract Map<String, Integer> calculateClassMethodCounts(List<File> classFiles,
                                                                    TestClassLoader testClassLoader,
                                                                    TestSplitterConfiguration config);


    private void writeGroupedTestsToFile(List<TestBucket> buckets, String outPutFile) throws MojoExecutionException {
        List<Map<String, Object>> output = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            TestBucket b = buckets.get(i);
            Map<String, Object> jobObj = new HashMap<>();
            jobObj.put("jobIndex", i);
            jobObj.put("classes", b.getClassNames());
            jobObj.put("totalMethods", b.getTotalMethods());
            output.add(jobObj);
        }

        File outFile = new File(outPutFile + ".json");
        try (FileWriter fw = new FileWriter(outFile)) {
            new Gson().toJson(output, fw);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to write grouped-tests.json", e);
        }

        log.info("[TestSplitter] Wrote " + outFile.getAbsolutePath());
    }

}
