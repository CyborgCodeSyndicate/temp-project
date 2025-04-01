package com.reqres.test.framework;

import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.preconditions.QuestPreconditions.Data.CREATE_NEW_USER;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.Helpers.EMPTY_JSON;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.*;

@API
public class ReqresApiAIGeneratedTest extends BaseTest {

    @Test
    @Regression
    public void testGetAllUsersPage1AndPage2(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 1),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected("Tired of writing").build(),
                        Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(".jpg").build(),
                        Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build()
                )
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(PAGE.getJsonPath()).type(IS).expected(2).build(),
                        Assertion.builder().target(BODY).key(USER_EMAIL_BY_INDEX.getJsonPath(5)).type(CONTAINS).expected("@reqres.in").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testGetUserByIdValidAndInvalid(Quest quest) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS.withQueryParam("page", 2))
                .requestAndValidate(
                        GET_USER.withPathParam("id", 9),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_FIRST_NAME.getJsonPath()).type(IS).expected("Tobias").build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL.getJsonPath()).type(CONTAINS).expected("@reqres.in").build()
                )
                .requestAndValidate(
                        GET_USER.withPathParam("id", 999),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testCreateUserWithValidPayload(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        user,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected(user.getName()).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected(user.getJob()).build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_ID.getJsonPath()).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_TIMESTAMP.getJsonPath()).type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginUserWithValidCredentials(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_LOGIN_USER,
                        loginUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(MATCHES_REGEX).expected("[a-zA-Z0-9]+").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testDeleteUserAfterCreation(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS)
                .request(POST_CREATE_USER, user.join())
                .requestAndValidate(
                        DELETE_USER.withPathParam("id",
                                Integer.parseInt(
                                        retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                                .getBody().as(CreatedUserResponse.class).getId()
                                )
                        ),
                        user,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .complete();
    }

    @Test
    @Regression
    @Smoke
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    public void testCreateIntermediateUserAfterLeaderAndLogin(Quest quest,
                                                              @Craft(model = USER_LEADER) Late<User> userLeader,
                                                              @Craft(model = USER_INTERMEDIATE) Late<User> userIntermediate) {

        String leaderId = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                .getBody().as(CreatedUserResponse.class).getId();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userIntermediate.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected("Mr. Morpheus").build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected("Intermediate Leader").build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_ID.getJsonPath()).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_TIMESTAMP.getJsonPath()).type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", leaderId),
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                .getBody().as(CreatedUserResponse.class).getId()),
                        userIntermediate,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .complete();
    }

    @Test
    @Regression
    @Smoke
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testCreateIntermediateUserAfterLeaderAndLogin2(Quest quest,
                                                               @Craft(model = USER_LEADER) Late<User> userLeader,
                                                               @Craft(model = USER_INTERMEDIATE) Late<User> userIntermediate) {

        String leaderId = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                .getBody().as(CreatedUserResponse.class).getId();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userIntermediate.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME.getJsonPath()).type(IS).expected("Mr. Morpheus").build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB.getJsonPath()).type(IS).expected("Intermediate Leader").build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_ID.getJsonPath()).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(CREATED_USER_TIMESTAMP.getJsonPath()).type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", leaderId),
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                .getBody().as(CreatedUserResponse.class).getId()),
                        userIntermediate,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .complete();
    }

    @Test
    @Regression
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    public void testGetNonExistentUserReturns404AndEmptyBody(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam("id", 23),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                        Assertion.builder().target(BODY).key(ROOT.getJsonPath()).type(IS).expected(EMPTY_JSON).build()
                )
                .complete();
    }
}
