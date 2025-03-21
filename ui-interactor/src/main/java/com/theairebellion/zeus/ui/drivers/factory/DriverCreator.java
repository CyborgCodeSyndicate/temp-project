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

/**
 * Creates WebDriver instances based on the provided configuration and driver provider.
 * <p>
 * This class is responsible for setting up WebDriver options, applying necessary configurations,
 * and instantiating the WebDriver either locally or remotely.
 * </p>
 * <p>
 * The generic type {@code T} represents browser-specific configuration options that extend
 * {@link AbstractDriverOptions}. This allows flexibility in defining WebDriver settings
 * for different browsers, such as Chrome, Firefox, or Edge.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DriverCreator<T extends AbstractDriverOptions<?>> {

    /**
     * Creates and configures a WebDriver instance based on the given configuration and driver provider.
     *
     * @param config   The configuration settings for the WebDriver instance.
     * @param provider The driver provider responsible for creating and configuring WebDriver options.
     * @return A fully configured {@link WebDriver} instance.
     * @throws MalformedURLException If the remote WebDriver URL is invalid.
     */
    public WebDriver createDriver(WebDriverConfig<T> config, DriverProvider<T> provider) throws MalformedURLException {
        LogUI.info("Creating driver using provider [{}]. Headless: [{}], Remote: [{}], Remote URL: [{}]",
                provider.getClass().getSimpleName(),
                config.isHeadless(),
                config.isRemote(),
                config.getRemoteUrl());


        T options = provider.createOptions();
        provider.applyDefaultArguments(options);

        if (config.isHeadless()) {
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
