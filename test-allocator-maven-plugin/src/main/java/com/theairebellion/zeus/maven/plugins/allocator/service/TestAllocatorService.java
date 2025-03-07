package com.theairebellion.zeus.maven.plugins.allocator.service;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

public interface TestAllocatorService {

    void allocateTests(TestSplitterConfiguration configuration) throws MojoExecutionException;


}
