package com.example.project.preconditions;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.example.project.rest.Endpoints;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractorsTest;

public class QuestPreconditionFunctions {

    public static void validStudentsSetup(SuperQuest quest, Student student) {
        System.out.println("cdsfsdfsd");
    }


    public static void validStudentsSetup(SuperQuest quest, Late<Student> student) {
        String test = quest.getStorage().get(DataExtractorsTest.staticTestData("test"), String.class);
        quest.enters(World.OLYMPYS)
                .request(Endpoints.ENDPOINT_EXAMPLE, student.join());
    }


    public static void login(SuperQuest quest, String username, String password) {
        quest.enters(World.FORGE)
                .login(username, password);
        // .input().insert(InputFields.USERNAME_FIELD, username)
        // .input().insert(InputFields.USERNAME_FIELD, password);

    }

}
