package com.theairebellion.zeus.ui.drivers.providers;

import com.theairebellion.zeus.ui.drivers.base.BaseDriverProvider;
import com.theairebellion.zeus.ui.log.LogUI;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverProvider extends BaseDriverProvider<ChromeOptions> {

    @Override
    protected String getDriverType() {
        return "CHROME";
    }


    @Override
    public ChromeOptions createOptions() {
        return new ChromeOptions();
    }


    @Override
    public WebDriver createDriver(ChromeOptions options) {
        return new ChromeDriver(ChromeDriverService.createDefaultService(), options);
    }


    @Override
    public void applyDefaultArguments(ChromeOptions options) {
        options.addArguments("--disable-gpu", "--no-sandbox");
    }


    @Override
    public void downloadDriver(final String version) {
        if (version == null || version.isEmpty()) {
            WebDriverManager.chromedriver().setup();
            LogUI.info("Chrome driver is downloaded with compatible version for the installed browser");
        } else {
            WebDriverManager.chromedriver().driverVersion(version).setup();
            LogUI.info("Chrome driver is downloaded with version: '{}'", version);
        }
    }

}
