package com.theairebellion.zeus.framework.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropertiesUtil {

    public static void addSystemProperties() {
        Resource resource = getResource();
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

    static Resource getResource() {
        return new ClassPathResource("system.properties");
    }

}