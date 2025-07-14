package com.reqres.test.framework;

import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_SENIOR;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_LEADER_JOB;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_LEADER_NAME;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_SENIOR_JOB;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_SENIOR_NAME;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_CREATED;

@API
public class CreateTwoUsersEvolutionTest extends BaseTestSequential {

   @Test
   @Regression
   public void testCreateTwoUsersBasic(Quest quest) {
      User userLeader = User.builder()
            .name(USER_LEADER_NAME)
            .job(USER_LEADER_JOB)
            .build();

      User userSenior = User.builder()
            .name(USER_SENIOR_NAME)
            .job(USER_SENIOR_JOB)
            .build();

      quest.enters(OLYMPYS)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  userSenior,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateTwoUsersImproved(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_LEADER_JOB).soft(true).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  userSenior.join(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_NAME).soft(true).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_SENIOR_JOB).soft(true).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateTwoUsersImprovedWithCustomService(Quest quest, @Craft(model = USER_LEADER) User userLeader, @Craft(model = USER_SENIOR) Late<User> userSenior) {
      quest.enters(GONDOR)
            .createLeaderUserAndValidateResponse(userLeader)
            .createSeniorUserAndValidateResponse(userSenior.join())
            .complete();
   }

}
