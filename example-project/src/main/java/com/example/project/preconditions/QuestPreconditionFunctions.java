package com.example.project.preconditions;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.example.project.rest.Endpoints;
import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

public class QuestPreconditionFunctions {

    public static void validStudentsSetup(@Jailbreak Quest quest, Student student) {
        System.out.println("cdsfsdfsd");
    }


    public static void validStudentsSetup(@Jailbreak Quest quest, Late<Student> student) {
        quest.enters(World.OLYMPYS)
            .request(Endpoints.ENDPOINT_EXAMPLE, student.join());
    }


    public static void login(@Jailbreak Quest quest, String username, String password) {
        quest.enters(World.FORGE)
            .login(username, password);
            // .input().insert(InputFields.USERNAME_FIELD, username)
            // .input().insert(InputFields.USERNAME_FIELD, password);

    }

}
