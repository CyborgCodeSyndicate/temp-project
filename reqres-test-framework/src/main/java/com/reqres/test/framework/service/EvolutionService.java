package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.ApiResponsesJsonPaths;
import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.CREATE_USER;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@TestService("Gondor")
public class EvolutionService extends FluentService {

    public EvolutionService getAllUsersAndValidateResponse() {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
        return this;
    }

    public EvolutionService createJuniorUserAndValidateResponse(User juniorUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        juniorUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(HEADER).key(HttpHeaders.CONTENT_TYPE).type(CONTAINS).expected(ContentType.JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_NAME.getJsonPath()).type(IS).expected("Michael suffix").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_JOB.getJsonPath()).type(IS).expected("JuniorLawson worker").build()
                );
        return this;
    }
}
