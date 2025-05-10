package com.theairebellion.zeus.ui.drivers.providers;

import com.theairebellion.zeus.ui.drivers.base.BaseDriverProvider;
import com.theairebellion.zeus.ui.log.LogUi;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Provides a WebDriver implementation for the Google Chrome browser.
 *
 * <p>This class extends {@link BaseDriverProvider} and implements methods to configure,
 * create, and manage ChromeDriver instances. It also utilizes WebDriverManager
 * to automatically download the appropriate ChromeDriver version.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ChromeDriverProvider extends BaseDriverProvider<ChromeOptions> {

   /**
    * Retrieves the type of the driver.
    *
    * @return A string representing the driver type, which is "CHROME".
    */
   @Override
   protected String getDriverType() {
      return "CHROME";
   }

   /**
    * Creates a new instance of ChromeOptions, which holds the browser configuration.
    *
    * @return A new {@link ChromeOptions} instance.
    */
   @Override
   public ChromeOptions createOptions() {
      return new ChromeOptions();
   }

   /**
    * Creates a new instance of {@link ChromeDriver} using the specified options.
    *
    * @param options The ChromeOptions instance containing the browser configurations.
    * @return A new {@link ChromeDriver} instance.
    */
   @Override
   public WebDriver createDriver(ChromeOptions options) {
      return new ChromeDriver(ChromeDriverService.createDefaultService(), options);
   }

   /**
    * Applies default arguments to the ChromeOptions instance.
    *
    * <p>The default arguments help optimize browser execution and enhance stability.
    *
    * @param options The {@link ChromeOptions} instance to modify.
    */
   @Override
   public void applyDefaultArguments(ChromeOptions options) {
      options.addArguments("--disable-gpu", "--no-sandbox", "--remote-allow-origins=*");
   }

   /**
    * Applies additional arguments to enable headless mode in Chrome.
    *
    * <p>Headless mode allows running tests without a visible UI, making execution faster and more efficient in
    * CI/CD environments.
    *
    * @param options The {@link ChromeOptions} instance to modify.
    */
   @Override
   public void applyHeadlessArguments(final ChromeOptions options) {
      options.addArguments("--headless", "window-size=1920x1080", "--allow-insecure-localhost",
            "--disable-dev-shm-usage");
   }

   /**
    * Downloads the appropriate ChromeDriver version using WebDriverManager.
    *
    * <p>If a version is provided, it downloads the specified version.
    * Otherwise, it automatically downloads the compatible driver version
    * for the installed Chrome browser.
    *
    * @param version The ChromeDriver version to download (optional).
    */
   @Override
   public void downloadDriver(final String version) {
      if (version == null || version.isEmpty()) {
         WebDriverManager.chromedriver().setup();
         LogUi.info("Chrome driver is downloaded with a compatible version for the installed browser");
      } else {
         WebDriverManager.chromedriver().driverVersion(version).setup();
         LogUi.info("Chrome driver is downloaded with version: '{}'", version);
      }
   }

}
