package com.example.project.ui.elements.ZeroBank;

import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.UIElement;
import org.openqa.selenium.By;

public enum InputFields implements InputUIElement {

    USERNAME_FIELD(By.id("user_login"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    PASSWORD_FIELD(By.id("user_password"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AMOUNT_FIELD(By.id("tf_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AMOUNT_CURRENCY_FIELD(By.id("pc_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    TF_DESCRIPTION_FIELD(By.id("tf_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AA_DESCRIPTION_FIELD(By.id("aa_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AA_FROM_DATE_FIELD(By.id("aa_fromDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AA_TO_DATE_FIELD(By.id("aa_toDate"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AA_FROM_AMOUNT_FIELD(By.id("aa_fromAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    AA_TO_AMOUNT_FIELD(By.id("aa_toAmount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SP_AMOUNT_FIELD(By.id("sp_amount"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SP_DATE_FIELD(By.id("sp_date"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SP_DESCRIPTION_FIELD(By.id("sp_description"), InputFieldTypes.BOOTSTRAP_INPUT_TYPE),
    ;

    public static final String USERNAME = "USERNAME_FIELD";
    public static final String PASSWORD = "PASSWORD_FIELD";


    private final By locator;
    private final InputComponentType componentType;


    InputFields(final By locator, final InputComponentType componentType) {
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
