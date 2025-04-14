package com.bakery.project.preconditions;

import com.bakery.project.db.DbResponsesJsonPaths;
import com.bakery.project.model.bakery.Order;
import com.bakery.project.model.bakery.Seller;
import com.bakery.project.rest.Endpoints;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractorsTest;
import com.theairebellion.zeus.validator.core.Assertion;
import org.apache.http.HttpStatus;

import static com.bakery.project.base.World.FORGE;
import static com.bakery.project.base.World.OLYMPYS;
import static com.bakery.project.base.World.UNDERWORLD;
import static com.bakery.project.db.Queries.QUERY_SELLER_EMAIL;
import static com.bakery.project.db.Queries.QUERY_SELLER_PASSWORD;
import static com.bakery.project.rest.Endpoints.ENDPOINT_BAKERY;
import static com.bakery.project.service.CustomService.getJsessionCookie;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.QUERY_RESULT;
import static com.theairebellion.zeus.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class BakeryQuestPreconditionFunctions {


   public static void loginUser(SuperQuest quest, Seller seller) {
      quest
            .enters(FORGE)
            .loginUser(seller)
            .then()
            .enters(OLYMPYS)
            .requestAndValidate(
                  ENDPOINT_BAKERY.withHeader("Cookie", getJsessionCookie()),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build());
   }

   public static void validSellerSetup(SuperQuest quest, Seller seller) {
      quest
            .enters(UNDERWORLD)
            .query(QUERY_SELLER_EMAIL.withParam("id", 1))
            .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_SELLER_EMAIL, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.EMAIL.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(seller.getEmail()).soft(true)
                        .build())
            .query(QUERY_SELLER_PASSWORD.withParam("id", 1))
            .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_SELLER_PASSWORD, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PASSWORD.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(seller.getPassword()).soft(true)
                        .build()
         );
   }

   public static void validSellerSetup(SuperQuest quest, Late<Seller> seller) {
      String test = quest.getStorage().get(DataExtractorsTest.staticTestData("test"), String.class);
      quest.enters(OLYMPYS)
            .request(Endpoints.ENDPOINT_EXAMPLE, seller.join());
   }

   public static void validOrderSetup(SuperQuest quest, Order order) {
      quest
            .enters(FORGE)
            .createOrder(order);
   }

   public static void validOrderSetup(SuperQuest quest, Late<Order> order) {
      quest
            .enters(FORGE)
            .createOrder(order.join());
   }

}
