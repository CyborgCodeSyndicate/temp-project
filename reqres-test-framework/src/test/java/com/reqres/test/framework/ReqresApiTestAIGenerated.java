package com.reqres.test.framework;

import com.reqres.test.framework.rest.Endpoints;
import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.api.validator.RestAssertionTarget;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.preconditions.QuestPreconditions.Data.CREATE_NEW_USER;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;

@API
public class ReqresApiTestAIGenerated extends BaseTest {

    @Test
    @Regression
    public void testGetAllUsersPage1AndPage2(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.GET_ALL_USERS.withQueryParam("page", 1),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(RestAssertionTarget.HEADER).key(HttpHeaders.CONTENT_TYPE).type(CONTAINS).expected(ContentType.JSON.toString()).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data").type(NOT_EMPTY).expected(true).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("support.text").type(CONTAINS).expected("Tired of writing").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data[0].avatar").type(ENDS_WITH).expected(".jpg").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data[0].id").type(NOT_NULL).expected(true).build()
                )
                .requestAndValidate(
                        Endpoints.GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("page").type(IS).expected(2).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data[5].email").type(CONTAINS).expected("@reqres.in").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testGetUserByIdValidAndInvalid(Quest quest) {
        quest.enters(OLYMPYS)
                .request(Endpoints.GET_ALL_USERS.withQueryParam("page", 2))
                .requestAndValidate(
                        Endpoints.GET_USER.withPathParam("id", 9),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data.first_name").type(IS).expected("Tobias").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("data.email").type(CONTAINS).expected("@reqres.in").build()
                )
                .requestAndValidate(
                        Endpoints.GET_USER.withPathParam("id", 999),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NOT_FOUND).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testCreateUserWithValidPayload(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.POST_CREATE_USER,
                        user,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("name").type(IS).expected(user.getName()).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("job").type(IS).expected(user.getJob()).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("id").type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("createdAt").type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginUserWithValidCredentials(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.POST_LOGIN_USER,
                        loginUser,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("token").type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("token").type(MATCHES_REGEX).expected("[a-zA-Z0-9]+").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testDeleteUserAfterCreation(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
        quest.enters(OLYMPYS)
                .request(Endpoints.GET_ALL_USERS)
                .request(Endpoints.POST_CREATE_USER, user.join())
                .requestAndValidate(
                        Endpoints.DELETE_USER.withPathParam("id",
                                Integer.parseInt(
                                        retrieve(StorageKeysApi.API, Endpoints.POST_CREATE_USER, Response.class)
                                                .getBody().as(CreatedUserResponse.class).getId()
                                )
                        ),
                        user,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
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

        String leaderId = retrieve(StorageKeysApi.API, Endpoints.POST_CREATE_USER, Response.class)
                .getBody().as(CreatedUserResponse.class).getId();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.POST_CREATE_USER,
                        userIntermediate.join(),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("name").type(IS).expected("Mr. Morpheus").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("job").type(IS).expected("Intermediate Leader").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("id").type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("createdAt").type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .requestAndValidate(
                        Endpoints.DELETE_USER.withPathParam("id", leaderId),
                        userLeader,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                )
                .requestAndValidate(
                        Endpoints.DELETE_USER.withPathParam("id", retrieve(StorageKeysApi.API, Endpoints.POST_CREATE_USER, Response.class)
                                .getBody().as(CreatedUserResponse.class).getId()),
                        userIntermediate,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
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

        String leaderId = retrieve(StorageKeysApi.API, Endpoints.POST_CREATE_USER, Response.class)
                .getBody().as(CreatedUserResponse.class).getId();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.POST_CREATE_USER,
                        userIntermediate.join(),
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("name").type(IS).expected("Mr. Morpheus").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("job").type(IS).expected("Intermediate Leader").build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("id").type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(RestAssertionTarget.BODY).key("createdAt").type(MATCHES_REGEX).expected("^\\d{4}-\\d{2}-\\d{2}T.*Z$").build()
                )
                .requestAndValidate(
                        Endpoints.DELETE_USER.withPathParam("id", leaderId),
                        userLeader,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                )
                .requestAndValidate(
                        Endpoints.DELETE_USER.withPathParam("id", retrieve(StorageKeysApi.API, Endpoints.POST_CREATE_USER, Response.class)
                                .getBody().as(CreatedUserResponse.class).getId()),
                        userIntermediate,
                        Assertion.builder().target(RestAssertionTarget.STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                )
                .complete();
    }

}
