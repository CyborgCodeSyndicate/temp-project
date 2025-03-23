package com.reqres.test.framework;

import com.reqres.test.framework.rest.ApiResponsesJsonPaths;
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
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.base.World.RIVENDELL;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.preconditions.QuestPreconditions.CREATE_NEW_USER;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class ReqresApiTest extends BaseTest {

    @Test
    @Regression
    public void testGetAllUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(HEADER).key(HttpHeaders.CONTENT_TYPE).type(CONTAINS).expected(ContentType.JSON.toString()).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.TOTAL.getJsonPath()).type(NOT).expected(1).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.SUPPORT_URL.getJsonPath()).type(CONTAINS).expected("reqres").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected("Tired of writing").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(".jpg").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(7).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(LENGTH).expected(6).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected("https:\\/\\/contentcaddy\\.io\\?utm_source=reqres&utm_medium=json&utm_campaign=referral").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected("michael").build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.TOTAL.getJsonPath()).type(BETWEEN).expected(List.of(5, 15)).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(CONTAINS_ALL).expected(List.of(
                                Map.of("id", 7, "email", "michael.lawson@reqres.in", "first_name", "Michael", "last_name", "Lawson", "avatar", "https://reqres.in/img/faces/7-image.jpg"),
                                Map.of("id", 8, "email", "lindsay.ferguson@reqres.in", "first_name", "Lindsay", "last_name", "Ferguson", "avatar", "https://reqres.in/img/faces/8-image.jpg"),
                                Map.of("id", 9, "email", "tobias.funke@reqres.in", "first_name", "Tobias", "last_name", "Funke", "avatar", "https://reqres.in/img/faces/9-image.jpg"),
                                Map.of("id", 10, "email", "byron.fields@reqres.in", "first_name", "Byron", "last_name", "Fields", "avatar", "https://reqres.in/img/faces/10-image.jpg"),
                                Map.of("id", 11, "email", "george.edwards@reqres.in", "first_name", "George", "last_name", "Edwards", "avatar", "https://reqres.in/img/faces/11-image.jpg"),
                                Map.of("id", 12, "email", "rachel.howell@reqres.in", "first_name", "Rachel", "last_name", "Howell", "avatar", "https://reqres.in/img/faces/12-image.jpg")
                        )).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(CONTAINS_ANY).expected(List.of(
                                Map.of("id", 7, "email", "michael.lawson@reqres.in", "first_name", "Michael", "last_name", "Lawson", "avatar", "https://reqres.in/img/faces/7-image.jpg"),
                                Map.of("id", 22, "email", "invalid.user", "first_name", "Invalid", "last_name", "User", "avatar", "invalidUrls")
                        )).build()
                );
    }

    @Test
    @Regression
    public void testGetUser(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam("id", 3),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).soft(true).build(),
                        Assertion.builder().target(BODY).key("data.email").type(IS).expected("emma.wong@reqres.in").soft(true).build(),
                        Assertion.builder().target(BODY).key("support.url").type(IS).expected("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral").soft(true).build()
                );
    }

    @Test
    @Regression
    public void testUserNotFound(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam("id", 23),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_NOT_FOUND).build()
                );
    }

    @Test
    @Regression
    public void testGetUsersJUnitAssertions(Quest quest) {
        quest.enters(OLYMPYS)
                .request(
                        GET_ALL_USERS.withQueryParam("page", 2))
                .validate(() -> {
                    GetUsersResponse usersResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class);
                    assertEquals(6, usersResponse.getData().size(), "User data size not correct!");
                    assertEquals(7, usersResponse.getData().get(0).getFirstName().length(), "Name length incorrect!");
                });
    }

    @Test
    @Regression
    public void testGetUserFromListOfUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .request(
                        GET_ALL_USERS.withQueryParam("page", 2))
                .requestAndValidate(
                        GET_USER.withPathParam("id", retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class).getData().get(0).getId()),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    @Test
    @Regression
    public void testGetUserFromListOfUsersByName(Quest quest) {
        final String targetFirstName = "Tobias";
        quest.enters(OLYMPYS)
                .request(
                        GET_ALL_USERS.withQueryParam("page", 2))
                .request(
                        GET_USER.withPathParam("id", retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                                .getBody()
                                .as(GetUsersResponse.class)
                                .getData()
                                .stream()
                                .filter(user -> targetFirstName.equals(user.getFirstName()))
                                .map(DataResponse::getId)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("User with first name " + targetFirstName + " not found"))))
                .validate(softAssertions -> {
                    UserResponse userResponse = retrieve(StorageKeysApi.API, GET_USER, Response.class)
                            .getBody()
                            .as(UserResponse.class);
                    softAssertions.assertThat(userResponse.getData().getId()).isEqualTo(9);
                    softAssertions.assertThat(userResponse.getData().getEmail()).isEqualTo("tobias.funke@reqres.in");
                    softAssertions.assertThat(userResponse.getData().getFirstName()).isEqualTo("Tobias");
                    softAssertions.assertThat(userResponse.getData().getLastName()).isEqualTo("Funke");
                }).complete();
    }

    @Test
    @Regression
    public void testCreateUser(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        user,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build()
                );
    }

    @Test
    @Regression
    public void testCreateJuniorUser(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
                .requestAndValidate(
                        POST_CREATE_USER,
                        user.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build()
                );
    }

    @Test
    @Regression
    public void testCreateTwoUsers(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Leader").soft(true).build())
                .requestAndValidate(
                        POST_CREATE_USER,
                        userSenior.join(),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Mr. Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Senior Leader").soft(true).build()
                );
    }

    @Test
    @Regression
    public void testLoginUserAndAddHeader(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .request(POST_LOGIN_USER, loginUser)
                .requestAndValidate(
                        GET_USER
                                .withPathParam("id", 3)
                                .withHeader("SpecificHeader", retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString("token")),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    @Regression
    public void testUserLifecycle(Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                });
    }

    @Test
    @Regression
    public void testCustomService(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(RIVENDELL)
                .loginUserAndAddSpecificHeader(loginUser)
                .then()
                .enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    @Test
    @Regression
    public void testValidateAllUsers(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(RIVENDELL)
                .loginUserAndAddSpecificHeader(loginUser)
                .requestAndValidateGetAllUsers();
    }

}
