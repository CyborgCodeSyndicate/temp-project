package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.DATA;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.PER_PAGE;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_TEXT;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.SUPPORT_URL;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOTAL;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOTAL_PAGES;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.USER_AVATAR_BY_INDEX;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.USER_FIRST_NAME;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.USER_ID;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.reqres.test.framework.rest.Endpoints.GET_USER;
import static com.reqres.test.framework.rest.Endpoints.POST_LOGIN_USER;
import static com.reqres.test.framework.utils.Headers.SPECIFIC_HEADER;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_CONTAINS_ANY_USER;
import static com.reqres.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_DATA_SIZE;
import static com.reqres.test.framework.utils.TestConstants.PageTwo.PAGE_TWO_EXPECTED_USERS;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Pagination.TOTAL_USERS_IN_PAGE_RANGE;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_PREFIX;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_URL_REGEX;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_URL_REQRES_FRAGMENT;
import static com.reqres.test.framework.utils.TestConstants.Users.ID_THREE;
import static com.reqres.test.framework.utils.TestConstants.Users.USER_ONE_FIRST_NAME;
import static com.reqres.test.framework.utils.TestConstants.Users.USER_SEVENTH_FIRST_NAME_LENGTH;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.HEADER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.ALL_NOT_NULL;
import static com.theairebellion.zeus.validator.core.AssertionTypes.BETWEEN;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS_ALL;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS_ANY;
import static com.theairebellion.zeus.validator.core.AssertionTypes.ENDS_WITH;
import static com.theairebellion.zeus.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;
import static com.theairebellion.zeus.validator.core.AssertionTypes.GREATER_THAN;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.LENGTH;
import static com.theairebellion.zeus.validator.core.AssertionTypes.LESS_THAN;
import static com.theairebellion.zeus.validator.core.AssertionTypes.MATCHES_REGEX;
import static com.theairebellion.zeus.validator.core.AssertionTypes.NOT;
import static com.theairebellion.zeus.validator.core.AssertionTypes.NOT_EMPTY;
import static com.theairebellion.zeus.validator.core.AssertionTypes.NOT_NULL;
import static com.theairebellion.zeus.validator.core.AssertionTypes.STARTS_WITH;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

@TestService("Rivendell")
public class CustomService extends FluentService {

   public CustomService loginUserAndAddSpecificHeader(LoginUser loginUser) {
      quest.enters(OLYMPYS)
            .request(POST_LOGIN_USER, loginUser)
            .requestAndValidate(
                  GET_USER
                        .withPathParam(ID_PARAM, ID_THREE)
                        .withHeader(SPECIFIC_HEADER, quest.getStorage().sub(StorageKeysApi.API).get(POST_LOGIN_USER, Response.class)
                              .getBody()
                              .jsonPath()
                              .getString(TOKEN.getJsonPath())),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );
      return this;
   }

   public CustomService requestAndValidateGetAllUsers() {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam("page", PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(ContentType.JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(NOT).expected(1).build(),
                  Assertion.builder().target(BODY).key(TOTAL_PAGES.getJsonPath()).type(GREATER_THAN).expected(1).build(),
                  Assertion.builder().target(BODY).key(PER_PAGE.getJsonPath()).type(LESS_THAN).expected(10).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(CONTAINS).expected(SUPPORT_URL_REQRES_FRAGMENT).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(STARTS_WITH).expected(SUPPORT_TEXT_PREFIX).build(),
                  Assertion.builder().target(BODY).key(USER_AVATAR_BY_INDEX.getJsonPath(0)).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                  Assertion.builder().target(BODY).key(USER_ID.getJsonPath(0)).type(NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(ALL_NOT_NULL).expected(true).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(NOT_EMPTY).expected(true).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(LENGTH).expected(USER_SEVENTH_FIRST_NAME_LENGTH).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(LENGTH).expected(PAGE_TWO_DATA_SIZE).build(),
                  Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(MATCHES_REGEX).expected(SUPPORT_URL_REGEX).build(),
                  Assertion.builder().target(BODY).key(USER_FIRST_NAME.getJsonPath(0)).type(EQUALS_IGNORE_CASE).expected(USER_ONE_FIRST_NAME).build(),
                  Assertion.builder().target(BODY).key(TOTAL.getJsonPath()).type(BETWEEN).expected(TOTAL_USERS_IN_PAGE_RANGE).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ALL).expected(PAGE_TWO_EXPECTED_USERS).build(),
                  Assertion.builder().target(BODY).key(DATA.getJsonPath()).type(CONTAINS_ANY).expected(PAGE_TWO_CONTAINS_ANY_USER).build()
            );
      return this;
   }

}
