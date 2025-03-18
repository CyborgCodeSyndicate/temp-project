package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.AIDisableUsage;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.validator.core.AssertionResult;
import lombok.experimental.Delegate;

import java.util.List;

@AIDisableUsage
public class SuperUIServiceFluent<T extends UIServiceFluent<?>> extends UIServiceFluent<T> {

    @Delegate
    private final UIServiceFluent<T> original;


    public SuperUIServiceFluent(UIServiceFluent<T> uiServiceFluent) {
        super(uiServiceFluent.getDriver());
        this.original = uiServiceFluent;
    }


    public SmartWebDriver getDriver() {
        return original.getDriver();
    }


    @Override
    public void validation(List<AssertionResult<Object>> assertionResults) {
        original.validation(assertionResults);
    }

}
