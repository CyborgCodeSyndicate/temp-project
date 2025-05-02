package com.reqres.test.framework.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge<TestDataCreator> {

   USER_LEADER_FLOW(DataCreationFunctions::createLeaderUser),
   LOGIN_ADMIN_USER_FLOW(DataCreationFunctions::createAdminLoginUser),
   USER_JUNIOR_FLOW(DataCreationFunctions::createJuniorUser),
   USER_SENIOR_FLOW(DataCreationFunctions::createSeniorUser),
   USER_INTERMEDIATE_FLOW(DataCreationFunctions::createIntermediateUser);

   public static final String USER_LEADER = "USER_LEADER_FLOW";
   public static final String LOGIN_ADMIN_USER = "LOGIN_ADMIN_USER_FLOW";
   public static final String USER_JUNIOR = "USER_JUNIOR_FLOW";
   public static final String USER_SENIOR = "USER_SENIOR_FLOW";
   public static final String USER_INTERMEDIATE = "USER_INTERMEDIATE_FLOW";

   private final Late<Object> createDataFunction;

   TestDataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }

   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }

   @Override
   public TestDataCreator enumImpl() {
      return this;
   }

}
