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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Properties;

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

        driver1.get("https://www.w3schools.com/");
        driver1.manage().window().maximize();

        Thread.sleep(1000);
        // driver1.findSmartElement(By.cssSelector("input[aria-labelledby='vaadin-text-field-label-0 vaadin-text-field-input-0']")).sendKeys("admin@vaadin.com");
        // driver1.findSmartElement(By.cssSelector("input[aria-labelledby='vaadin-password-field-label-1 vaadin-password-field-input-1']")).sendKeys("admin");
        // driver1.findSmartElement(By.cssSelector("vaadin-button[part='vaadin-login-submit']")).click();
        // SmartWebElement smartElement = driver1.findSmartElement(By.className("row"));
        SmartWebElement smartElement = driver1.findSmartElement(By.className("codeeditorbr-container"));
        SmartWebElement smartElement1 = smartElement.findSmartElement(By.className("slideshow-container"));


        Thread.sleep(3000);


    }



}
