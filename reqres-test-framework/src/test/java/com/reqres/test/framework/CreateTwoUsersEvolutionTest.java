package com.reqres.test.framework;

import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_SENIOR;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class CreateTwoUsersEvolutionTest extends BaseTestSequential {

    @Test
    @Regression
    public void testCreateTwoUsersBasic(Quest quest) {
        User userLeader = User.builder()
                .name("Morpheus")
                .job("Leader")
                .build();

        User userSenior = User.builder()
                .name("Mr. " + userLeader.getName())
                .job("Senior " + userLeader.getJob())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Leader").soft(true).build())
                .requestAndValidate(
                        POST_CREATE_USER,
                        userSenior,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                        Assertion.builder().target(BODY).key("name").type(IS).expected("Mr. Morpheus").soft(true).build(),
                        Assertion.builder().target(BODY).key("job").type(IS).expected("Senior Leader").soft(true).build()
                );
    }

    @Test
    @Regression
    public void testCreateTwoUsersImproved(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
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
    public void testCreateTwoUsersImprovedWithCustomService(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
        quest.enters(GONDOR)
                .createLeaderUserAndValidateResponse(userLeader)
                .createSeniorUserAndValidateResponse(userSenior.join());
    }

}


