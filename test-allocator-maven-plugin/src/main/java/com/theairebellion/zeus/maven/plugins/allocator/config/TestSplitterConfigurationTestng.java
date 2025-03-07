package com.theairebellion.zeus.maven.plugins.allocator.config;

import lombok.Getter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Set;

@Getter
public class TestSplitterConfigurationTestng extends TestSplitterConfiguration {

    private final Set<String> suites;


    public TestSplitterConfigurationTestng(final boolean enabled, final int maxMethodsPerBucket,
                                           final File testOutputDirectory,
                                           final MavenProject mavenProject, final String jsonOutputFile,
                                           final String projectRoot,
                                           final boolean parallelMethods, final int maxNumberOfParallelRunners,
                                           final Set<String> suites) {
        super(enabled, maxMethodsPerBucket, testOutputDirectory, mavenProject, jsonOutputFile, projectRoot,
            parallelMethods,
            maxNumberOfParallelRunners);
        this.suites = suites;
    }

}
