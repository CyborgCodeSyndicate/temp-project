package com.example.project;

import com.example.project.base.World;
import com.example.project.data.cleaner.TestDataCleaner;
import com.example.project.model.Student;
import com.example.project.model.TableEntry;
import com.example.project.preconditions.QuestPreconditions;
import com.example.project.rest.authentication.AdminAuth;
import com.example.project.rest.authentication.PortalAuthentication;
import com.theairebellion.zeus.api.annotations.AuthenticateViaApiAs;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.project.data.creator.TestDataCreator.Data;
import static com.example.project.ui.elements.Tables.CAMPAIGNS;
import static com.theairebellion.zeus.ui.storage.DataExtractorsUi.tableRowExtractor;

@UI
// @API
// @DB
public class ExampleTest extends BaseTestSequential {


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

    private record UserDto(String name, int age) {

    }


    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    @PreQuest({
        @Journey(value = QuestPreconditions.Data.STUDENT_PRECONDITION, journeyData = {@JourneyData(Data.VALID_STUDENT)})
    })
    @Ripper(targets = {TestDataCleaner.Data.ALL_CREATED_STUDENTS})
    public void testExample1(Quest quest, @Craft(model = Data.VALID_STUDENT) Student student1,
                             @Craft(model = Data.VALID_STUDENT) Late<Student> student2) {
        ;
        quest
            .enters(World.EARTH)
            .table().readTable(CAMPAIGNS, TableField.of(TableEntry::setStudentName),
                TableField.of(TableEntry::setStudentSurname))
            .validate(() -> Assertions.assertEquals(
                "SomeName",
                retrieve(tableRowExtractor(CAMPAIGNS, "123", "User"), TableEntry.class).getStudentName().getText(),
                "Error Message")
            )

            .validate(() -> Assertions.assertEquals(
                "SomeName",
                DefaultStorage.retrieve(CAMPAIGNS, TableEntry.class).getStudentName().getText(),
                "Error Message")
            )


            .validate(() -> Assertions.assertTrue(
                DefaultStorage.retrieve(CAMPAIGNS, TableEntry.class).getStudentName().getElement().isDisplayed(),
                "Error Message")
            )

            .table().readRow(CAMPAIGNS, List.of("123"), TableField.of(TableEntry::setStudentName),
                TableField.of(TableEntry::setStudentSurname))
            .complete();

        System.out.println("dsa");
    }


    @Test
    @AuthenticateViaApiAs(credentials = AdminAuth.class, type = PortalAuthentication.class)
    @PreQuest({
        @Journey(value = QuestPreconditions.Data.STUDENT_PRECONDITION, journeyData = {@JourneyData(Data.VALID_STUDENT)})
    })
    @Ripper(targets = {TestDataCleaner.Data.ALL_CREATED_STUDENTS})
    public void testExample2(Quest quest, @Craft(model = Data.VALID_STUDENT) Student student1,
                             @Craft(model = Data.VALID_STUDENT) Late<Student> student2) {

        System.out.println("dsa");
    }

    //
    // @Test
    // @PreQuest({
    //     @Journey(value = QuestPreconditions.Data.LOGIN,
    //         journeyData = {@JourneyData(Data.USERNAME_JOHN), @JourneyData(Data.PASSWORD_JOHN)})
    // })
    //
    // public void testExample3(Quest quest) {
    //     quest.enters(World.FORGE)
    //         .login(testData().username(), "sfsdfsdfs")
    //         .then()
    //
    //         .enters(World.EARTH)
    //         .input().insert(USERNAME, "vdsfsd")
    //
    //
    //         .input().getErrorMessage(USERNAME)
    //         .validate(softAssertions ->
    //                       softAssertions.assertThat(retrieve(StorageKeysUi.UI, USERNAME, String.class))
    //                           .isEqualTo("Expected Message"))
    //
    //         .validate(softAssertions ->
    //                       softAssertions.assertThat(DefaultStorage.retrieve(USERNAME, String.class))
    //                           .isEqualTo("Expected Message"))
    //
    //
    //         .input().validateErrorMessage(USERNAME, "Expected Message")
    //
    //
    //         .complete();
    // }


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
