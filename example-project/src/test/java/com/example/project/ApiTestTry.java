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

import static com.example.project.data.creator.TestDataCreator.DOG_PET;
import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.example.project.rest.Endpoints.CREATE_PET;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class ApiTestTry extends BaseTest {

    @Test
    public void testPet(Quest quest, @Craft(model = DOG_PET) Pet pet) {
        quest.enters(World.OLYMPYS)
            .requestAndValidate(CREATE_PET.withQueryParam("name", "john").withQueryParam("surname", "smith"), pet,
                Assertion.builder(Integer.class).target(STATUS).type(IS).expected(200).build())
            .complete();
    }


    @Test
    public void testPetNew(Quest quest, @Craft(model = VALID_STUDENT) Student student, @Craft(model = DOG_PET) Late<Pet> pet) {
        quest.enters(World.OLYMPYS)
                .requestAndValidate(CREATE_PET.withQueryParam("name", "john").withQueryParam("surname", "smith"), pet.join(),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(200).build())
                .complete();
    }

}
