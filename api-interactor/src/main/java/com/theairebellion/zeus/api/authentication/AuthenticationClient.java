package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;

public interface AuthenticationClient {

    AuthenticationKey authenticate(RestService restService, String username, String password, boolean cache);

}
