package com.example.project.service;

import com.example.project.base.World;
import com.example.project.data.creator.TestDataCreator;
import com.example.project.model.Student;
import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@WorldName("Custom")
@Service
@Scope("prototype")
public class CustomService extends FluentService {


    public CustomService somethingCustom(Student student) {
        quest.getStorage().sub(StorageKeysTest.ARGUMENTS).getAllByClass(TestDataCreator.VALID_STUDENT, Student.class);
        SmartWebDriver artifact = quest.artifact(World.EARTH, SmartWebDriver.class);
        return this;
    }


}
