package com.theairebellion.zeus.maven.plugins.allocator;

import com.google.gson.Gson;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


@Mojo(name = "split", defaultPhase = LifecyclePhase.TEST_COMPILE, requiresDependencyResolution = ResolutionScope.TEST
)
public class TestAllocatorMojo extends AbstractMojo {

    @Parameter(property = "testSplitter.enabled", defaultValue = "false")
    private boolean enabled;

    /**
     * Comma-separated list of tags to include.
     * e.g. -DtestSplitter.tags.include=Regression,Smoke
     */
    @Parameter(property = "testSplitter.tags.include")
    private String tagsInclude;

    /**
     * Comma-separated list of tags to exclude.
     * e.g. -DtestSplitter.tags.exclude=Flaky,Ignore
     */
    @Parameter(property = "testSplitter.tags.exclude")
    private String tagsExclude;

    /**
     * Maximum test methods per bucket/job.
     * e.g. -DtestSplitter.maxMethods=20
     */
    @Parameter(property = "testSplitter.maxMethods", defaultValue = "20")
    private int maxMethods;

    /**
     * The directory containing compiled test classes. Usually target/test-classes.
     */
    @Parameter(defaultValue = "${project.build.testOutputDirectory}", readonly = true)
    private File testOutputDir;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "path.to.file.classloaders")
    private String pathToFileWithClassLoaders;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!enabled) {
            getLog().info("[TestSplitter] Disabled. Skipping.");
            return;
        }

        getLog().info("[TestSplitter] Starting test splitting...");
        getLog().info("[TestSplitter] testOutputDir = " + testOutputDir);
        getLog().info("[TestSplitter] tagsInclude = " + tagsInclude);
        getLog().info("[TestSplitter] tagsExclude = " + tagsExclude);
        getLog().info("[TestSplitter] maxMethods = " + maxMethods);
        getLog().info("testOutputDir absolute path = " + testOutputDir.getAbsolutePath());

        // Parse the include/exclude lists
        Set<String> includeTags = parseCommaSeparated(tagsInclude);
        Set<String> excludeTags = parseCommaSeparated(tagsExclude);

        Set<Artifact> artifacts = project.getArtifacts();
        getLog().warn("Viksa Artifacts: " + artifacts);
        getLog().warn("Viksa Project: " + project);
        getLog().warn("Viksa Project Name: " + project.getName());

        List<String> testElements = null;
        try {
            testElements = project.getTestClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
        List<String> compileElements = null;
        try {
            compileElements = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }

        // Combine them:
        Set<URL> urls = new LinkedHashSet<>();
        for (String path : testElements) {
            try {
                urls.add(new File(path).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        for (String path : compileElements) {
            try {
                urls.add(new File(path).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }


        URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());

        // URLClassLoader cl = createClassLoader();

        if (!testOutputDir.exists()) {
            getLog().warn("[TestSplitter] testOutputDir does not exist: " + testOutputDir);
            return;
        }

        // 1) Find all .class files in target/test-classes
        List<File> classFiles = findClassFiles(testOutputDir);
        getLog().info("[TestSplitter] Found " + classFiles.size() + " class files.");

        // 2) For each class, reflect over methods, count how many match the tags
        Map<String, Integer> classMethodCount = new HashMap<>();
        for (File cf : classFiles) {
            String className = fileToClassName(cf, testOutputDir);
            Class<?> clazz;
            try {
                clazz = cl.loadClass(className);
                getLog().info("Viksa: " + className);
            } catch (NoClassDefFoundError | ClassNotFoundException e) {
                e.printStackTrace();
                // skip
                continue;
            }

            int matchingCount = countMatchingTestMethods(clazz, includeTags, excludeTags);

            if (matchingCount > 0) {
                classMethodCount.put(className, matchingCount);
            }
        }


        getLog().info("[TestSplitter] classMethodCount size=" + classMethodCount.size());

        // 3) Group classes into buckets of up to 'maxMethods'
        List<Bucket> buckets = groupClasses(classMethodCount, maxMethods);

        // 4) Generate final JSON: an array of objects, each with jobIndex, classes, totalMethods
        List<Map<String, Object>> output = new ArrayList<>();
        for (int i = 0; i < buckets.size(); i++) {
            Bucket b = buckets.get(i);
            Map<String, Object> jobObj = new HashMap<>();
            jobObj.put("jobIndex", i);
            jobObj.put("classes", b.classNames);
            jobObj.put("totalMethods", b.totalMethods);
            output.add(jobObj);
        }

        // 5) Write grouped-tests.json to current working directory
        File outFile = new File("grouped-tests.json");
        try (FileWriter fw = new FileWriter(outFile)) {
            new Gson().toJson(output, fw);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to write grouped-tests.json", e);
        }

        getLog().info("[TestSplitter] Wrote " + outFile.getAbsolutePath());
    }


    private URLClassLoader createClassLoader() throws MojoExecutionException {
        Set<URL> urls = new LinkedHashSet<>();

        // 1) All test/compile classpath elements
        urls.addAll(getProjectClassLoaders());

        // 2) All dependencies from the project's POM
        urls.addAll(getDependenciesClassLoaders());
        urls.addAll(getDependenciesClassLoadersHardCoded());

        // 3) Possibly an external file listing jar/class directories
        urls.addAll(getAdditionalClassLoaders());

        getLog().info("Constructing custom class loader with " + urls.size() + " entries.");
        return new URLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }


    // -------------------------------------------------------------------------------------
    // (A) Gather test + compile classpath elements via MavenProject
    // -------------------------------------------------------------------------------------
    private List<URL> getProjectClassLoaders() throws MojoExecutionException {
        try {
            // test + compile classpath
            List<String> testClasspathElements = project.getTestClasspathElements();
            List<String> compileClasspathElements = project.getCompileClasspathElements();

            List<String> allElements = new ArrayList<>();
            allElements.addAll(testClasspathElements);
            allElements.addAll(compileClasspathElements);

            // Convert each path to URL
            return allElements.stream()
                       .map(File::new)
                       .filter(File::exists)
                       .map(this::toURL)
                       .collect(Collectors.toList());
        } catch (org.apache.maven.artifact.DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error resolving project classpath elements", e);
        }
    }


    // -------------------------------------------------------------------------------------
    // (B) Gather direct dependencies from <dependencies> in this module's POM
    //     => build .m2/repository jar path
    // -------------------------------------------------------------------------------------
    private List<URL> getDependenciesClassLoaders() {
        return project.getDependencies().stream()
                   .map(dep -> new File(getPathFromDependency(dep)))
                   .filter(File::exists)
                   .map(this::toURL)
                   .collect(Collectors.toList());
    }


    private List<URL> getDependenciesClassLoadersHardCoded() {
        return getPathFromDependencyHardCoded().stream().map(url -> new File(url))
                   .filter(File::exists)
                   .map(this::toURL)
                   .collect(Collectors.toList());
    }


    private String getPathFromDependency(Dependency dep) {
        // Convert groupId:artifactId:version => ~/.m2/repository/...
        // This can fail if it's a multi-module reference not in local repo yet, or if the artifact is not installed.
        String groupId = dep.getGroupId().replace('.', '/');
        String artifactId = dep.getArtifactId();
        String version = dep.getVersion();
        return System.getProperty("user.home") + "/.m2/repository/"
                   + groupId + "/" + artifactId + "/" + version + "/"
                   + artifactId + "-" + version + ".jar";
    }


    private List<String> getPathFromDependencyHardCoded() {
        String pathFormat = System.getProperty("user.home") + "/.m2/repository/%s/%s/$s/%s-%s.jar";

        List<String> results = List.of(
            String.format(pathFormat, "com/theairebellion/zeus,", "api-interactor", "1.0.0", "api-interactor", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "api-interactor-test-framework-adapter", "1.0.0",
                "api-interactor-test-framework-adapter", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "assertions", "1.0.0", "assertions", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "commons", "1.0.0", "commons", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "test-framework", "1.0.0", "test-framework", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "db-interactor", "1.0.0", "db-interactor", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "db-interactor-test-framework-adapter", "1.0.0",
                "db-interactor-test-framework-adapter", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "ui-interactor", "1.0.0", "ui-interactor", "1.0.0"),
            String.format(pathFormat, "com/theairebellion/zeus,", "ui-interactor-test-framework-adapter", "1.0.0",
                "ui-interactor-test-framework-adapter", "1.0.0"),
            String.format(pathFormat, "org/springframework,", "spring-core", "3.4.1",
                "spring-core", "3.4.1")
        );
        return results;
    }


    // -------------------------------------------------------------------------------------
    // (C) Possibly load additional jar paths from a text file
    // -------------------------------------------------------------------------------------
    private List<URL> getAdditionalClassLoaders() {
        if (pathToFileWithClassLoaders == null || pathToFileWithClassLoaders.isEmpty()) {
            return Collections.emptyList();
        }
        File file = new File(pathToFileWithClassLoaders);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        List<URL> urls = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String ln;
            while ((ln = reader.readLine()) != null) {
                lines.add(ln);
            }
        } catch (IOException e) {
            getLog().warn("Failed reading additional classloader file: " + e);
        }

        for (String line : lines) {
            // Might look like "file:/path/to/dep.jar" or just "/path/to/dep.jar"
            String path = line.replace("file:", "");
            File f = new File(path);
            if (f.exists()) {
                urls.add(toURL(f));
            } else {
                getLog().warn("File not found: " + f);
            }
        }
        // optionally delete or rename the file
        try {
            Files.delete(Paths.get(pathToFileWithClassLoaders));
        } catch (IOException e) {
            // ignore or log
        }
        return urls;
    }


    // Helper: convert File -> URL
    private URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error converting file to URL: " + file, e);
        }
    }


    // ----------------------------------------------------------------
    // Helper: list all .class files recursively
    // ----------------------------------------------------------------
    private List<File> findClassFiles(File baseDir) {
        List<File> results = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();
        queue.add(baseDir);
        while (!queue.isEmpty()) {
            File f = queue.poll();
            if (f.isDirectory()) {
                for (File child : Objects.requireNonNull(f.listFiles())) {
                    queue.add(child);
                }
            } else {
                if (f.getName().endsWith(".class")) {
                    results.add(f);
                }
            }
        }
        return results;
    }


    // ----------------------------------------------------------------
    // Helper: convert file path -> fully qualified class name
    // ----------------------------------------------------------------
    private String fileToClassName(File classFile, File baseDir) {
        // e.g. baseDir = target/test-classes
        //      classFile = target/test-classes/com/mycompany/MyTest.class
        // ->  className = "com.mycompany.MyTest"
        String absPath = classFile.getAbsolutePath();
        String basePath = baseDir.getAbsolutePath();
        String relative = absPath.substring(basePath.length() + 1);
        relative = relative.replace(File.separatorChar, '.');
        // remove ".class"
        return relative.substring(0, relative.length() - 6);
    }


    // ----------------------------------------------------------------
    // Helper: count methods that have @Test and match included/excluded tags
    // ----------------------------------------------------------------
    private int countMatchingTestMethods(Class<?> clazz, Set<String> include, Set<String> exclude) {
        getLog().info("Viksa vleze: ");
        int count = 0;
        for (Method m : clazz.getDeclaredMethods()) {

            if (!m.isAnnotationPresent(Test.class)) {
                continue;
            }
            // Gather all Tag annotations from the method
            Set<String> methodTags = new HashSet<>();
            for (Annotation ann : m.getAnnotations()) {
                if (ann instanceof Tag) {
                    methodTags.add(((Tag) ann).value());
                }
            }
            // Decide if we pass the filter
            if (isMethodIncluded(methodTags, include, exclude)) {
                count++;
            }
        }
        if (clazz.getSuperclass().getSimpleName().contains("BaseTestSequential") && count != 0) {
            return 1;
        }
        return count;
    }


    // ----------------------------------------------------------------
    // Decide if a method is included based on tag sets
    // ----------------------------------------------------------------
    private boolean isMethodIncluded(Set<String> methodTags, Set<String> include, Set<String> exclude) {
        // 1) Exclude check: if the method has any tags that are in 'exclude', skip
        for (String ex : exclude) {
            if (methodTags.contains(ex)) {
                return false;
            }
        }
        // 2) Include check: if 'include' is non-empty, method must have at least one included tag
        if (!include.isEmpty()) {
            boolean found = false;
            for (String inc : include) {
                if (methodTags.contains(inc)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }


    // ----------------------------------------------------------------
    // Helper: group classes so each bucket has up to 'maxMethods'
    //         (simple greedy approach)
    // ----------------------------------------------------------------
    private List<Bucket> groupClasses(Map<String, Integer> classMethodCounts, int max) {
        // Sort by methodCount descending
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(classMethodCounts.entrySet());
        sorted.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<Bucket> buckets = new ArrayList<>();
        Bucket current = new Bucket();

        for (Map.Entry<String, Integer> e : sorted) {
            int methods = e.getValue();
            // if single class > max, put it alone
            if (methods > max) {
                Bucket single = new Bucket();
                single.classNames.add(e.getKey());
                single.totalMethods = methods;
                buckets.add(single);
                continue;
            }

            if (current.totalMethods + methods <= max) {
                // add to current bucket
                current.classNames.add(e.getKey());
                current.totalMethods += methods;
            } else {
                // start new bucket
                buckets.add(current);
                current = new Bucket();
                current.classNames.add(e.getKey());
                current.totalMethods = methods;
            }
        }
        if (!current.classNames.isEmpty()) {
            buckets.add(current);
        }

        return buckets;
    }


    // ----------------------------------------------------------------
    // Helper: parse comma-separated string into a set
    // ----------------------------------------------------------------
    private Set<String> parseCommaSeparated(String value) {
        Set<String> result = new HashSet<>();
        if (value == null || value.trim().isEmpty()) {
            return result;
        }
        String[] parts = value.split(",");
        for (String p : parts) {
            result.add(p.trim());
        }
        return result;
    }


    // ----------------------------------------------------------------
    // Bucket data structure
    // ----------------------------------------------------------------
    static class Bucket {

        List<String> classNames = new ArrayList<>();
        int totalMethods = 0;

    }


    private static List<Method> findAnnotatedMethodsInPackage(String packageName, Class<?> annotationClass,
                                                              ClassLoader... classLoaders) {
        List<Method> annotatedMethods = new ArrayList<>();

        ClassGraph classGraph = new ClassGraph()
                                    .enableAllInfo()
                                    .acceptPackages(packageName);

        for (ClassLoader classLoader : classLoaders) {
            classGraph = classGraph.addClassLoader(classLoader);
        }


        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList classInfos = scanResult.getAllClasses();

            for (ClassInfo classInfo : classInfos) {
                if (classInfo.getName().contains(packageName)) {
                    for (MethodInfo methodInfo : classInfo.getDeclaredMethodInfo()) {
                        if (methodInfo.hasAnnotation(annotationClass.getName())) {
                            Method method = methodInfo.loadClassAndGetMethod();
                            annotatedMethods.add(method);
                        }
                    }
                }
            }
        }

        return annotatedMethods;
    }

}
