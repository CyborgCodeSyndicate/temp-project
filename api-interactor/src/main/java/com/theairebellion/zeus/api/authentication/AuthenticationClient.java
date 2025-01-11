package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;

public interface AuthenticationClient {

    void authenticate(RestService restService, String username, String password);


}
