package com.theairebellion.zeus.ui.selenium.decorators;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.openqa.selenium.WebDriver;

/**
 * Abstract base class for WebDriver decorators.
 *
 * <p>This class allows for extending WebDriver functionality by wrapping an existing WebDriver instance.
 * It provides a foundation for additional behaviors such as logging, exception handling, or event-driven execution.
 *
 * <p>Subclasses should extend this class and implement any additional WebDriver-related features.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public abstract class WebDriverDecorator implements WebDriver {

   /**
    * The original WebDriver instance being decorated.
    */
   @Delegate
   protected final WebDriver original;

   /**
    * Constructs a WebDriverDecorator wrapping an existing WebDriver instance.
    *
    * @param original The WebDriver instance to be decorated.
    */
   protected WebDriverDecorator(WebDriver original) {
      this.original = original;
   }
}
