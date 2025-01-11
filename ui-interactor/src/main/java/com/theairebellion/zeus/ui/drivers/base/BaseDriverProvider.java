package com.theairebellion.zeus.ui.drivers.base;

import org.openqa.selenium.remote.AbstractDriverOptions;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseDriverProvider<T extends AbstractDriverOptions<?>> implements DriverProvider<T> {

    private static final ConcurrentHashMap<String, Boolean> DRIVER_DOWNLOAD_STATUS = new ConcurrentHashMap<>();


    protected abstract String getDriverType(); // Return unique identifier for the driver (e.g., "CHROME", "EDGE")


    @Override
    public void setupDriver(final String version) {
        String driverType = getDriverType();
        DRIVER_DOWNLOAD_STATUS.computeIfAbsent(driverType, key -> {
            synchronized (DRIVER_DOWNLOAD_STATUS) {
                if (!DRIVER_DOWNLOAD_STATUS.containsKey(driverType)) {
                    downloadDriver(version);
                    return true;
                }
                return DRIVER_DOWNLOAD_STATUS.get(driverType);
            }
        });
    }

}
