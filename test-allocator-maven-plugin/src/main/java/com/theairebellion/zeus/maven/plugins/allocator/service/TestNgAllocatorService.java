package com.theairebellion.zeus.maven.plugins.allocator.service;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfigurationTestng;
import com.theairebellion.zeus.maven.plugins.allocator.discovery.TestClassLoader;
import org.apache.maven.plugin.logging.Log;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.internal.Parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class TestNgAllocatorService extends BaseAllocatorService {

    public TestNgAllocatorService(final Log log) {
        super(log);
    }


    @Override
    public Map<String, Integer> calculateClassMethodCounts(final List<File> classFiles,
                                                           final TestClassLoader testClassLoader,
                                                           final TestSplitterConfiguration config) {

        TestSplitterConfigurationTestng configTestNg = (TestSplitterConfigurationTestng) config;
        Set<String> suiteNames = configTestNg.getSuites();

        Map<String, Integer> classMethodCounts = new HashMap<>();

        List<File> suiteXmlFiles = findAllXmlFilesInProject(Paths.get(configTestNg.getProjectRoot()));

        for (File xmlFile : suiteXmlFiles) {
            try {
                List<XmlSuite> xmlSuites = new Parser(xmlFile.getAbsolutePath()).parseToList();
                for (XmlSuite xmlSuite : xmlSuites) {
                    if (suiteNames.contains(xmlSuite.getName())) {

                        for (XmlTest xmlTest : xmlSuite.getTests()) {
                            for (XmlClass xmlClass : xmlTest.getXmlClasses()) {
                                String className = xmlClass.getName();
                                Class<?> clazz = testClassLoader.loadClass(className);
                                if (clazz == null) {
                                    continue;
                                }

                                List<XmlInclude> includes = xmlClass.getIncludedMethods();
                                if (!includes.isEmpty()) {
                                    int matchedCount = 0;
                                    for (XmlInclude include : includes) {
                                        String methodName = include.getName();
                                        Method[] methods = clazz.getDeclaredMethods();
                                        for (Method m : methods) {
                                            if (m.getName().equals(methodName) && m.isAnnotationPresent(Test.class)) {
                                                matchedCount++;
                                            }
                                        }
                                    }
                                    classMethodCounts.merge(className, matchedCount, Integer::sum);
                                } else {
                                    int testCount = 0;
                                    if (!config.isParallelMethods()) {
                                        testCount = 1;
                                    } else {
                                        for (Method m : clazz.getDeclaredMethods()) {
                                            if (m.isAnnotationPresent(Test.class)) {
                                                testCount++;
                                            }
                                        }
                                    }

                                    classMethodCounts.merge(className, testCount, Integer::sum);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // Handle parse or IO exceptions as needed
                e.printStackTrace();
            }
        }

        return classMethodCounts;
    }


    /**
     * Example helper method for finding .xml files anywhere in your project.
     * Adjust to your real directory structure.
     */
    private List<File> findAllXmlFilesInProject(Path projectRoot) {
        List<File> result = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(projectRoot)) {
            pathStream
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".xml"))
                .forEach(p -> result.add(p.toFile()));
        } catch (IOException e) {
            // handle exception
        }
        return result;
    }

}
