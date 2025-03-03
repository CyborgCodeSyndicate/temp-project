package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class DriverCreator<T extends AbstractDriverOptions<?>> {

    public WebDriver createDriver(WebDriverConfig<T> config, DriverProvider<T> provider) throws MalformedURLException {
        LogUI.info("Creating driver using provider [{}]. Headless: [{}], Remote: [{}], Remote URL: [{}]",
                provider.getClass().getSimpleName(),
                config.isHeadless(),
                config.isRemote(),
                config.getRemoteUrl());


        T options = provider.createOptions();
        provider.applyDefaultArguments(options);

        if (config.isHeadless()) {
            // options.setCapability("headless", true);
            LogUI.info("Headless capability added to webdriver");
            provider.applyHeadlessArguments(options);
        }

        Optional.ofNullable(config.getOptionsCustomizer()).ifPresent(customizer -> customizer.accept(options));

        WebDriver driver;
        if (config.isRemote()) {
            driver = new RemoteWebDriver(new URL(config.getRemoteUrl()), options);
        } else {
            driver = provider.createDriver(options);
        }

        return Optional.ofNullable(config.getEventFiringDecorator())
                   .map(decorator -> decorator.decorate(driver))
                   .orElse(driver);

    }

}
