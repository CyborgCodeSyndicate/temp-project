package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;

import java.time.Instant;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.reqres.test.framework.utils.AssertionMessages.*;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Roles.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static io.restassured.http.ContentType.JSON;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestService("Gondor")
@InfoAIClass(
        level = Level.LAST,
        description = "Provides reusable test logic for interacting with the API. " +
                "Methods in this service perform actions such as sending requests, validating responses, and retrieving stored data. " +
                "Used in multiple test cases to avoid code duplication.")
public class EvolutionService extends FluentService {

    @InfoAI(description = "Sends a request to retrieve all users from the API and validates the response. " +
            "The request includes a query parameter to fetch users from page 2, and the response is validated to ensure the status is HTTP 200 OK.")
    public EvolutionService getAllUsersAndValidateResponse() {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
                );
        return this;
    }

    @InfoAI(description = "Creates a new junior user in the system and validates the response. " +
            "The user details are provided as input, and the response is checked for a successful creation (HTTP 201), correct content type, and expected name and job values.")
    public EvolutionService createJuniorUserAndValidateResponse(@InfoAI(description = "The user object containing details of the junior user to be created.") User juniorUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        juniorUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
                );
        return this;
    }

    @InfoAI(description = "Creates a new leader user in the system and validates the response. " +
            "The user details are provided as input, and the response is checked for a successful creation (HTTP 201), expected name 'Morpheus', and job title 'Leader'.")
    public EvolutionService createLeaderUserAndValidateResponse(@InfoAI(description = "The user object containing details of the leader user to be created.") User leaderUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        leaderUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
                );
        return this;
    }

    @InfoAI(description = "Creates a new senior user in the system and validates the response. " +
            "The user details are provided as input, and the response is checked for a successful creation (HTTP 201), expected name 'Mr. Morpheus', and job title 'Senior Leader'.")
    public EvolutionService createSeniorUserAndValidateResponse(@InfoAI(description = "The user object containing details of the senior user to be created.") User seniorUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        seniorUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
                );
        return this;
    }

    @InfoAI(description = "Validates the details of a previously created user by retrieving the stored response and verifying the name, job title, and creation date.")
    public EvolutionService validateCreatedUser() {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = quest
                            .getStorage()
                            .sub(StorageKeysApi.API)
                            .get(POST_CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), NAME_INCORRECT);
                    assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), JOB_INCORRECT);
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
                });
        return this;
    }

}
