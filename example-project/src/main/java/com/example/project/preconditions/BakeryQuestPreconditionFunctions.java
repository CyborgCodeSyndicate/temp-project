package com.example.project.preconditions;

import com.example.project.base.World;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.rest.Endpoints;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractorsTest;
import com.theairebellion.zeus.validator.core.Assertion;

import static com.example.project.base.World.FORGE;
import static com.example.project.base.World.UNDERWORLD;
import static com.example.project.db.Queries.QUERY_SELLER;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;

public class BakeryQuestPreconditionFunctions {


    public static void loginUser(SuperQuest quest, Seller seller) {
        quest.enters(FORGE)
                .loginUser(seller);
    }

    public static void validSellerSetup(SuperQuest quest, Seller seller) {
        quest
                .enters(UNDERWORLD)
                .query(QUERY_SELLER.withParam("id", 1))
                .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_SELLER, QueryResponse.class),
                        Assertion.builder()
                                .target(COLUMNS).key("EMAIL")
                                .type(EQUALS_IGNORE_CASE).expected(seller.getEmail()).soft(true)
                                .build(),
                        Assertion.builder()
                                .target(COLUMNS).key("PASSWORD")
                                .type(EQUALS_IGNORE_CASE).expected(seller.getPassword()).soft(true)
                                .build()

                );
    }

    public static void validSellerSetup(SuperQuest quest, Late<Seller> seller) {
        String test = quest.getStorage().get(DataExtractorsTest.staticTestData("test"), String.class);
        quest.enters(World.OLYMPYS)
                .request(Endpoints.ENDPOINT_EXAMPLE, seller.join());
    }

    public static void validOrderSetup(SuperQuest quest, Order order) {
        quest.enters(FORGE)
                .createOrder(order);
    }

    public static void validOrderSetup(SuperQuest quest, Late<Order> order) {
        quest.enters(FORGE)
                .createOrder(order.join());
    }

}
