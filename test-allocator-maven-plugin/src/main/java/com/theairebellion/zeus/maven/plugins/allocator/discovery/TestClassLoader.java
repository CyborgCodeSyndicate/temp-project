package com.theairebellion.zeus.maven.plugins.allocator.discovery;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TestClassLoader {

    private final URLClassLoader classLoader;


    public TestClassLoader(TestSplitterConfiguration config) {
        this.classLoader = createClassLoader(config);
    }


    private URLClassLoader createClassLoader(TestSplitterConfiguration config) {
        try {
            Set<URL> urls = new LinkedHashSet<>();

            List<String> testElements = config.getMavenProject().getTestClasspathElements();
            List<String> compileElements = config.getMavenProject().getCompileClasspathElements();

            addUrlsFromPaths(testElements, urls);
            addUrlsFromPaths(compileElements, urls);

            return new URLClassLoader(
                urls.toArray(new URL[0]),
                getClass().getClassLoader()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create class loader", e);
        }
    }


    private void addUrlsFromPaths(List<String> paths, Set<URL> urls) throws MalformedURLException {
        for (String path : paths) {
            urls.add(new File(path).toURI().toURL());
        }
    }


    public Class<?> loadClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return null;
        }
    }

}
