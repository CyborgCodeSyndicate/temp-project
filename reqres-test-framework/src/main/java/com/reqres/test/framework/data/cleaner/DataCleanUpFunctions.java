package com.reqres.test.framework.data.cleaner;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.DELETE_USER;
import static com.reqres.test.framework.utils.PathVariables.ID_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Users.ID_THREE;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

public class DataCleanUpFunctions {

    public static void deleteAdminUser(SuperQuest quest) {
        quest.enters(OLYMPYS)
                .requestAndValidate(
                        DELETE_USER.withPathParam(ID_PARAM, ID_THREE),
                        Assertion.builder().target(STATUS).type(IS).expected(SC_NO_CONTENT).build()
                );
    }

}
