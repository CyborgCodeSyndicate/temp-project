package com.bakery.project.ui.authentication;

import com.bakery.project.ui.elements.Bakery.ButtonFields;
import com.bakery.project.ui.elements.Bakery.InputFields;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.openqa.selenium.By;

public class BakeryUILogging extends BaseLoginClient {


    @Override
    protected <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
        uiService
                .getNavigation().navigate("https://bakery-flow.demo.vaadin.com/")
                .getInputField().insert(InputFields.USERNAME_FIELD, username)
                .getInputField().insert(InputFields.PASSWORD_FIELD, password)
                .getButtonField().click(ButtonFields.SIGN_IN_BUTTON);
    }

    @Override
    protected By successfulLoginElementLocator() {
        return By.tagName("vaadin-app-layout");
    }

}
