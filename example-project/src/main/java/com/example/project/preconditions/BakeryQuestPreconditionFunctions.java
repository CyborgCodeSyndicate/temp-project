package com.example.project.preconditions;

import com.example.project.base.World;
import com.example.project.db.Queries;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.rest.Endpoints;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractorsTest;
import com.theairebellion.zeus.validator.core.Assertion;

import static com.example.project.rest.Endpoints.ENDPOINT_EXAMPLE;
import static com.theairebellion.zeus.api.storage.DataExtractorsApi.responseBodyExtraction;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.NUMBER_ROWS;
import static com.theairebellion.zeus.framework.base.BaseTest.DefaultStorage.retrieve;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class BakeryQuestPreconditionFunctions {


    public static void loginUser(SuperQuest quest, Seller seller) {
        quest.enters(World.FORGE)
                .loginUser(seller);
    }

    public static void validSellerSetup(SuperQuest quest, Seller seller) {
        quest.enters(World.UNDERWORLD)
                .query(Queries.EXAMPLE.withParam("id",
                        retrieve(responseBodyExtraction(ENDPOINT_EXAMPLE, "$.id"), Long.class)))
                .validate(retrieve(Queries.EXAMPLE, QueryResponse.class),
                        Assertion.builder(Integer.class).target(NUMBER_ROWS).type(IS).expected(3).soft(true)
                                .build());
    }

    public static void validSellerSetup(SuperQuest quest, Late<Seller> seller) {
        String test = quest.getStorage().get(DataExtractorsTest.staticTestData("test"), String.class);
        quest.enters(World.OLYMPYS)
                .request(Endpoints.ENDPOINT_EXAMPLE, seller.join());
    }

    public static void validOrderSetup(SuperQuest quest, Order order) {
        quest.enters(World.FORGE)
                .createOrder(order);
    }

    public static void validOrderSetup(SuperQuest quest, Late<Order> order) {
        quest.enters(World.FORGE)
                .createOrder(order.join());
    }

}
