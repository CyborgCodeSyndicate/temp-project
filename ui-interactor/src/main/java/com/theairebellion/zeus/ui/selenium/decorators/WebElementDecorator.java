package com.theairebellion.zeus.ui.selenium.decorators;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.openqa.selenium.WebElement;

/**
 * A base decorator class for {@link WebElement} that provides a wrapper for additional functionality.
 *
 * <p>This abstract class delegates all method calls to the original {@link WebElement}, allowing
 * extensions to enhance WebElement interactions without modifying the core Selenium API.
 *
 * <p>Subclasses can override specific methods to add custom behavior, such as logging, exception handling,
 * or custom waiting mechanisms.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Getter
public abstract class WebElementDecorator implements WebElement {

   /**
    * The original {@link WebElement} instance wrapped by this decorator.
    */
   @Delegate
   protected final WebElement original;

   /**
    * Constructs a new {@code WebElementDecorator} wrapping the specified WebElement.
    *
    * @param original The original WebElement to be wrapped.
    */
   public WebElementDecorator(WebElement original) {
      this.original = original;
   }

}
