package com.example.project;

import com.example.project.data.cleaner.TestDataCleaner;
import com.example.project.model.Student;
import com.example.project.preconditions.QuestPreconditions;
import com.example.project.rest.authentication.AdminAuth;
import com.example.project.rest.authentication.PortalAuthentication;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.api.annotations.AuthenticateAs;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import org.junit.jupiter.api.Test;

import static com.example.project.data.creator.TestDataCreator.Data;

// @UI
@API
@DB
public class ExampleTest extends BaseTest {


    // @Test
    // @AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    // @InterceptRequests(requestUrlSubStrings = {"api/create-campaign", "upload"})
    // @Ripper(targets = {ALL_CREATED_STUDENTS})
    // public void testExample(Quest quest, @Craft(model = Data.VALID_STUDENT) Student student1,
    //                         @Craft(model = Data.VALID_STUDENT) Late<Student> student2) {
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
        @Journey(value = QuestPreconditions.Data.STUDENT_PRECONDITION, journeyData = {@JourneyData(Data.VALID_STUDENT)})
    })
    @Ripper(targets = {TestDataCleaner.Data.ALL_CREATED_STUDENTS})
    public void testExample1(Quest quest, @Craft(model = Data.VALID_STUDENT) Student student1,
                             @Craft(model = Data.VALID_STUDENT) Late<Student> student2) {
        System.out.println("dsa");
    }

    @Test
    @AuthenticateAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    @PreQuest({
            @Journey(value = QuestPreconditions.Data.STUDENT_PRECONDITION, journeyData = {@JourneyData(Data.VALID_STUDENT)})
    })
    @Ripper(targets = {TestDataCleaner.Data.ALL_CREATED_STUDENTS})
    public void testExample2(Quest quest, @Craft(model = Data.VALID_STUDENT) Student student1,
                             @Craft(model = Data.VALID_STUDENT) Late<Student> student2) {
        System.out.println("dsa");
    }


//    @Override
//    protected void beforeAll(final Services services) {
//        RestService restService1 = services.service(OLYMPYS, RestService.class);
//        DatabaseService service = services.service(UNDERWORLD, DatabaseService.class);
//    }
//
//
//    @Override
//    protected void afterAll(final Services services) {
//        RestService restService1 = services.service(OLYMPYS, RestService.class);
//    }

}
