package com.example.project.ui.authentication;

import com.example.project.ui.UiServiceCustom;
import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.openqa.selenium.By;

public class FacebookUILogging extends BaseLoginClient {


    @Override
    protected <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
        UiServiceCustom uiServiceCustom = (UiServiceCustom) uiService;
        uiServiceCustom
                .input().insert(InputFields.USERNAME, username)
                .input().insert(InputFields.PASSWORD, password);
    }

    @Override
    protected By successfulLoginElementLocator() {
        return By.className("home-page");
    }

}
