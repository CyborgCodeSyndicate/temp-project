package com.example.project.ui.authentication;

import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.openqa.selenium.By;

public class FacebookUILogging extends BaseLoginClient {

    @Override
    protected void loginImpl(final UIServiceFluent uiService, final String username, final String password) {
        uiService
            .input().insert(InputFields.USERNAME, username)
            .input().insert(InputFields.PASSWORD, password);
    }


    @Override
    protected By successfulLoginElementLocator() {
        return By.className("home-page");
    }

}
