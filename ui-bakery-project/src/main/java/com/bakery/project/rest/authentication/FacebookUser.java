package com.bakery.project.rest.authentication;

import com.theairebellion.zeus.api.authentication.Credentials;

public class FacebookUser implements Credentials {

    @Override
    public String username() {
        return "facebook";
    }


    @Override
    public String password() {
        return "";
    }

}
