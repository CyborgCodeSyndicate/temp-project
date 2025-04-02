package com.reqres.test.framework.service;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.Headers.SPECIFIC_HEADER;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.PageTwo.*;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Pagination.TOTAL_USERS_IN_PAGE_RANGE;
import static com.reqres.test.framework.utils.TestConstants.Support.*;
import static com.reqres.test.framework.utils.TestConstants.Users.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_OK;

@TestService("Rivendell")
@InfoAIClass(
        level = Level.LAST,
        description = "Provides reusable test logic for API interactions, including user authentication and validation of API responses. " +
                "This service is directly used in test cases to streamline common test operations."
)
public class CustomService extends FluentService {

    @InfoAI(description = "Logs in a user using the provided credentials and retrieves the authentication token. " +
            "The token is then used as a specific header in a request to fetch user details.")
    public CustomService loginUserAndAddSpecificHeader(@InfoAI(description = "The login credentials of the user, including email and password.") LoginUser loginUser) {
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

    @InfoAI(description = "Sends a request to retrieve all users from the API and validates the response. " +
            "The response is checked for correct HTTP status, content type, and multiple business rules including user count, pagination, avatar format, and specific user attributes.")
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
