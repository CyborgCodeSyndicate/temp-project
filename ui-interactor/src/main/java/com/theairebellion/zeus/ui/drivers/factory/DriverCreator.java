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
        T options = provider.createOptions();


        provider.applyDefaultArguments(options);

        if (config.isHeadless()) {
            options.setCapability("headless", config.isHeadless());
            LogUI.info("Headless capability added to webdriver");
        }

        if (config.getOptionsCustomizer() != null) {
            config.getOptionsCustomizer().accept(options);
        }
        WebDriver driver;
        if (config.isRemote()) {
            LogUI.info("Remote webdriver is started");
            driver = new RemoteWebDriver(new URL(config.getRemoteUrl()), options);
        } else {
            LogUI.info("Webdriver is started");
            driver = provider.createDriver(options);
        }

        if (config.getEventFiringDecorator() != null) {
            return config.getEventFiringDecorator().decorate(driver);
        }

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
