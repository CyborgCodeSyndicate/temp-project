package com.theairebellion.zeus.maven.plugins.allocator.discovery;

import com.theairebellion.zeus.maven.plugins.allocator.config.TestSplitterConfiguration;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A custom class loader for loading test classes from the project's compiled output directories.
 *
 * <p>This class constructs a {@link URLClassLoader} using the test and compile classpath elements
 * from the provided Maven project configuration. It is used for dynamically loading test classes
 * for test allocation and execution purposes.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class TestClassLoader {

   /**
    * The URL-based class loader for loading test classes dynamically.
    */
   private final URLClassLoader classLoader;

   /**
    * Constructs a new {@code TestClassLoader} instance using the provided test splitter configuration.
    *
    * @param config The configuration containing the Maven project and test output directory information.
    */
   public TestClassLoader(TestSplitterConfiguration config) {
      this.classLoader = createClassLoader(config);
   }

   /**
    * Creates a new {@link URLClassLoader} with classpath elements from the provided configuration.
    *
    * <p>The class loader includes:
    * <ul>
    *   <li>Test classpath elements</li>
    *   <li>Compile classpath elements</li>
    * </ul>
    *
    * @param config The test splitter configuration containing Maven project information.
    * @return A new {@link URLClassLoader} instance.
    * @throws RuntimeException If an error occurs while creating the class loader.
    */
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

   /**
    * Converts a list of classpath element paths into URLs and adds them to the provided set.
    *
    * @param paths The list of classpath element paths.
    * @param urls  The set to which the URLs should be added.
    * @throws MalformedURLException If a path cannot be converted into a valid URL.
    */
   private void addUrlsFromPaths(List<String> paths, Set<URL> urls) throws MalformedURLException {
      for (String path : paths) {
         urls.add(new File(path).toURI().toURL());
      }
   }

   /**
    * Attempts to load a class by its fully qualified name.
    *
    * @param className The fully qualified name of the class to load.
    * @return The {@code Class} object if found, or {@code null} if the class cannot be loaded.
    */
   public Class<?> loadClass(String className) {
      try {
         return classLoader.loadClass(className);
      } catch (ClassNotFoundException | NoClassDefFoundError e) {
         return null;
      }
   }

}
