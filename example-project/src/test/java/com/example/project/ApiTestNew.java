package com.example.project;

import com.example.project.base.World;
import com.example.project.data.creator.TestDataCreator;
import com.example.project.rest.dto.Pet;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Test;

import static com.example.project.rest.Endpoints.CREATE_PET;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class ApiTestNew extends BaseTest {

    @Test
    public void testPet(Quest quest, @Craft(model = TestDataCreator.Data.DOG_PET) Pet pet) {
        quest.enters(World.OLYMPYS)
                .requestAndValidate(CREATE_PET, pet,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(200).build())
                .complete();
    }


}
