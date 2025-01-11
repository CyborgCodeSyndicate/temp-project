package com.theairebellion.zeus.ui.drivers.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;

public interface DriverProvider<T extends AbstractDriverOptions<?>> {

    T createOptions();

    WebDriver createDriver(T options);

    void applyDefaultArguments(T options);

    void setupDriver(String version);

    void downloadDriver(String version);

}