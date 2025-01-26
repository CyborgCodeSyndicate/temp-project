package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.listeners.WebDriverEventListener;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class UIInteractionAutoConfiguration {

    public static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);


    @Bean
    @Lazy
    @Scope("prototype")
    public SmartWebDriver webDriver() {
        return new SmartWebDriver(WebDriverFactory.createDriver(uiConfig.browserType(), WebDriverConfig.builder()
                .version(
                        uiConfig.browserVersion())
                .headless(uiConfig.headless())
                .eventFiringDecorator(
                        new EventFiringDecorator<>(
                                new WebDriverEventListener()))
                .build()));
    }


}
