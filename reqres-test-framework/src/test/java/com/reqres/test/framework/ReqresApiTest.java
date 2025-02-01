package com.reqres.test.framework;

import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.reqres.test.framework.rest.dto.response.UserResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.*;
import static com.reqres.test.framework.preconditions.QuestPreconditions.CREATE_NEW_LEADER_USER;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class ReqresApiTest extends BaseTest {

    @Test
    public void testGetAllUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder(String.class).target(HEADER).key("Content-Type").type(CONTAINS).expected("application/json").build(),
                        Assertion.builder(Integer.class).target(BODY).key("total").type(NOT).expected(1).build(),
                        Assertion.builder(Integer.class).target(BODY).key("total_pages").type(GREATER_THAN).expected(1).build(),
                        Assertion.builder(Integer.class).target(BODY).key("per_page").type(LESS_THAN).expected(10).build(),
                        Assertion.builder(String.class).target(BODY).key("support.url").type(CONTAINS).expected("reqres").build(),
                        Assertion.builder(String.class).target(BODY).key("support.text").type(STARTS_WITH).expected("Tired of writing").build(),
                        Assertion.builder(String.class).target(BODY).key("data[0].avatar").type(ENDS_WITH).expected(".jpg").build(),
                        Assertion.builder(Integer.class).target(BODY).key("data[0].id").type(NOT_NULL).build(),
                        Assertion.builder(List.class).target(BODY).key("data").type(ALL_NOT_NULL).build(),
                        Assertion.builder(List.class).target(BODY).key("data").type(NOT_EMPTY).build(),
                        // todo: check how the length works
                        // Assertion.builder(Integer.class).target(BODY).key("data[0].first_name").type(LENGTH).expected(7).build(),
                        // Assertion.builder(Integer.class).target(BODY).key("data").type(LENGTH).expected(6).build(),
                        Assertion.builder(String.class).target(BODY).key("support.url").type(MATCHES_REGEX).expected("https:\\/\\/contentcaddy\\.io\\?utm_source=reqres&utm_medium=json&utm_campaign=referral").build(),
                        Assertion.builder(String.class).target(BODY).key("data[0].first_name").type(EQUALS_IGNORE_CASE).expected("michael").build(),
                        Assertion.builder(List.class).target(BODY).key("total").type(BETWEEN).expected(List.of(5, 15)).build(),
                        Assertion.builder(List.class).target(BODY).key("data").type(CONTAINS_ALL).expected(List.of(
                                Map.of("id", 7, "email", "michael.lawson@reqres.in", "first_name", "Michael", "last_name", "Lawson", "avatar", "https://reqres.in/img/faces/7-image.jpg"),
                                Map.of("id", 8, "email", "lindsay.ferguson@reqres.in", "first_name", "Lindsay", "last_name", "Ferguson", "avatar", "https://reqres.in/img/faces/8-image.jpg"),
                                Map.of("id", 9, "email", "tobias.funke@reqres.in", "first_name", "Tobias", "last_name", "Funke", "avatar", "https://reqres.in/img/faces/9-image.jpg"),
                                Map.of("id", 10, "email", "byron.fields@reqres.in", "first_name", "Byron", "last_name", "Fields", "avatar", "https://reqres.in/img/faces/10-image.jpg"),
                                Map.of("id", 11, "email", "george.edwards@reqres.in", "first_name", "George", "last_name", "Edwards", "avatar", "https://reqres.in/img/faces/11-image.jpg"),
                                Map.of("id", 12, "email", "rachel.howell@reqres.in", "first_name", "Rachel", "last_name", "Howell", "avatar", "https://reqres.in/img/faces/12-image.jpg")
                        )).build(),
                        Assertion.builder(List.class).target(BODY).key("data").type(CONTAINS_ANY).expected(List.of(
                                Map.of("id", 7, "email", "michael.lawson@reqres.in", "first_name", "Michael", "last_name", "Lawson", "avatar", "https://reqres.in/img/faces/7-image.jpg"),
                                Map.of("id", 22, "email", "invalid.user", "first_name", "Invalid", "last_name", "User", "avatar", "invalidUrls")
                        )).build()
                ).complete();
    }

    @Test
    public void testGetUser(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam("id", 3),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("data.email").type(IS).expected("emma.wong@reqres.in").soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("support.url").type(IS).expected("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral").soft(true).build()
                ).complete();
    }

    @Test
    public void testUserNotFound(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam("id", 23),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_NOT_FOUND).build()
                ).complete();
    }

    @Test
    public void testGetUsersJUnitAssertions(Quest quest) {
        quest.enters(OLYMPYS)
                .request(
                        GET_ALL_USERS.withQueryParam("page", 2))
                .validate(() -> {
                    GetUsersResponse usersResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class);
                    assertEquals(6, usersResponse.getData().size(), "User data size not correct");
                    assertEquals(7, usersResponse.getData().get(0).getFirstName().length(), "Name length incorrect!");
                });
    }

    @Test
    public void testGetUserFromListOfUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .request(
                        GET_ALL_USERS.withQueryParam("page", 2))
                .requestAndValidate(
                        GET_USER.withPathParam("id", retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class).getBody().as(GetUsersResponse.class).getData().get(0).getId()),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    @Test
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
                                .map(UserResponse::getId)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("User with first name " + targetFirstName + " not found"))))
                .validate(softAssertions -> {
                    UserResponse userResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
                            .getBody()
                            .as(GetUsersResponse.class)
                            .getData()
                            .stream()
                            .filter(user -> targetFirstName.equals(user.getFirstName()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("User with first name " + targetFirstName + " not found"));
                    softAssertions.assertThat(userResponse.getId()).isEqualTo(9);
                    softAssertions.assertThat(userResponse.getEmail()).isEqualTo("tobias.funke@reqres.in");
                    softAssertions.assertThat(userResponse.getFirstName()).isEqualTo("Tobias");
                    softAssertions.assertThat(userResponse.getLastName()).isEqualTo("Funke");
                }).complete();
    }

    @Test
    public void testCreateUser(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        user,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder(String.class).target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build())
                .complete();
    }

    @Test
    public void testCreateUserWithSuffix(Quest quest, @Craft(model = USER_SUFFIX) Late<User> user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
                .requestAndValidate(
                        CREATE_USER,
                        user.join(),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build())
                .complete();
    }

    @Test
    public void testCreateTwoUsers(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_PREFIX) Late<User> userPrefix) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        userLeader,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder(String.class).target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("job").type(IS).expected("Leader").soft(true).build())
                .requestAndValidate(
                        CREATE_USER,
                        userPrefix.join(),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder(String.class).target(BODY).key("name").type(IS).expected("Mr. Morpheus").soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("job").type(IS).expected("Senior Leader").soft(true).build())
                .complete();
    }

    @Test
    public void testLoginUserAndAddHeader(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .request(LOGIN_USER, loginUser)
                .requestAndValidate(
                        GET_USER
                                .withPathParam("id", 3)
                                .withHeader("Authorization", "Bearer " + retrieve(StorageKeysApi.API, LOGIN_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString("token")),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
    }

    @Test
    @AuthenticateAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_LEADER_USER, journeyData = {@JourneyData(USER_LEADER)})
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testPreconditionsExample(Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, CREATE_USER, Response.class).getBody().as(CreatedUserResponse.class);
                    assertEquals("Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                });
    }
}
