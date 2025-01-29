package com.example.project;


import com.example.project.base.World;
import com.example.project.db.Queries;
import com.example.project.model.Student;
import com.example.project.rest.authentication.AdminAuth;
import com.example.project.rest.authentication.PortalAuthentication;
import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.api.storage.DataExtractorsApi;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import com.theairebellion.zeus.validator.core.Assertion;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.example.project.rest.Endpoints.ENDPOINT_EXAMPLE;
import static com.theairebellion.zeus.api.storage.DataExtractorsApi.*;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.NUMBER_ROWS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.NOT_NULL;

// @UI
@API
@DB
public class NewTest extends BaseTest {


    @Test
    @AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    @InterceptRequests(requestUrlSubStrings = {"api/create-campaign", "upload"})
    public void scenario_some(Quest quest, @Craft(model = VALID_STUDENT) Student student,
                              @Craft(model = VALID_STUDENT) Late<Student> student1) {
        quest
            .enters(World.OLYMPYS)
            .request(ENDPOINT_EXAMPLE.withPathParam("campaignId", 17).withQueryParam("page", 1), student)
            .validateResponse(
                retrieve(ENDPOINT_EXAMPLE, Response.class),
                Assertion.builder(Integer.class).target(STATUS).type(IS).expected(200).build(),
                Assertion.builder(Long.class).target(BODY).key("id").type(NOT_NULL).build(),
                Assertion.builder(String.class).target(BODY).key("list").type(CONTAINS).expected("Ssfsdf").build())
            .then()
            .enters(World.UNDERWORLD)
            .query(Queries.EXAMPLE.withParam("id",
                retrieve(responseBodyExtraction(ENDPOINT_EXAMPLE, "$.id"), Long.class)))
            .validate(retrieve(Queries.EXAMPLE, QueryResponse.class),
                Assertion.builder(Integer.class).target(NUMBER_ROWS).type(IS).expected(3).soft(true)
                    .build())
            .then()
            .enters(World.EARTH)
            //.input().insert(InputFields.USERNAME_FIELD, student1.join().getName())
            //.input().insert(InputFields.PASSWORD_FIELD, student.getName())
            .input().insert(InputFields.PASSWORD_FIELD,
                String.valueOf(
                    retrieve(DataExtractorsUi.responseBodyExtraction("api/create-campaign", "$.id"), Long.class)))
            .then()
            .enters(World.FORGE)
            .complete();
    }

}
