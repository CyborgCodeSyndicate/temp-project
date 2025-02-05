package com.theairebellion.zeus.ui.drivers.base;

import org.openqa.selenium.remote.AbstractDriverOptions;

import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseDriverProvider<T extends AbstractDriverOptions<?>> implements DriverProvider<T> {

    private static final ConcurrentHashMap<String, Boolean> DRIVER_DOWNLOAD_STATUS = new ConcurrentHashMap<>();


    protected abstract String getDriverType();


    @Override
    public void setupDriver(final String version) {
        String driverType = getDriverType();
            DRIVER_DOWNLOAD_STATUS.computeIfAbsent(driverType, key -> {
                downloadDriver(version);
                return true;
            });
    }

}
