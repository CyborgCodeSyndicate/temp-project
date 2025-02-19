package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.GET_USER;
import static com.reqres.test.framework.rest.Endpoints.LOGIN_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@TestService("Rivendell")
public class CustomService extends FluentService {

    public CustomService loginUserAndAddSpecificHeader(LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .request(LOGIN_USER, loginUser)
                .requestAndValidate(
                        GET_USER
                                .withPathParam("id", 3)
                                .withHeader("SpecificHeader", quest.getStorage().sub(StorageKeysApi.API).get(LOGIN_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString("token")),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
        return this;
    }

}
