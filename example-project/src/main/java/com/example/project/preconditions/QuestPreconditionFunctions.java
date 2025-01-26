package com.example.project.preconditions;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.example.project.rest.Endpoints;
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

}
