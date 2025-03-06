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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WebDriverFactory {

    private static final Map<String, DriverProvider<?>> DRIVER_PROVIDERS = new ConcurrentHashMap<>();


    static {
        registerDriver("CHROME", new ChromeDriverProvider());
        registerDriver("EDGE", new EdgeDriverProvider());
    }

    public static <T extends DriverProvider<?>> void registerDriver(String type, T provider) {
        if (DRIVER_PROVIDERS.containsKey(type.toUpperCase())) {
            throw new IllegalArgumentException("Driver type already registered: " + type);
        }
        DRIVER_PROVIDERS.put(type.toUpperCase(), provider);
    }


    public static WebDriver createDriver(String type, WebDriverConfig config) {
        DriverProvider<?> provider = Optional.ofNullable(DRIVER_PROVIDERS.get(type.toUpperCase()))
                                         .orElseThrow(() -> new IllegalArgumentException("No driver registered for type: " + type));

        provider.setupDriver(config.getVersion());

        try {
            WebDriver driver = new DriverCreator<>().createDriver(config,
                    (DriverProvider<AbstractDriverOptions<?>>) provider);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebDriver for type: " + type, e);
        }
    }

}
