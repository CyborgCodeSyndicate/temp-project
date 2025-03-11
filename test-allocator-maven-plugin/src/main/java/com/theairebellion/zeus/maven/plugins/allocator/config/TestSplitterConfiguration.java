package com.theairebellion.zeus.maven.plugins.allocator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.maven.project.MavenProject;

import java.io.File;

@AllArgsConstructor
@Getter
public class TestSplitterConfiguration {

    private final boolean enabled;
    private final int maxMethodsPerBucket;
    private final File testOutputDirectory;
    private final MavenProject mavenProject;
    private final String jsonOutputFile;
    private final String projectRoot;
    private final boolean parallelMethods;
    private int maxNumberOfParallelRunners;

}
