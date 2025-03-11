package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.validator.core.AssertionResult;
import lombok.experimental.Delegate;

import java.util.List;

/**
 * A specialized UI service fluent class that extends {@link UIServiceFluent}
 * and delegates its functionalities to an existing instance of {@code UIServiceFluent}.
 *
 * @param <T> The type parameter extending {@link UIServiceFluent}, ensuring fluent interface compatibility.
 *
 * @author Cyborg Code Syndicate
 */
public class SuperUIServiceFluent<T extends UIServiceFluent<?>> extends UIServiceFluent<T> {

    /**
     * A delegate reference to the original {@link UIServiceFluent} instance.
     */
    @Delegate
    private final UIServiceFluent<T> original;

    /**
     * Constructs an instance of {@code SuperUIServiceFluent} by wrapping an existing {@link UIServiceFluent}.
     *
     * @param uiServiceFluent The original {@link UIServiceFluent} instance to delegate functionality to.
     */
    public SuperUIServiceFluent(UIServiceFluent<T> uiServiceFluent) {
        super(uiServiceFluent.getDriver());
        this.original = uiServiceFluent;
    }

    /**
     * Retrieves the {@link SmartWebDriver} instance used by the underlying {@link UIServiceFluent}.
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
