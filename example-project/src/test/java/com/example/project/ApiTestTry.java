package com.example.project;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.example.project.rest.dto.Pet;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static com.example.project.data.creator.TestDataCreator.DOG_PET;
import static com.example.project.data.creator.TestDataCreator.Data;
import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.example.project.rest.Endpoints.CREATE_PET;
import static com.theairebellion.zeus.api.retry.RetryConditionApi.responseFieldEqualsTo;
import static com.theairebellion.zeus.api.retry.RetryConditionApi.statusEquals;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class ApiTestTry extends BaseTest {

    @Test
    public void testPet(Quest quest, @Craft(model = Data.DOG_PET) Pet pet) {
        quest.enters(World.OLYMPYS)
            .requestAndValidate(CREATE_PET, pet,
                Assertion.builder().target(STATUS).type(IS).expected(200).build())
            .complete();
    }


    static Stream<Arguments> petsProvider() {
        return Stream.of(
            Arguments.of(DOG_PET.dataCreator(), VALID_STUDENT.dataCreator()),
            Arguments.of(DOG_PET.dataCreator(), VALID_STUDENT.dataCreator())
        );
    }


    @ParameterizedTest
    @MethodSource("petsProvider")
    public void testPetNew(Late<Pet> pet, Late<Student> student, Quest quest) {
        quest.enters(World.OLYMPYS)

            .retryUntil(statusEquals(CREATE_PET, 200),
                Duration.ofSeconds(5), Duration.ofSeconds(1))

            .retryUntil(responseFieldEqualsTo(CREATE_PET, "$.status", "FINISH"),
                Duration.ofSeconds(5), Duration.ofSeconds(1))


            .requestAndValidate(CREATE_PET.withQueryParam("name", "john").withQueryParam("surname", "smith"),
                pet.join(),
                Assertion.builder().target(STATUS).type(IS).expected(200).build())
            .complete();
    }


}
