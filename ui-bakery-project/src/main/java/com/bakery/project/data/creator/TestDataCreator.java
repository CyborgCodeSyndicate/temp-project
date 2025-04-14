package com.bakery.project.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge {

   VALID_SELLER(DataCreationFunctions::createValidSeller),
   VALID_ORDER(DataCreationFunctions::createValidOrder),
   VALID_LATE_ORDER(DataCreationFunctions::createValidLateOrder);


   public static final class Data {

      public static final String VALID_SELLER = "VALID_SELLER";
      public static final String VALID_ORDER = "VALID_ORDER";
      public static final String VALID_LATE_ORDER = "VALID_LATE_ORDER";


      private Data() {
      }

   }

   private final Late<Object> createDataFunction;


   TestDataCreator(final Late<Object> createDataFunction) {
      this.createDataFunction = createDataFunction;
   }


   @Override
   public Late<Object> dataCreator() {
      return createDataFunction;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }
}
