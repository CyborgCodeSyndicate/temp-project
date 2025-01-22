package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.providers.ChromeDriverProvider;
import com.theairebellion.zeus.ui.drivers.providers.EdgeDriverProvider;
import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    private static final Map<String, DriverProvider<?>> DRIVER_PROVIDERS = new HashMap<>();

    static {
        registerDriver("CHROME", new ChromeDriverProvider());
        registerDriver("EDGE", new EdgeDriverProvider());
    }

    public static <T extends DriverProvider<?>> void registerDriver(String type, T provider) {
        LogUI.info("Registering driver provider for type: [{}], provider class: [{}]",
                type, provider.getClass().getSimpleName());
        DRIVER_PROVIDERS.put(type.toUpperCase(), provider);
        LogUI.debug("Current driver providers map size: [{}]", DRIVER_PROVIDERS.size());
    }


    public static WebDriver createDriver(String type, WebDriverConfig config) {
        LogUI.info("WebDriverFactory: Requesting creation of driver type: [{}], version: [{}], remote: [{}].",
                type, config.getVersion(), config.isRemote());

        DriverProvider<?> provider = DRIVER_PROVIDERS.get(type.toUpperCase());
        if (provider == null) {
            LogUI.error("No provider registered for type: [{}]. Aborting.", type);
            throw new IllegalArgumentException("No driver registered for type: " + type);
        }
        provider.setupDriver(config.getVersion());
        LogUI.debug("Setup driver for type [{}] with version [{}].", type, config.getVersion());

        try {
            WebDriver driver = new DriverCreator<>().createDriver(config,
                    (DriverProvider<AbstractDriverOptions<?>>) provider);
            LogUI.info("WebDriverFactory: Successfully created driver for type: [{}].", type);
            return driver;
        } catch (Exception e) {
            LogUI.error("Driver creation failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create WebDriver for type: " + type, e);
        }
    }

}
