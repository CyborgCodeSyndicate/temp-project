package com.theairebellion.zeus.ui.drivers.providers;

import com.theairebellion.zeus.ui.drivers.base.BaseDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Provides a WebDriver implementation for Microsoft Edge.
 *
 * <p>This class extends {@link BaseDriverProvider} and implements methods to configure,
 * create, and manage EdgeDriver instances. It also utilizes WebDriverManager
 * to automatically download the appropriate EdgeDriver version.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class EdgeDriverProvider extends BaseDriverProvider<EdgeOptions> {

   /**
    * Retrieves the type of the driver.
    *
    * @return A string representing the driver type, which is "EDGE".
    */
   @Override
   protected String getDriverType() {
      return "EDGE";
   }

   /**
    * Creates a new instance of {@link EdgeOptions}, which holds the browser configuration.
    *
    * @return A new {@link EdgeOptions} instance.
    */
   @Override
   public EdgeOptions createOptions() {
      return new EdgeOptions();
   }

   /**
    * Creates a new instance of {@link EdgeDriver} using the specified options.
    *
    * @param options The {@link EdgeOptions} instance containing the browser configurations.
    * @return A new {@link EdgeDriver} instance.
    */
   @Override
   public WebDriver createDriver(EdgeOptions options) {
      return new EdgeDriver(EdgeDriverService.createDefaultService(), options);
   }

   /**
    * Applies default arguments to the {@link EdgeOptions} instance.
    *
    * <p>These default arguments help optimize browser execution and enhance stability.
    *
    * @param options The {@link EdgeOptions} instance to modify.
    */
   @Override
   public void applyDefaultArguments(EdgeOptions options) {
      options.addArguments("--disable-gpu", "--no-sandbox", "--remote-allow-origins=*");
   }

   /**
    * Applies additional arguments to enable headless mode in Edge.
    *
    * <p>Headless mode allows running tests without a visible UI, making execution
    * faster and more efficient in CI/CD environments.
    *
    * @param options The {@link EdgeOptions} instance to modify.
    */
   @Override
   public void applyHeadlessArguments(final EdgeOptions options) {
      options.addArguments("--headless", "window-size=1920x1080", "--allow-insecure-localhost",
            "--disable-dev-shm-usage");
   }

   /**
    * Downloads the appropriate EdgeDriver version using WebDriverManager.
    *
    * <p>If a version is provided, it downloads the specified version.
    * Otherwise, it automatically downloads the compatible driver version
    * for the installed Edge browser.
    *
    * @param version The EdgeDriver version to download (optional).
    */
   @Override
   public void downloadDriver(final String version) {
      if (version == null || version.isEmpty()) {
         WebDriverManager.edgedriver().setup();
      } else {
         WebDriverManager.edgedriver().driverVersion(version).setup();
      }
   }

}
