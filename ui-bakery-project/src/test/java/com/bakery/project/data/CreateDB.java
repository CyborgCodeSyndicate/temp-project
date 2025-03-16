package com.bakery.project.data;

import com.bakery.project.db.h2.H2Database;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.framework.base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

import static com.bakery.project.db.Queries.QUERY_ORDER;

public class CreateDB implements BeforeAllCallback, HooksDatabase {

    static {
        synchronized (BaseTest.class) {
            addSystemProperties();
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.reconfigure();
        }
    }

    private static void addSystemProperties() {
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

    @Override
    public void beforeAll(ExtensionContext context) {
        DatabaseConfiguration dbConfig = QUERY_ORDER.config();
        H2Database.initialize(dbConfig, dbService());
    }
}
