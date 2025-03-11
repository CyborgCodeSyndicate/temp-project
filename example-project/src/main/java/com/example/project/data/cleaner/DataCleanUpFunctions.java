package com.example.project.data.cleaner;

import com.example.project.base.World;
import com.example.project.model.Student;
import com.example.project.model.bakery.Order;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.service.fluent.DatabaseServiceFluent;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static com.example.project.base.World.UNDERWORLD;
import static com.example.project.data.creator.TestDataCreator.VALID_ORDER;
import static com.example.project.data.creator.TestDataCreator.VALID_STUDENT;
import static com.example.project.db.Queries.*;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.*;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.*;

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

    public static void cleanAllOrders(SuperQuest quest) {
        var storage = quest.getStorage().sub(ARGUMENTS);
        List<Order> allOrders = storage.getAllByClass(VALID_ORDER, Order.class);

        DatabaseServiceFluent dbService = quest.enters(UNDERWORLD);
        allOrders.forEach(order -> {
            dbService
                    .query(QUERY_ORDER_DELETE.withParam("id", order.getId()))
                    .query(QUERY_ORDER.withParam("id", order.getId()))
                    .validate(() ->
                            Assertions.assertTrue(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER, QueryResponse.class)
                                    .getRows().isEmpty()));
                    /*.validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER, QueryResponse.class),
                            Assertion.builder(Object.class)
                                    .target(NUMBER_ROWS)
                                    .type(IS).expected(0).soft(true) //todo: check how to validate null result
                                    .build()
                    );*/
            System.out.println("Delete order");
        });
    }

}
