package com.reqres.test.framework;

import com.reqres.test.framework.rest.authentication.AdminAuth;
import com.reqres.test.framework.rest.authentication.ReqResAuthentication;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.CreatedUserResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_INTERMEDIATE;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.preconditions.QuestPreconditions.Data.CREATE_NEW_USER;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.*;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.reqres.test.framework.utils.AssertionMessages.*;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.TestConstants.FileConstants.AVATAR_FILE_EXTENSION;
import static com.reqres.test.framework.utils.TestConstants.Roles.*;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_TEXT_CADDY_FULL;
import static com.reqres.test.framework.utils.TestConstants.Support.SUPPORT_URL_VALUE;
import static com.reqres.test.framework.utils.TestConstants.Users.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.ENDS_WITH;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class RoAGeneratedTest extends BaseTest {

    @Test
    @Regression
    public void shouldReturnUserThreeWithCorrectFieldsAndSupportInfo(final Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_USER.withPathParam(ID_PARAM, ID_THREE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_FIRST_NAME.getJsonPath()).type(IS).expected(USER_THREE_FIRST_NAME).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_AVATAR.getJsonPath()).type(ENDS_WITH).expected(AVATAR_FILE_EXTENSION).build(),
                        Assertion.builder().target(BODY).key(SINGLE_USER_AVATAR.getJsonPath()).type(IS).expected(USER_THREE_AVATAR).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_URL.getJsonPath()).type(IS).expected(SUPPORT_URL_VALUE).build(),
                        Assertion.builder().target(BODY).key(SUPPORT_TEXT.getJsonPath()).type(IS).expected(SUPPORT_TEXT_CADDY_FULL).build()
                )
                .complete();
    }

    @Test
    @Regression
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1),
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void shouldCreateIntermediateUserWithCorrectDetails(final Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdIntermediateUser = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);

                    assertEquals(USER_INTERMEDIATE_NAME, createdIntermediateUser.getName(), NAME_INCORRECT);
                    assertEquals(USER_INTERMEDIATE_JOB, createdIntermediateUser.getJob(), JOB_INCORRECT);
                    assertTrue(createdIntermediateUser.getCreatedAt().matches(USER_TIMESTAMP_REGEX), CREATED_AT_INCORRECT);
                })
                .complete();
    }

    @Test
    @Smoke
    @Regression
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void shouldCreateAndDeleteLeaderUserSuccessfully(
            final Quest quest,
            final @Craft(model = USER_LEADER) Late<User> userLeader) {

        quest.enters(GONDOR)
                .createLeaderUserAndValidateResponse(userLeader.join())
                .then()
                .enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdLeader = retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);

                    assertEquals(USER_LEADER_NAME, createdLeader.getName(), NAME_INCORRECT);
                    assertEquals(USER_LEADER_JOB, createdLeader.getJob(), JOB_INCORRECT);
                })
                .requestAndValidate(
                        DELETE_USER.withPathParam(ID_PARAM, retrieve(StorageKeysApi.API, POST_CREATE_USER, Response.class)
                                .getBody()
                                .as(CreatedUserResponse.class)
                                .getId()),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                )
                .complete();
    }

}
