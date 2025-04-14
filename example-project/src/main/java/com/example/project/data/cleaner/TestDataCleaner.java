package com.example.project.data.cleaner;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper {
   ALL_CREATED_STUDENTS(DataCleanUpFunctions::cleanAllStudents);

   public static final class Data {
      public static final String ALL_CREATED_STUDENTS = "ALL_CREATED_STUDENTS";

      private Data() {
      }
   }


   private final Consumer<SuperQuest> cleanUpFunction;


   TestDataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
      this.cleanUpFunction = cleanUpFunction;
   }


   @Override
   public Consumer<SuperQuest> eliminate() {
      return cleanUpFunction;
   }


   @Override
   public Enum<?> enumImpl() {
      return this;
   }
}
