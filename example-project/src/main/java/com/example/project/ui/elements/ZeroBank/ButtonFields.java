package com.example.project.ui.elements.ZeroBank;

import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import org.openqa.selenium.By;

public enum ButtonFields implements ButtonUIElement {

    SIGN_IN_BUTTON(By.id("signin_button"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SIGN_IN_FORM_BUTTON(By.cssSelector("input[value='Sign in']"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SUBMIT_BUTTON(By.id("btn_submit"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    CALCULATE_COST_BUTTON(By.id("pc_calculate_costs"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    PURCHASE_BUTTON(By.id("purchase_cash"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    MORE_SERVICES_BUTTON(By.id("online-banking"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    FIND_SUBMIT_BUTTON(By.cssSelector("button[type='submit']"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    PAY_BUTTON(By.id("pay_saved_payees"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    ;

    private final By locator;
    private final ButtonComponentType componentType;


    ButtonFields(final By locator, final ButtonComponentType componentType) {
        this.locator = locator;
        this.componentType = componentType;
    }


    @Override
    public By locator() {
        return locator;
    }


    @Override
    public <T extends ComponentType> T componentType() {
        return (T) componentType;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
