package com.example.project.data.cleaner;

import com.example.project.model.Student;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.framework.quest.SuperQuest;


import java.util.List;

import static com.example.project.base.World.UNDERWORLD;
import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;

public class DataCleanUpFunctions {

    public static void cleanAllStudents(SuperQuest quest) {
        var storage = quest.getStorage().sub(ARGUMENTS);
        List<Student> allStudents = storage.getAllByClass(VALID_STUDENT, Student.class);

        DatabaseServiceFluent dbService = quest.enters(UNDERWORLD);
        allStudents.forEach(student -> {
            // dbService.query(null);
            System.out.println("Delete student");
        });
    }

}
