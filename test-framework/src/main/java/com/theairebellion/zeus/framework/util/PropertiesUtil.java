package com.theairebellion.zeus.framework.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Utility class for loading and applying system-level properties from the classpath.
 *
 * <p>This class looks for a resource named <code>system.properties</code> on the classpath,
 * and for each property defined therein, sets it into <code>System.getProperties()</code>
 * if no value for that key is already present.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class PropertiesUtil {

   /**
    * Loads <code>system.properties</code> from the root of the classpath (if it exists)
    * and applies each property to the JVM system properties, but only if that property
    * has not already been defined.
    *
    * <p>If the resource cannot be read, this method wraps the resulting {@link IOException}
    * in an unchecked {@link UncheckedIOException}.</p>
    *
    * @throws UncheckedIOException if reading or parsing <code>system.properties</code> fails
    */
   public static void addSystemProperties() {
      Resource resource = new ClassPathResource("system.properties");
      if (resource.exists()) {
         try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            for (String propName : props.stringPropertyNames()) {
               String propValue = props.getProperty(propName);
               if (System.getProperty(propName) == null) {
                  System.setProperty(propName, propValue);
               }
            }
         } catch (IOException e) {
            throw new UncheckedIOException("Failed to load system.properties", e);
         }
      }
   }

}