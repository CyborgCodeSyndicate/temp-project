package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class CheckboxServiceFluent implements Insertion {

    private final CheckboxService checkboxService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public CheckboxServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, CheckboxService checkboxService) {
        this.checkboxService = checkboxService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent select(final UIElement element, final String value) {
        Allure.step(String.format("Selecting value: '%s' from checkbox component of type: '%s'.", value,
            element.componentType().toString()));
        checkboxService.select(element.componentType(), value);
        return uiServiceFluent;
    }





    @Override
    public void insertion(final By locator, final ComponentType componentType, final Object... values) {
        checkboxService.insertion(locator, componentType, values);
    }

}
