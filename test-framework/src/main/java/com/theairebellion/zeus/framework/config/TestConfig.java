package com.theairebellion.zeus.framework.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuration class for setting up the test execution environment.
 * <p>
 * This class enables component scanning within the framework and allows
 * dynamic resolution of the project package for test execution.
 * </p>
 *
 * <p>
 * The {@code @SpringBootConfiguration} annotation marks this class as a configuration source,
 * and {@code @ComponentScan} ensures that the required beans are loaded from
 * the specified base packages.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@SpringBootConfiguration
@ComponentScan(basePackages = {
    "com.theairebellion.zeus.framework",
    "${project.package}"
})
public class TestConfig {

}
