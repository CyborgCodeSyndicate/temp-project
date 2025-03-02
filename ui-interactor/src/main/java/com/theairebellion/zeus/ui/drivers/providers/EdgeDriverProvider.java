package com.theairebellion.zeus.ui.drivers.providers;

import com.theairebellion.zeus.ui.drivers.base.BaseDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverProvider extends BaseDriverProvider<EdgeOptions> {

    @Override
    protected String getDriverType() {
        return "EDGE";
    }


    @Override
    public EdgeOptions createOptions() {
        return new EdgeOptions();
    }


    @Override
    public WebDriver createDriver(EdgeOptions options) {
        return new EdgeDriver(EdgeDriverService.createDefaultService(), options);
    }


    @Override
    public void applyDefaultArguments(EdgeOptions options) {
        options.addArguments("--disable-gpu", "--no-sandbox", "--remote-allow-origins=*");
    }


    @Override
    public void applyHeadlessArguments(final EdgeOptions options) {
        options.addArguments("--headless", "window-size=1920x1080", "--allow-insecure-localhost", "--disable-dev-shm-usage");
    }



    @Override
    public void downloadDriver(final String version) {
        if (version == null || version.isEmpty()) {
            WebDriverManager.edgedriver().setup();
        } else {
            WebDriverManager.edgedriver().driverVersion(version).setup();
        }
    }

}
