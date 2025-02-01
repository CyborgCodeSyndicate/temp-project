package com.reqres.test.framework.data.cleaner;

import com.theairebellion.zeus.api.service.fluent.RestServiceFluent;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.validator.core.Assertion;
import manifold.ext.rt.api.Jailbreak;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.DELETE_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class DataCleanUpFunctions {

    public static void deleteAdminUser(@Jailbreak Quest quest) {
        // todo: revisit code
        RestServiceFluent enters = quest.enters(OLYMPYS);
        enters
                .requestAndValidate(
                        DELETE_USER.withPathParam("id", 2),
                        Assertion.builder(Integer.class).target(STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
                );
    }
}
