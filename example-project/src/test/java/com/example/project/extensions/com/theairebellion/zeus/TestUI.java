package com.example.project.extensions.com.theairebellion.zeus;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.selenium.listeners.WebDriverEventListener;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.pagefactory.ByChained;

public class TestUI {

    public static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);


    @Test
    public void test() {
        SmartWebDriver smartWebDriver =
                new SmartWebDriver(WebDriverFactory.createDriver(uiConfig.browserType(), WebDriverConfig.builder()
                        .version(
                                uiConfig.browserVersion())
                        .headless(uiConfig.headless())
                        .eventFiringDecorator(
                                new EventFiringDecorator<>(
                                        new WebDriverEventListener()))
                        .build()));
        smartWebDriver.get("https://tocka.com.mk/");
        ByChained byChained = new ByChained(By.id("menu_expanded_shou-biznis"), By.tagName("div"), By.className("row"),
                By.cssSelector("div.nav-expand-list"), By.xpath("//ul[1]"));
        SmartWebElement body = smartWebDriver.findSmartElement(By.tagName("body"));
        SmartWebElement smartElement = body.findSmartElement(byChained);
        System.out.println("fsdf");
    }

}
