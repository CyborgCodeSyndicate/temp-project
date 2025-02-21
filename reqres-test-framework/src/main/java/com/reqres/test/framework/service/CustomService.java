package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.ApiResponsesJsonPaths;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;

@TestService("Rivendell")
public class CustomService extends FluentService {

    public CustomService loginUserAndAddSpecificHeader(LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .request(LOGIN_USER, loginUser)
                .requestAndValidate(
                        GET_USER
                                .withPathParam("id", 3)
                                .withHeader("SpecificHeader", quest.getStorage().sub(StorageKeysApi.API).get(LOGIN_USER, Response.class)
                                        .getBody()
                                        .jsonPath()
                                        .getString("token")),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build()
                );
        return this;
    }

    public RestServiceFluent requestAndValidateGetAllUsers() {
        return quest.enters(OLYMPYS)
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
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.USER_ID.getJsonPath(0)).type(NOT_NULL).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(ALL_NOT_NULL).build(),
                        Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.DATA.getJsonPath()).type(NOT_EMPTY).build(),
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

}
