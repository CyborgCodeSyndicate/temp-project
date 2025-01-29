package com.example.project;

import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.factory.WebDriverFactory;
import com.theairebellion.zeus.ui.selenium.listeners.WebDriverEventListener;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;
import java.util.Set;

public class TestUI {

    static {

        Resource resource = new ClassPathResource("system.properties");
        if (resource.exists()) {
            try {
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                for (String propName : props.stringPropertyNames()) {
                    String propValue = props.getProperty(propName);
                    System.setProperty(propName, propValue);
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to load system.properties", e);
            }
        }
    }

    public static final UiConfig uiConfig = ConfigCache.getOrCreate(UiConfig.class);


    @Test
    public void test() throws InterruptedException {
        SmartWebDriver driver1 =
            new SmartWebDriver(WebDriverFactory.createDriver("CHROME", WebDriverConfig.builder()
                                                                           .version(
                                                                               "")
                                                                           .headless(false)
                                                                           .eventFiringDecorator(
                                                                               new EventFiringDecorator<>(
                                                                                   new WebDriverEventListener()))
                                                                           .build()));

        driver1.get("https://evnonline.mk/auth/login");
        SmartWebElement email = driver1.findSmartElement(By.name("UsernameEmail"));
        SmartWebElement pass = driver1.findSmartElement(By.name("password"));
        SmartWebElement button = driver1.findSmartElement(
            By.className("btnNext"));
        email.sendKeys("vsushelski@gmail.com");
        pass.sendKeys("Sus1212361_w550");
        button.click();

        Thread.sleep(3000);

        Set<Cookie> cookies = driver1.manage().getCookies();

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver1.getOriginal();
        String localStorage = (String) jsExecutor.executeScript("return JSON.stringify(window.localStorage);");


        SmartWebDriver driver2 =
            new SmartWebDriver(WebDriverFactory.createDriver("CHROME", WebDriverConfig.builder()
                                                                           .version(
                                                                               "")
                                                                           .headless(false)
                                                                           .eventFiringDecorator(
                                                                               new EventFiringDecorator<>(
                                                                                   new WebDriverEventListener()))
                                                                           .build()));

        driver2.get("https://evnonline.mk/authorized/home");




        for (Cookie cookie : cookies) {
            try {
                driver2.manage().addCookie(cookie);

            }catch (Exception e){
            }
        }

        driver2.get("https://evnonline.mk/authorized/home");


        JavascriptExecutor jsExecutor2 = (JavascriptExecutor) driver2.getOriginal();
        jsExecutor2.executeScript("let data = " + localStorage + "; for (let key in data) { window.localStorage.setItem(key, data[key]); }");


        System.out.println("fsdgfre");



        // smartWebDriver.get("https://tocka.com.mk/");
        // ByChained byChained = new ByChained(By.id("menu_expanded_shou-biznis"), By.tagName("div"), By.className("row"),
        //         By.cssSelector("div.nav-expand-list"), By.xpath("//ul[1]"));
        // SmartWebElement body = smartWebDriver.findSmartElement(By.tagName("body"));
        // SmartWebElement smartElement = body.findSmartElement(byChained);
        // System.out.println("fsdf");
    }

}
