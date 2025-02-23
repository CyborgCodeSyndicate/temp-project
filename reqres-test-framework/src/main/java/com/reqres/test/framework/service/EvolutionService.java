package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.ApiResponsesJsonPaths;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.CREATE_USER;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    public EvolutionService createLeaderUserAndValidateResponse(User leaderUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        leaderUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Leader").soft(true).build()
                );
        return this;
    }

    public EvolutionService createSeniorUserAndValidateResponse(User seniorUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        seniorUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Mr. Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Senior Leader").soft(true).build()
                );
        return this;
    }

    public EvolutionService validateCreatedUser() {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = quest
                            .getStorage()
                            .sub(StorageKeysApi.API)
                            .get(CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                });
        return this;
    }
}
