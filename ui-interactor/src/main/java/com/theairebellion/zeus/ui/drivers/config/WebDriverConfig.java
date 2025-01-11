package com.theairebellion.zeus.ui.drivers.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
@Builder
public class WebDriverConfig<T extends AbstractDriverOptions<?>> {

    private final String version;
    private final boolean headless;
    private final boolean remote;
    private final String remoteUrl;
    private final Consumer<T> optionsCustomizer;
    private final EventFiringDecorator<WebDriver> eventFiringDecorator;

}
