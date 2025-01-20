package com.reqres.test.framework.data.creator;

import com.reqres.test.framework.rest.dto.LoginUser;
import com.reqres.test.framework.rest.dto.User;

public class DataCreationFunctions {

    public static User createLeaderUser() {
        return User.builder()
                .name("Morpheus")
                .job("Leader")
                .build();
    }

    public static LoginUser createLoginUser() {
        return LoginUser.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();
    }
}
