package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;

public interface LoginClient {

    void login(UIServiceFluent uiService, String username, String password, boolean cache);

}
