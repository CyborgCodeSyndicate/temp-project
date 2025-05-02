package com.reqres.test.framework;

import com.reqres.test.framework.rest.ApiResponsesJsonPaths;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.DataResponse;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Regression;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.reqres.test.framework.base.World.GONDOR;
import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_JUNIOR;
import static com.reqres.test.framework.rest.Endpoints.CREATE_USER;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.HEADER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class CreateUserEvolutionTest extends BaseTestSequential {

   @Test
   @Regression
   public void testCreateJuniorUserBasic(Quest quest) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam("page", 2),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build());

      DataResponse dataResponse = retrieve(StorageKeysApi.API, GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersResponse.class)
            .getData().get(0);

      User userJunior = User.builder()
            .name(dataResponse.getFirstName() + " suffix")
            .job("Junior" + dataResponse.getLastName() + " worker")
            .build();

      quest.enters(OLYMPYS)
            .requestAndValidate(
                  CREATE_USER,
                  userJunior,
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(HttpHeaders.CONTENT_TYPE).type(CONTAINS)
                        .expected(ContentType.JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_NAME.getJsonPath()).type(IS)
                        .expected("Michael suffix").build(),
                  Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_JOB.getJsonPath()).type(IS)
                        .expected("JuniorLawson worker").build()
            );
   }

   @Test
   @Regression
   public void testCreateJuniorUserImproved(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  GET_ALL_USERS.withQueryParam("page", 2),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
            .requestAndValidate(
                  CREATE_USER,
                  user.join(),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build(),
                  Assertion.builder().target(HEADER).key(HttpHeaders.CONTENT_TYPE).type(CONTAINS)
                        .expected(ContentType.JSON.toString()).build(),
                  Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_NAME.getJsonPath()).type(IS)
                        .expected("Michael suffix").build(),
                  Assertion.builder().target(BODY).key(ApiResponsesJsonPaths.CREATE_USER_JOB.getJsonPath()).type(IS)
                        .expected("JuniorLawson worker").build()
            );
   }

   @Test
   @Regression
   public void testCreateJuniorUserImprovedWithCustomService(Quest quest, @Craft(model = USER_JUNIOR) Late<User> user) {
      quest.enters(GONDOR)
            .getAllUsersAndValidateResponse()
            .createJuniorUserAndValidateResponse(user.join());
   }
}
