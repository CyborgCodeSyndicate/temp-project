package com.theairebellion.zeus.maven.plugins.allocator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Set;

@Getter
public class TestSplitterConfigurationJunit extends TestSplitterConfiguration {

    private final Set<String> includeTags;
    private final Set<String> excludeTags;


    public TestSplitterConfigurationJunit(final boolean enabled, final int maxMethodsPerBucket,
                                          final File testOutputDirectory,
                                          final MavenProject mavenProject, final String jsonOutputFile,
                                          final String projectRoot,
                                          final boolean parallelMethods, final Set<String> includeTags,
                                          final Set<String> excludeTags) {
        super(enabled, maxMethodsPerBucket, testOutputDirectory, mavenProject, jsonOutputFile, projectRoot,
            parallelMethods);
        this.includeTags = includeTags;
        this.excludeTags = excludeTags;
    }

}
