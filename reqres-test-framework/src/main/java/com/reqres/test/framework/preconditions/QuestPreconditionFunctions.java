package com.reqres.test.framework.preconditions;

import com.reqres.test.framework.rest.dto.request.User;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.POST_CREATE_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class QuestPreconditionFunctions {

    public static void createNewUser(SuperQuest quest, Object... objects) {
        createNewUser(quest, (User) objects[0]);
    }

    public static void createNewUser(SuperQuest quest, User userObject) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        POST_CREATE_USER,
                        userObject,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_CREATED).build());
    }

}
