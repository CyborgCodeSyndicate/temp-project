package com.theairebellion.zeus.maven.plugins.allocator;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfigurationJunit;
import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfigurationTestng;
import com.theairebellion.zeus.maven.plugins.allocator.service.JUnitAllocatorService;
import com.theairebellion.zeus.maven.plugins.allocator.service.TestAllocatorService;
import com.theairebellion.zeus.maven.plugins.allocator.service.TestNgAllocatorService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.testng.internal.collections.Pair;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Mojo(
    name = "split",
    defaultPhase = LifecyclePhase.TEST_COMPILE,
    requiresDependencyResolution = ResolutionScope.TEST
)
public class TestAllocatorMojo extends AbstractMojo {

    @Parameter(property = "testSplitter.enabled", defaultValue = "false")
    private boolean enabled;

    @Parameter(property = "testSplitter.test.engine", required = true, defaultValue = "junit")
    private String testEngine;

    @Parameter(property = "testSplitter.junit.tags.include")
    private String tagsInclude;

    @Parameter(property = "testSplitter.junit.tags.exclude")
    private String tagsExclude;

    @Parameter(property = "testSplitter.testng.suites")
    private String suites;

    @Parameter(property = "testSplitter.maxMethods", defaultValue = "20")
    private int maxMethods;

    @Parameter(defaultValue = "${project.build.testOutputDirectory}", readonly = true)
    private File testOutputDir;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "testSplitter.json.output", defaultValue = "grouped-tests")
    private String outputJsonFile;

    @Parameter(defaultValue = "${project.basedir}", readonly = true, required = true)
    private File projectBaseDir;

    @Parameter(property = "testSplitter.parallel.methods", defaultValue = "true", required = true)
    private Boolean parallelMethods;

    @Parameter(property = "testSplitter.max.number.runners", defaultValue = "20", required = true)
    private int maxNumberOfParallelRunners;



    @Override
    public void execute() throws MojoExecutionException {
        if (!enabled) {
            getLog().info("[TestSplitter] Disabled. Skipping.");
            return;
        }

        Pair<TestSplitterConfiguration, TestAllocatorService> pair = createConfigurationAndService();
        TestSplitterConfiguration config = pair.getLeft();
        TestAllocatorService testAllocatorService = pair.getRight();

        logConfiguration(config);
        testAllocatorService.allocateTests(config);
    }


    private Pair<TestSplitterConfiguration, TestAllocatorService> createConfigurationAndService() {
        TestSplitterConfiguration config;
        TestAllocatorService service;
        switch (testEngine.toLowerCase()) {
            case "junit":
                config = new TestSplitterConfigurationJunit(
                    enabled, maxMethods, testOutputDir, project, outputJsonFile,
                    projectBaseDir.getAbsolutePath(), parallelMethods, parseInput(tagsInclude), parseInput(tagsExclude)
                );
                service = new JUnitAllocatorService(getLog());
                break;
            case "testng":
                config = new TestSplitterConfigurationTestng(
                    enabled, maxMethods, testOutputDir, project, outputJsonFile,
                    projectBaseDir.getAbsolutePath(), this.parallelMethods, parseInput(suites)
                );
                service = new TestNgAllocatorService(getLog());
                break;
            default:
                throw new IllegalArgumentException(
                    "Invalid test engine: " + testEngine + ". Supported: junit, testng."
                );
        }

        return new Pair<>(config, service);
    }


    private void logConfiguration(TestSplitterConfiguration config) {
        getLog().info("[TestSplitter] Starting test splitting...");
        getLog().info(String.format("[TestSplitter] testOutputDir = %s", config.getTestOutputDirectory()));
        getLog().info(String.format("[TestSplitter] testEngine = %s", testEngine));

        if (config instanceof TestSplitterConfigurationJunit junitConfig) {
            getLog().info(String.format("[TestSplitter] tagsInclude = %s", junitConfig.getIncludeTags()));
            getLog().info(String.format("[TestSplitter] tagsExclude = %s", junitConfig.getExcludeTags()));
        } else if (config instanceof TestSplitterConfigurationTestng testngConfig) {
            getLog().info(String.format("[TestSplitter] suites = %s", testngConfig.getSuites()));
        }

        getLog().info(String.format("[TestSplitter] maxMethods = %d", config.getMaxMethodsPerBucket()));
        getLog().info(String.format("[TestSplitter] outputJsonFile = %s", config.getJsonOutputFile()));
    }


    private static Set<String> parseInput(String input) {
        return Optional.ofNullable(input)
                   .map(str -> str.split(","))
                   .map(arr -> Arrays.stream(arr)
                                   .map(String::trim)
                                   .filter(s -> !s.isEmpty())
                                   .collect(Collectors.toSet()))
                   .orElse(new HashSet<>());
    }


    private static class Pair<L, R> {

        private final L left;
        private final R right;


        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }


        public L getLeft() {
            return left;
        }


        public R getRight() {
            return right;
        }

    }

}