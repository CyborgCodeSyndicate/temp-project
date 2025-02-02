package com.theairebellion.zeus.framework.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan(basePackages = {
    "com.theairebellion.zeus.framework",
    "${project.package}"
})
public class TestConfig {

}
