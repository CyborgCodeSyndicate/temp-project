package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.log.LogUI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class DriverCreator<T extends AbstractDriverOptions<?>> {

    public WebDriver createDriver(WebDriverConfig config, DriverProvider<T> provider) throws MalformedURLException {

        LogUI.info("Creating driver using provider [{}]. Headless: [{}], Remote: [{}], Remote URL: [{}]",
                provider.getClass().getSimpleName(),
                config.isHeadless(),
                config.isRemote(),
                config.getRemoteUrl());

        if (config.getVersion() != null && !config.getVersion().isEmpty()) {
            LogUI.debug("Browser version specified: [{}]", config.getVersion());
        }

        T options = provider.createOptions();

        provider.applyDefaultArguments(options);
        LogUI.debug("Default arguments applied to [{}].", options.getClass().getSimpleName());

        if (config.isHeadless()) {
            options.setCapability("headless", config.isHeadless());
            LogUI.info("Enabled headless mode for options [{}].", options.getClass().getSimpleName());
        }

        if (config.getOptionsCustomizer() != null) {
            LogUI.debug("Applying custom options via config.getOptionsCustomizer()...");
            config.getOptionsCustomizer().accept(options);
        }
        WebDriver driver;
        if (config.isRemote()) {
            LogUI.info("Creating a RemoteWebDriver at URL: [{}].", config.getRemoteUrl());
            driver = new RemoteWebDriver(new URL(config.getRemoteUrl()), options);
        } else {
            LogUI.info("Creating a local WebDriver using provider: [{}].", provider.getClass().getSimpleName());
            driver = provider.createDriver(options);
        }

        if (config.getEventFiringDecorator() != null) {
            LogUI.debug("Wrapping the driver with EventFiringDecorator.");
            return config.getEventFiringDecorator().decorate(driver);
        }

        LogUI.info("WebDriver successfully created with options: [{}].", options);
        return driver;
    }


    private WebDriver getRealDriverFromDecorator(EventFiringDecorator<WebDriver> decorator) {
        try {
            Field decoratedField = EventFiringDecorator.class.getDeclaredField("decorated");
            decoratedField.setAccessible(true);
            return (WebDriver) decoratedField.get(decorator);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve real WebDriver from EventFiringDecorator", e);
        }
    }

}
