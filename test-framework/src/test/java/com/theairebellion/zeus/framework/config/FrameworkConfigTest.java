package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrameworkConfigTest {

    @Test
    void testConfigurationValues() {
        // Clear the Owner configuration cache to force a reload
        ConfigCache.clear();

        // Set the placeholder so that the classpath source does not override the system properties.
        System.setProperty("framework.config.file", "nonexistentfile");

        // Set system properties BEFORE obtaining the configuration.
        System.setProperty("project.package", "com.example.project");
        System.setProperty("default.storage", "defaultStorage");

        FrameworkConfig config = FrameworkConfigHolder.getFrameworkConfig();
        assertEquals("com.example.project", config.projectPackage());
        assertEquals("defaultStorage", config.defaultStorage());
    }
}