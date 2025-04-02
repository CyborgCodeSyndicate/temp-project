package com.reqres.test.framework;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.LOGIN_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.Helpers.EMPTY_JSON;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.Login.TOKEN_REGEX;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_ONE;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static com.reqres.test.framework.utils.TestConstants.Users.*;
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

}
