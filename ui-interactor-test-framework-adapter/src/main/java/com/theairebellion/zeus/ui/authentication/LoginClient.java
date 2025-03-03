package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;

public interface LoginClient {

    void login(SuperUIServiceFluent<?> uiService, String username, String password, boolean cache);

}
