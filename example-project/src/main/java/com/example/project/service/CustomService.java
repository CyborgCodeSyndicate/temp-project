package com.example.project.service;

import com.example.project.data.creator.TestDataCreator;
import com.example.project.model.Student;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

import static com.example.project.base.World.EARTH;

@TestService("Custom")
public class CustomService extends FluentService {


   public CustomService somethingCustom(Student student) {
      quest.getStorage().sub(StorageKeysTest.ARGUMENTS).getAllByClass(TestDataCreator.VALID_STUDENT, Student.class);
      SmartWebDriver artifact = quest.artifact(EARTH, SmartWebDriver.class);

      return this;
   }
}
