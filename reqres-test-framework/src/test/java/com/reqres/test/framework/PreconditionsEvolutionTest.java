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
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigCache;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.cleaner.TestDataCleaner.DELETE_ADMIN_USER;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_INTERMEDIATE;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER;
import static com.reqres.test.framework.preconditions.QuestPreconditions.CREATE_NEW_USER;
import static com.reqres.test.framework.rest.Endpoints.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@API
public class PreconditionsEvolutionTest extends BaseTestSequential {

    @Test
    public void testPreconditionsBasic(Quest quest) {
        final TestData testData = ConfigCache.getOrCreate(TestData.class);
        final String username = testData.username();
        final String password = testData.password();

        quest.enters(OLYMPYS)
                .request(
                        LOGIN_USER,
                        new LoginUser(username, password)
                );

        String token = retrieve(StorageKeysApi.API, LOGIN_USER, Response.class)
                .getBody()
                .jsonPath()
                .getString("token");

        User userLeader = User.builder()
                .name("Morpheus")
                .job("Leader")
                .build();

        User userIntermediate = User.builder()
                .name("Mr. " + userLeader.getName())
                .job("Intermediate " + userLeader.getJob())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER.withHeader("Authorization", "Bearer " + token),
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build())
                .requestAndValidate(
                        CREATE_USER.withHeader("Authorization", "Bearer " + token),
                        userIntermediate,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build())
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                })
                .requestAndValidate(
                        DELETE_USER
                                .withPathParam("id", 2)
                                .withHeader("Authorization", "Bearer " + token),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                );
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    public void testPreconditionsImprovedAuthentication(Quest quest) {
        User userLeader = User.builder()
                .name("Morpheus")
                .job("Leader")
                .build();

        User userIntermediate = User.builder()
                .name("Mr. " + userLeader.getName())
                .job("Intermediate " + userLeader.getJob())
                .build();

        quest.enters(OLYMPYS)
                .requestAndValidate(
                        CREATE_USER,
                        userLeader,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build())
                .requestAndValidate(
                        CREATE_USER,
                        userIntermediate,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build())
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                })
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                );
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    public void testPreconditionsImprovedAuthenticationAndPreQuest(Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                })
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", 2),
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                );
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testPreconditionsImprovedAuthenticationPreQuestAndRipper(Quest quest) {
        quest.enters(OLYMPYS)
                .validate(() -> {
                    CreatedUserResponse createdUserResponse = retrieve(StorageKeysApi.API, CREATE_USER, Response.class)
                            .getBody()
                            .as(CreatedUserResponse.class);
                    assertEquals("Mr. Morpheus", createdUserResponse.getName(), "Name is incorrect!");
                    assertEquals("Intermediate Leader", createdUserResponse.getJob(), "Job is incorrect!");
                    assertTrue(createdUserResponse
                            .getCreatedAt()
                            .contains(Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)), "CreatedAt date is incorrect!");
                });
    }

    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = ReqResAuthentication.class)
    @PreQuest({
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_INTERMEDIATE)}, order = 2),
            @Journey(value = CREATE_NEW_USER, journeyData = {@JourneyData(USER_LEADER)}, order = 1)
    })
    @Ripper(targets = {DELETE_ADMIN_USER})
    public void testPreconditionsCustomService(Quest quest) {
        quest.enters(GONDOR)
                .validateCreatedUser();
    }

}
