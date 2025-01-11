package com.example.project.data.creator;

import com.example.project.model.Student;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.storage.Storage;
import manifold.ext.rt.api.Jailbreak;

public class DataCreationFunctions {


    public static Student createValidStudent() {
        @Jailbreak Quest quest = QuestHolder.get();
        Storage storage = quest.getStorage();
        Long id = storage.get(null, Long.class);
        return Student.builder()
                   .name("John")
                   .surname("Smith")
                   // .id(id)
                   .age(20)
                   .build();

    }

}
