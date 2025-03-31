package com.reqres.test.framework;

import com.reqres.test.framework.data.test.TestData;
import com.reqres.test.framework.rest.Endpoints;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.aeonbits.owner.ConfigCache;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;

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
                        Endpoints.POST_LOGIN_USER,
                        validUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder().target(BODY).key("token").type(NOT_NULL).expected(true).build(),
                        Assertion.builder().target(BODY).key("token").type(MATCHES_REGEX).expected("[a-zA-Z0-9]+").build()
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
                        Endpoints.POST_LOGIN_USER,
                        noPasswordUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key("error").type(IS).expected("Missing password").build()
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
                        Endpoints.POST_LOGIN_USER,
                        noEmailUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key("error").type(IS).expected("Missing email or username").build()
                )
                .complete();
    }

    @Test
    @Regression
    public void testLoginWithInvalidEmail(Quest quest) {
        LoginUser invalidEmailUser = LoginUser.builder()
                .email("wrong.email@reqres.in")
                .password(testData.password())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        Endpoints.POST_LOGIN_USER,
                        invalidEmailUser,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_BAD_REQUEST).build(),
                        Assertion.builder().target(BODY).key("error").type(IS).expected("user not found").build()
                )
                .complete();
    }
}
