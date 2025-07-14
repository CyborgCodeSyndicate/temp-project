package com.bakery.project.data.cleaner;

import com.bakery.project.db.DbResponsesJsonPaths;
import com.bakery.project.model.bakery.Order;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.validator.core.Assertion;
import java.util.List;

import static com.bakery.project.base.World.UNDERWORLD;
import static com.bakery.project.data.creator.TestDataCreator.VALID_ORDER;
import static com.bakery.project.db.Queries.QUERY_ORDER_ALL;
import static com.bakery.project.db.Queries.QUERY_ORDER_DELETE;
import static com.bakery.project.db.Queries.QUERY_ORDER_PRODUCT;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.NUMBER_ROWS;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.QUERY_RESULT;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.ARGUMENTS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;

public class DataCleanUpFunctions {


   public static void cleanAllOrders(SuperQuest quest) {
      var storage = quest.getStorage().sub(ARGUMENTS);
      List<Order> allOrders = storage.getAllByClass(VALID_ORDER, Order.class);

      allOrders.forEach(order ->
            quest
                  .enters(UNDERWORLD)
                  .query(QUERY_ORDER_ALL)
                  .query(QUERY_ORDER_DELETE.withParam("id", order.getId()))
                  .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER_DELETE, QueryResponse.class),
                        Assertion.builder()
                              .target(QUERY_RESULT).key(DbResponsesJsonPaths.DELETED.getJsonPath(0))
                              .type(IS).expected(1).soft(true)
                              .build()
                  )
                  .query(QUERY_ORDER_PRODUCT.withParam("id", order.getId()))
                  .validate(quest.getStorage().sub(StorageKeysDb.DB).get(QUERY_ORDER_PRODUCT, QueryResponse.class),
                        Assertion.builder()
                              .target(NUMBER_ROWS).key("numRows")
                              .type(IS).expected(0).soft(true)
                              .build()
                  ));
   }

}
