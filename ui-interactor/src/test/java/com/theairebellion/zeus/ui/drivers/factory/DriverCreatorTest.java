package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import java.net.MalformedURLException;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverCreatorTest {

   private static final String REMOTE_URL = "http://example.com:4444/wd/hub";

   @InjectMocks
   private DriverCreator<ChromeOptions> driverCreator;

   @Mock
   private WebDriverConfig<ChromeOptions> config;

   @Mock
   private DriverProvider<ChromeOptions> provider;

   @Mock
   private ChromeOptions options;

   @Mock
   private WebDriver webDriver;

   @Mock
   private WebDriver decoratedWebDriver;

   @Mock
   private Consumer<ChromeOptions> optionsCustomizer;

   @Mock
   private EventFiringDecorator<WebDriver> eventFiringDecorator;

   private void mockWindowMaximizeChain(WebDriver driver) {
      WebDriver.Options mockedOptions = mock(WebDriver.Options.class);
      WebDriver.Window mockedWindow = mock(WebDriver.Window.class);
      when(driver.manage()).thenReturn(mockedOptions);
      when(mockedOptions.window()).thenReturn(mockedWindow);
   }

   @Test
   void testCreateLocalDriver() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(false);
      when(config.getOptionsCustomizer()).thenReturn(null);
      when(config.getEventFiringDecorator()).thenReturn(null);
      when(provider.createOptions()).thenReturn(options);
      when(provider.createDriver(options)).thenReturn(webDriver);
      mockWindowMaximizeChain(webDriver);

      WebDriver result = driverCreator.createDriver(config, provider);

      assertNotNull(result);
      assertSame(webDriver, result);
      verify(provider).createOptions();
      verify(provider).applyDefaultArguments(options);
      verify(provider).createDriver(options);
      verify(provider, never()).applyHeadlessArguments(any());
   }

   @Test
   void testCreateHeadlessDriver() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(true);
      when(config.isRemote()).thenReturn(false);
      when(config.getOptionsCustomizer()).thenReturn(null);
      when(config.getEventFiringDecorator()).thenReturn(null);
      when(provider.createOptions()).thenReturn(options);
      when(provider.createDriver(options)).thenReturn(webDriver);
      mockWindowMaximizeChain(webDriver);

      WebDriver result = driverCreator.createDriver(config, provider);

      assertNotNull(result);
      assertSame(webDriver, result);
      verify(provider).createOptions();
      verify(provider).applyDefaultArguments(options);
      verify(provider).applyHeadlessArguments(options);
      verify(provider).createDriver(options);
   }

   @Test
   void testCreateRemoteDriver() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(true);
      when(config.getRemoteUrl()).thenReturn(REMOTE_URL);
      when(config.getOptionsCustomizer()).thenReturn(null);
      when(config.getEventFiringDecorator()).thenReturn(null);
      when(provider.createOptions()).thenReturn(options);

      try (MockedConstruction<RemoteWebDriver> mockedConstruction =
                 mockConstruction(RemoteWebDriver.class, (mock, context) -> mockWindowMaximizeChain(mock))) {

         WebDriver result = driverCreator.createDriver(config, provider);

         assertEquals(1, mockedConstruction.constructed().size());
         verify(provider).createOptions();
         verify(provider).applyDefaultArguments(options);
         verify(provider, never()).createDriver(any());
      }
   }

   @Test
   void testOptionsCustomizer() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(false);
      when(config.getOptionsCustomizer()).thenReturn(optionsCustomizer);
      when(config.getEventFiringDecorator()).thenReturn(null);
      when(provider.createOptions()).thenReturn(options);
      when(provider.createDriver(options)).thenReturn(webDriver);
      mockWindowMaximizeChain(webDriver);

      WebDriver result = driverCreator.createDriver(config, provider);

      verify(optionsCustomizer).accept(options);
      assertSame(webDriver, result);
   }

   @Test
   void testEventFiringDecorator() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(false);
      when(config.getOptionsCustomizer()).thenReturn(null);
      when(config.getEventFiringDecorator()).thenReturn(eventFiringDecorator);
      when(provider.createOptions()).thenReturn(options);
      when(provider.createDriver(options)).thenReturn(webDriver);
      when(eventFiringDecorator.decorate(webDriver)).thenReturn(decoratedWebDriver);
      mockWindowMaximizeChain(webDriver);

      WebDriver result = driverCreator.createDriver(config, provider);

      assertNotNull(result);
      assertSame(decoratedWebDriver, result);
      verify(eventFiringDecorator).decorate(webDriver);
   }

   @Test
   void testNullOptionsCustomizerAndDecorator() throws MalformedURLException {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(false);
      when(config.getOptionsCustomizer()).thenReturn(null);
      when(config.getEventFiringDecorator()).thenReturn(null);
      when(provider.createOptions()).thenReturn(options);
      when(provider.createDriver(options)).thenReturn(webDriver);
      mockWindowMaximizeChain(webDriver);

      WebDriver result = driverCreator.createDriver(config, provider);

      assertNotNull(result);
      assertSame(webDriver, result);
   }

   @Test
   void testMalformedUrlException() {
      when(config.isHeadless()).thenReturn(false);
      when(config.isRemote()).thenReturn(true);
      when(config.getRemoteUrl()).thenReturn("invalid-url");
      when(provider.createOptions()).thenReturn(options);

      assertThrows(MalformedURLException.class, () -> driverCreator.createDriver(config, provider));
   }
}
