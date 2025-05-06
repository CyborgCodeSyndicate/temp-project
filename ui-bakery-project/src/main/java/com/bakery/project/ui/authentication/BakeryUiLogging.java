package com.bakery.project.ui.authentication;

import com.bakery.project.ui.elements.bakery.ButtonFields;
import com.bakery.project.ui.elements.bakery.InputFields;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import org.openqa.selenium.By;

public class BakeryUiLogging extends BaseLoginClient {


   @Override
   protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
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
