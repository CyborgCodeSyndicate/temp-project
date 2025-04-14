package com.reqres.test.framework.data.cleaner;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.rest.Endpoints.DELETE_USER;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class DataCleanUpFunctions {

   public static void deleteAdminUser(SuperQuest quest) {
      quest.enters(OLYMPYS)
            .requestAndValidate(
                  DELETE_USER.withPathParam("id", 2),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_NO_CONTENT).build()
         );
   }

}
