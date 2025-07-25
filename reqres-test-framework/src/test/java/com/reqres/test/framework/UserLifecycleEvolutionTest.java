package com.reqres.test.framework;

import com.reqres.test.framework.data.test.TestData;
import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import java.time.Instant;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_INTERMEDIATE;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.preconditions.QuestPreconditions.Data.CREATE_NEW_USER;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static com.reqres.test.framework.rest.Endpoints.DELETE_USER;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.reqres.test.framework.rest.Endpoints.POST_LOGIN_USER;
import static com.reqres.test.framework.utils.AssertionMessages.CREATED_AT_INCORRECT;
import static com.reqres.test.framework.utils.AssertionMessages.JOB_INCORRECT;
import static com.reqres.test.framework.utils.AssertionMessages.NAME_INCORRECT;
import static com.reqres.test.framework.utils.Headers.AUTHORIZATION_HEADER_KEY;
import static com.reqres.test.framework.utils.Headers.AUTHORIZATION_HEADER_VALUE;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_JOB;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_INTERMEDIATE_NAME;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_LEADER_JOB;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_LEADER_NAME;
import static com.reqres.test.framework.utils.TestConstants.Users.ID_THREE;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class UserLifecycleEvolutionTest extends BaseTest {

   @Test
   @Regression
   public void testUserLifecycleBasic(Quest quest) {
      final TestData testData = ConfigCache.getOrCreate(TestData.class);
      final String username = testData.username();
      final String password = testData.password();

      quest.enters(OLYMPYS)
            .request(POST_LOGIN_USER, new LoginUser(username, password));

      String token = retrieve(StorageKeysApi.API, POST_LOGIN_USER, Response.class)
            .getBody().jsonPath().getString(TOKEN.getJsonPath());

      User userLeader = User.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      User userIntermediate = User.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.enters(OLYMPYS)
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER.withHeader(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token),
                  userIntermediate,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE).withHeader(AUTHORIZATION_HEADER_KEY,
                        AUTHORIZATION_HEADER_VALUE + token),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @Regression
   public void testUserLifecycleWithAuth(Quest quest) {
      User userLeader = User.builder().name(USER_LEADER_NAME).job(USER_LEADER_JOB).build();
      User userIntermediate = User.builder().name(USER_INTERMEDIATE_NAME).job(USER_INTERMEDIATE_JOB).build();

      quest.enters(OLYMPYS)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userLeader,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  userIntermediate,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build()
            )
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @PreQuest({
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
   })
   @Regression
   public void testUserLifecycleWithPreQuest(Quest quest) {
      quest.enters(OLYMPYS)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .requestAndValidate(
                  DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
            )
            .complete();
   }

   @Test
   @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @PreQuest({
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
   })
   @Ripper(targets = {DELETE_ADMIN_USER})
   @Regression
   public void testUserLifecycleWithRipper(Quest quest) {
      quest.enters(OLYMPYS)
            .validate(() -> {
               CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                     .getBody().as(CreatedUserResponse.class);
               assertEquals(USER_INTERMEDIATE_NAME, createdUserResponse.getName(), NAME_INCORRECT);
               assertEquals(USER_INTERMEDIATE_JOB, createdUserResponse.getJob(), JOB_INCORRECT);
               assertTrue(createdUserResponse.getCreatedAt()
                     .contains(Instant.now().atZone(UTC).format(ISO_LOCAL_DATE)), CREATED_AT_INCORRECT);
            })
            .complete();
   }

   @Test
   @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
   @PreQuest({
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
         @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
   })
   @Ripper(targets = {DELETE_ADMIN_USER})
   @Regression
   public void testUserLifecycleWithCustomService(Quest quest) {
      quest.enters(GONDOR)
            .validateCreatedUser()
            .complete();
   }

}
