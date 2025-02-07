package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.GET_USER;
import static com.reqres.test.framework.rest.Endpoints.LOGIN_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@WorldName("Rivendell")
@Service
@Scope("prototype")
public class CustomService extends FluentService {

    public CustomService loginUserAndAddSpecificHeader(LoginUser loginUser) {
        RestServiceFluent enters = quest.enters(OLYMPYS);
        enters
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
