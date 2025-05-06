package com.reqres.test.framework;

import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.DataResponse;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_JUNIOR;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_JOB_RESPONSE;
import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.CREATE_USER_NAME_RESPONSE;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_JUNIOR_JOB;
import static com.reqres.test.framework.utils.TestConstants.Roles.USER_JUNIOR_NAME;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.HEADER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@API
public class CreateUserEvolutionTest extends BaseTest {

   @Test
   @Regression
   public void testCreateJuniorUserBasic(Quest quest) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            );

      DataResponse dataResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersResponse.class)
            .getData().get(0);

      User userJunior = User.builder()
            .name(dataResponse.getFirstName() + " suffix")
            .job("Junior " + dataResponse.getLastName() + " worker")
            .build();

      quest.enters(OLYMPYS)
            .requestAndValidate(
                  POST_CREATE_USER,
                  userJunior,
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateJuniorUserImproved(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_OK).build()
            )
            .requestAndValidate(
                  POST_CREATE_USER,
                  user.join(),
                  Assertion.builder().target(STATUS).type(IS).expected(SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(CONTENT_TYPE).type(CONTAINS).expected(JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_NAME_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_NAME).build(),
                  Assertion.builder().target(BODY).key(CREATE_USER_JOB_RESPONSE.getJsonPath()).type(IS).expected(USER_JUNIOR_JOB).build()
            )
            .complete();
   }

   @Test
   @Regression
   public void testCreateJuniorUserImprovedWithCustomService(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
      quest.enters(GONDOR)
            .getAllUsersAndValidateResponse()
            .createJuniorUserAndValidateResponse(user.join())
            .complete();
   }

}
