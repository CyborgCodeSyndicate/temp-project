package com.reqres.test.framework;

import com.reqres.test.framework.data.test.TestData;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.ERROR;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static com.reqres.test.framework.rest.Endpoints.POST_LOGIN_USER;
import static com.reqres.test.framework.utils.TestConstants.Login.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

@API
public class LoginFunctionalityAIGeneratedTest extends BaseTest {

    private static final TestData testData = ConfigCache.getOrCreate(TestData.class);

    @Test
    @Regression
    public void testSuccessfulLogin(Quest quest) {
        LoginUser validUser = LoginUser.builder()
                .email(testData.username())
                .password(testData.password())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_LOGIN_USER,
                        validUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key(TOKEN.getJsonPath()).type(MATCHES_REGEX).expected(TOKEN_REGEX).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginMissingPassword(Quest quest) {
        LoginUser noPasswordUser = LoginUser.builder()
                .email(testData.username())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_LOGIN_USER,
                        noPasswordUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(MISSING_PASSWORD_ERROR).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginMissingEmail(Quest quest) {
        LoginUser noEmailUser = LoginUser.builder()
                .password(testData.password())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_LOGIN_USER,
                        noEmailUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(MISSING_EMAIL_ERROR).build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginWithInvalidEmail(Quest quest) {
        LoginUser invalidEmailUser = LoginUser.builder()
                .email(INVALID_EMAIL)
                .password(testData.password())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_LOGIN_USER,
                        invalidEmailUser,
                        Assertion.builder().target(STATUS).type(IS).expected(SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key(ERROR.getJsonPath()).type(IS).expected(USER_NOT_FOUND_ERROR).build()
                )
                .complete();
    }

}
