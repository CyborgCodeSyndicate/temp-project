package com.theairebellion.zeus.framework.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

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