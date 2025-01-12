package com.example.project;

import com.example.project.db.Queries;
import com.example.project.model.Student;
import com.example.project.rest.authentication.AdminAuth;
import com.example.project.rest.authentication.PortalAuthentication;
import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.validator.DbAssertionTarget;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.base.Services;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.service.facade.UIService;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import manifold.ext.rt.api.Jailbreak;
import org.junit.jupiter.api.Test;

import static com.example.project.base.World.EARTH;
import static com.example.project.base.World.FORGE;
import static com.example.project.base.World.OLYMPYS;
import static com.example.project.base.World.UNDERWORLD;
import static com.example.project.data.cleaner.TestDataCleaner.ALL_CREATED_STUDENTS;
import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.example.project.preconditions.QuestPreconditions.STUDENT_PRECONDITION;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.ui.storage.DataExtractorsUi.responseBodyExtraction;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS;

// @UI
@API
@DB
public class ExampleTest extends BaseTest {


    // @Test
    // @AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    // @InterceptRequests(requestUrlSubStrings = {"api/create-campaign", "upload"})
    // @Ripper(targets = {ALL_CREATED_STUDENTS})
    // public void testExample(Quest quest, @Craft(model = VALID_STUDENT) Student student1,
    //                         @Craft(model = VALID_STUDENT) Late<Student> student2) {
    //     quest
    //         .enters(OLYMPYS)
    //         .request(null, "{...}")
    //         .validateResponse(null,
    //             Assertion.builder(String.class).target(BODY).key("title").type(CONTAINS).expected("sunt").soft(true)
    //                 .build())
    //         .validate(softAssertions -> softAssertions.assertThat(1).isNull())
    //         .then()
    //         .enters(UNDERWORLD)
    //         .query(Queries.EXAMPLE, "dsadsa", String.class)
    //         .validate(retrieve(Queries.EXAMPLE, QueryResponse.class),
    //             Assertion.builder(Long.class).target(
    //             DbAssertionTarget.QUERY_RESULT).key("$.budget[student.name=viksa]").type(AssertionTypes.IS).expected(15L).soft(true).build())
    //         .then()
    //         .enters(FORGE)
    //         .somethingCustom()
    //         .then()
    //         .enters(EARTH)
    //         .insertion().insertData(student1)
    //         .input().clear(null)
    //         .input().insert(null, testData().username())
    //         .input().insert(null, student1.getName())
    //         .input().insert(null, student2.join().getName())
    //         .input()
    //         .insert(null, retrieve(responseBodyExtraction("create-campaing", "$.name"), String.class))
    //         .interceptor().validateResponseHaveStatus("api", 2)
    //         .complete();
    // }


    @Test
    @AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    @PreQuest({
        @Journey(value = STUDENT_PRECONDITION, journeyData = {@JourneyData(VALID_STUDENT)})
    })
    @Ripper(targets = {ALL_CREATED_STUDENTS})
    public void testExample1(Quest quest, @Craft(model = VALID_STUDENT) Student student1,
                             @Craft(model = VALID_STUDENT) Late<Student> student2) {
        System.out.println("dsa");
    }


    @Override
    protected void beforeAll(final Services services) {
        RestService restService1 = services.service(OLYMPYS, RestService.class);
    }


    @Override
    protected void afterAll(final Services services) {
        RestService restService1 = services.service(OLYMPYS, RestService.class);
    }

}
