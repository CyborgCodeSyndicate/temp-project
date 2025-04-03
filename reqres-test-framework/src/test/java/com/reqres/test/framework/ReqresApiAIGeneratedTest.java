package com.reqres.test.framework;

import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.annotation.Smoke;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.AssertionMessages.EMAIL_FOUND_IN_LIST_UNEXPECTED;
import static com.reqres.test.framework.utils.Helpers.EMPTY_JSON;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.Login.TOKEN_REGEX;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_ONE;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Roles.*;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static com.reqres.test.framework.utils.TestConstants.Users.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class ReqresApiAIGeneratedTest extends BaseTest {

    @Test
    @Regression
    public void testGetAllUsersPage1AndPage2(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_ONE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(CONTAINS).expected(SUPPORT_TEXT_PREFIX).build(),
                        Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                        Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build()
                )
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(PAGE.getJsonPath()).type(IS).expected(PAGE_TWO).build(),
                        Assertion.builder().target(BODY).key(USER_EMAIL_BY_INDEX.getJsonPath(5)).type(CONTAINS).expected(USER_EMAIL_DOMAIN).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testGetUserByIdValidAndInvalid(Quest quest) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, USER_NINE_ID),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_FIRST_NAME.getJsonPath()).type(IS).expected(USER_NINE_FIRST_NAME).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL.getJsonPath()).type(CONTAINS).expected(USER_EMAIL_DOMAIN).build()
                )
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
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
                        Assertion.builder().target(BODY).key(CREATED_USER_TIMESTAMP.getJsonPath()).type(MATCHES_REGEX).expected(USER_TIMESTAMP_REGEX).build()
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
                        Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(MATCHES_REGEX).expected(TOKEN_REGEX).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testGetNonExistentUserReturns404AndEmptyBody(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build(),
                        Assertion.builder().target(BODY).key(ROOT.getJsonPath()).type(IS).expected(EMPTY_JSON).build()
                )
                .complete();
    }

    @Test
    @Smoke
    @Regression
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testCreateAndDeleteLeaderUser(Quest quest, @Craft(model = USER_LEADER) User userLeader) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
                )
                .request(
                        DELETE_USER.withPathParam(
                                ID_PARAM,
                                retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString(CREATED_USER_ID.getJsonPath())
                        )
                )
                .complete();
    }

    @Test
    @Smoke
    @Regression
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testFullUserLifecycleAndCrossValidation(
            Quest quest,
            @Craft(model = USER_LEADER) User userLeader,
            @Craft(model = USER_JUNIOR) Late<User> userJunior
    ) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
                )
                .requestAndValidate(
                        POST_CREATE_USER,
                        userJunior.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).soft(true).build()
                )

                .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))

                .validate(() -> {
                    List<String> emails = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                            .getBody()
                            .jsonPath()
                            .getList(USER_EMAIL_BY_INDEX.getJsonPath().replace("[%d]", ""), String.class);

                    assertTrue(
                            emails.stream().noneMatch(email -> email.contains(USER_SENIOR_NAME.toLowerCase())),
                            EMAIL_FOUND_IN_LIST_UNEXPECTED
                    );
                })

                .request(
                        DELETE_USER.withPathParam(
                                ID_PARAM,
                                retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                        .getBody().jsonPath().getString(CREATED_USER_ID.getJsonPath())
                        )
                )
                .complete();
    }

}
