package com.reqres.test.framework.preconditions;

import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import manifold.ext.rt.api.Jailbreak;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.CREATE_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class QuestPreconditionFunctions {

    public static void createNewUser(@Jailbreak Quest quest, Object... objects) {
        createNewUser(quest, (User) objects[0]);
    }

    public static void createNewUser(@Jailbreak Quest quest, User userObject) {
        // todo: revisit code
        RestServiceFluent enters = quest.enters(OLYMPYS);
        enters
                .requestAndValidate(
                        CREATE_USER,
                        userObject,
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build());
    }
}
