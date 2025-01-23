package com.reqres.test.framework;

import com.reqres.test.framework.rest.dto.LoginUser;
import com.reqres.test.framework.rest.dto.User;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.LOGIN_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.NOT;

@API
public class ReqresApiTest extends BaseTest {

    @Test
    public void testGetAllUsers(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_ALL_USERS.withQueryParam("page", 2),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder(Integer.class).target(BODY).key("page").type(IS).expected(2).build(),
                        Assertion.builder(Integer.class).target(BODY).key("data[0].id").type(IS).expected(7).build(),
                        Assertion.builder(String.class).target(BODY).key("data[1].email").type(IS).expected("lindsay.ferguson@reqres.in").build()
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
    public void testCreateUser(Quest quest, @Craft(model = USER_LEADER) User user) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        user,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(201).build(),
                        Assertion.builder(String.class).target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build()
                ).complete();
    }

    @Test
    public void testLoginUser(Quest quest, @Craft(model = LOGIN_ADMIN_USER) LoginUser loginUser) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        LOGIN_USER,
                        loginUser,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(200).build()
                ).complete();
    }
}
