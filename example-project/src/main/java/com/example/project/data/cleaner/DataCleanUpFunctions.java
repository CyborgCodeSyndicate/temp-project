package com.example.project.data.cleaner;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.List;

import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT_FLOW;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;

public class DataCleanUpFunctions {

    public static void cleanAllStudents(@Jailbreak Quest quest) {
        var storage = quest.getStorage().sub(ARGUMENTS);
        List<Student> allStudents = storage.getAllByClass(VALID_STUDENT_FLOW, Student.class);

        DatabaseServiceFluent dbService = quest.enters(World.UNDERWORLD);
        allStudents.forEach(student -> {
            // dbService.query(null);
            System.out.println("Delete student");
        });
    }


}
