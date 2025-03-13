package com.bakery.project.preconditions;

import com.bakery.project.base.World;
import com.bakery.project.model.bakery.Order;
import com.bakery.project.model.bakery.Seller;
import com.bakery.project.rest.Endpoints;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractorsTest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

import static com.bakery.project.base.World.*;
import static com.bakery.project.db.Queries.QUERY_SELLER;
import static com.bakery.project.rest.Endpoints.ENDPOINT_BAKERY;
import static com.bakery.project.rest.Endpoints.ENDPOINT_BAKERY_LOGIN;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.*;
import static com.theairebellion.zeus.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class BakeryQuestPreconditionFunctions {


    public static void loginUser(SuperQuest quest, Seller seller) {
        quest.enters(FORGE)
                .loginUser(seller);

        //Set<Cookie> cookies = quest.artifact(EARTH, SmartWebDriver.class).getOriginal().manage().getCookies();
        quest.enters(World.OLYMPYS)
                //.request(
                //        ENDPOINT_BAKERY_LOGIN,
                //        seller
                //)
                //.request(Endpoints.ENDPOINT_BAKERY);
                .requestAndValidate(
                        ENDPOINT_BAKERY,
                        Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_MOVED_TEMPORARILY).build());
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
