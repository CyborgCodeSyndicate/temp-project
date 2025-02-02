package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.listeners.WebDriverEventListener;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

@Configuration
public class UIInteractionAutoConfiguration {


    @Bean
    @Lazy
    @Scope("prototype")
    public SmartWebDriver webDriver() {
        return new SmartWebDriver(WebDriverFactory.createDriver(getUiConfig().browserType(), WebDriverConfig.builder()
                                                                                                 .version(
                                                                                                     getUiConfig().browserVersion())
                                                                                                 .headless(
                                                                                                     getUiConfig().headless())
                                                                                                 .eventFiringDecorator(
                                                                                                     new EventFiringDecorator<>(
                                                                                                         new WebDriverEventListener()))
                                                                                                 .build()));
    }



}
