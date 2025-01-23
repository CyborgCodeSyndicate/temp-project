package com.petstore.test.framework;

import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.petstore.test.framework.base.World.OLYMPYS;
import static com.petstore.test.framework.rest.Endpoints.GET_PET_BY_ID;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.BODY;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

@API
public class PetTest extends BaseTest {

    @Test
    public void testGetPetById(Quest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        GET_PET_BY_ID.withPathParam("petId", 589),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_OK).build(),
                        Assertion.builder(Integer.class).target(BODY).key("id").type(IS).expected(589).soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("name").type(IS).expected("Clare").soft(true).build(),
                        Assertion.builder(String.class).target(BODY).key("status").type(IS).expected("sold").soft(true).build()
                ).complete();
    }
}
