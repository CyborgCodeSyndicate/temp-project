package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.validator.core.AssertionResult;
import java.util.List;
import lombok.experimental.Delegate;

/**
 * A specialized UI service fluent class that extends {@link UiServiceFluent}
 * and delegates its functionalities to an existing instance of {@code UIServiceFluent}.
 *
 * <p>The generic type {@code T} represents a concrete implementation of {@link UiServiceFluent},
 * ensuring fluent interface compatibility and enabling seamless method chaining.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class SuperUiServiceFluent<T extends UiServiceFluent<?>> extends UiServiceFluent<T> {

   /**
    * A delegate reference to the original {@link UiServiceFluent} instance.
    */
   @Delegate
   private final UiServiceFluent<T> original;

   /**
    * Constructs an instance of {@code SuperUIServiceFluent} by wrapping an existing {@link UiServiceFluent}.
    *
    * @param uiServiceFluent The original {@link UiServiceFluent} instance to delegate functionality to.
    */
   public SuperUiServiceFluent(UiServiceFluent<T> uiServiceFluent) {
      super(uiServiceFluent.getDriver());
      this.original = uiServiceFluent;
   }

   /**
    * Retrieves the {@link SmartWebDriver} instance used by the underlying {@link UiServiceFluent}.
    *
    * @return The {@link SmartWebDriver} instance.
    */
   public SmartWebDriver getDriver() {
      return original.getDriver();
   }

   /**
    * Executes validation using a list of assertion results.
    *
    * @param assertionResults A list of {@link AssertionResult} objects representing validation outcomes.
    */
   @Override
   public void validation(List<AssertionResult<Object>> assertionResults) {
      original.validation(assertionResults);
   }

}
