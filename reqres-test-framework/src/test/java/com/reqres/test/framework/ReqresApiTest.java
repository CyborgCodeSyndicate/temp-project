package com.reqres.test.framework;

import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.reqres.test.framework.rest.dto.response.DataResponse;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.reqres.test.framework.rest.dto.response.UserResponse;
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

import java.time.Instant;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.base.World.RIVENDELL;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.preconditions.QuestPreconditions.Data;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.AssertionMessages.*;
import static com.reqres.test.framework.utils.Headers.SPECIFIC_HEADER;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.PageTwo.*;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Pagination.TOTAL_USERS_IN_PAGE_RANGE;
import static com.reqres.test.framework.utils.TestConstants.Roles.*;
import static com.reqres.test.framework.utils.TestConstants.Support.*;
import static com.reqres.test.framework.utils.TestConstants.Users.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static io.restassured.http.ContentType.JSON;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class ReqresApiTest extends BaseTest {

    @Test
    @Regression
    public void testGetAllUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(NOT).expected(1).build(),
                        Assertion.builder().target(BODY).key(TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
                        Assertion.builder().target(BODY).key(PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(CONTAINS).expected(SUPPORT_URL_REQRES_FRAGMENT).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected(SUPPORT_TEXT_PREFIX).build(),
                        Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                        Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(7).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(LENGTH).expected(6).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected(SUPPORT_URL_REGEX).build(),
                        Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected(USER_ONE_FIRST_NAME).build(),
                        Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(BETWEEN).expected(TOTAL_USERS_IN_PAGE_RANGE).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ALL).expected(PAGE_TWO_EXPECTED_USERS).build(),
                        Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ANY).expected(PAGE_TWO_CONTAINS_ANY_USER).build())
                .complete();
    }

    @Test
    @Regression
    public void testGetUser(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, ID_THREE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).soft(true).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_EMAIL_EXPLICIT.getJsonPath()).type(IS).expected(USER_THREE_EMAIL).soft(true).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_URL_EXPLICIT.getJsonPath()).type(IS).expected(SUPPORT_URL_VALUE).soft(true).build()
                );
    }

    @Test
    @Regression
    public void testUserNotFound(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, INVALID_USER_ID),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NOT_FOUND).build()
                );
    }

    @Test
    @Regression
    public void testGetUsersJUnitAssertions(Quest quest) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
                .validate(() -> {
                    GetUsersResponse usersResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class);
                    assertEquals(PAGE_TWO_DATA_SIZE, usersResponse.getData().size(), USER_DATA_SIZE_INCORRECT);
                    assertEquals(USER_SEVENTH_FIRST_NAME_LENGTH, usersResponse.getData().get(0).getFirstName().length(), FIRST_NAME_LENGTH_INCORRECT);
                });
    }

    @Test
    @Regression
    public void testGetUserFromListOfUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class).getData().get(0).getId()),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
                );
    }

    @Test
    @Regression
    public void testGetUserFromListOfUsersByName(Quest quest) {
        quest.enters(OLYMPYS)
                .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO))
                .request(GET_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                        .getBody()
                        .as(GetUsersResponse.class)
                        .getData()
                        .stream()
                        .filter(user -> USER_NINE_FIRST_NAME.equals(user.getFirstName()))
                        .map(DataResponse::getId)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException(userWithFirstNameNotFound(USER_NINE_FIRST_NAME)))))
                .validate(softAssertions -> {
                    UserResponse userResponse = retrieve(StorageKeysApi.API, GET_USER, Response.class).getBody().as(UserResponse.class);
                    softAssertions.assertThat(userResponse.getData().getId()).isEqualTo(USER_NINE_ID);
                    softAssertions.assertThat(userResponse.getData().getEmail()).isEqualTo(USER_NINE_EMAIL);
                    softAssertions.assertThat(userResponse.getData().getFirstName()).isEqualTo(USER_NINE_FIRST_NAME);
                    softAssertions.assertThat(userResponse.getData().getLastName()).isEqualTo(USER_NINE_LAST_NAME);
                }).complete();
    }

    @Test
    @Regression
    public void testCreateUser(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        user,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build()
                );
    }

    @Test
    @Regression
    public void testCreateJuniorUser(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build())
                .requestAndValidate(POST_CREATE_USER,
                        user.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
                );
    }

    @Test
    @Regression
    public void testCreateTwoUsers(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
        quest.enters(OLYMPYS)
                .requestAndValidate(POST_CREATE_USER, userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build())
                .requestAndValidate(POST_CREATE_USER, userSenior.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                        Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
                );
    }

    @Test
    @Regression
    public void testLoginUserAndAddHeader(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .request(POST_LOGIN_USER, loginUser)
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, ID_THREE)
                                .withHeader(SPECIFIC_HEADER, retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
                                        .getBody().jsonPath().getString(TOKEN.getJsonPath())),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
                );
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = Data.CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
            @Journey(value = Data.CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    @Regression
    public void testUserLifecycle(Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                            .getBody().as(CreatedUserResponse.class);
                    assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName());
                    assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob());
                    assertTrue(createdUserResponse.getCreatedAt().contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)));
                });
    }

    @Test
    @Regression
    public void testCustomService(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(RIVENDELL)
                .loginUserAndAddSpecificHeader(loginUser)
                .then()
                .enters(OLYMPYS)
                .requestAndValidate(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build());
    }

    @Test
    @Regression
    public void testValidateAllUsers(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(RIVENDELL)
                .loginUserAndAddSpecificHeader(loginUser)
                .requestAndValidateGetAllUsers();
    }

}
