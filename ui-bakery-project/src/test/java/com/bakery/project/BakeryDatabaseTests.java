package com.bakery.project;

import com.bakery.project.data.creator.TestDataCreator;
import com.bakery.project.db.DbResponsesJsonPaths;
import com.bakery.project.db.hooks.DbHookFlows;
import com.bakery.project.model.bakery.Order;
import com.bakery.project.ui.authentication.AdminUi;
import com.bakery.project.ui.authentication.BakeryUiLogging;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.annotations.DbHook;
import com.theairebellion.zeus.db.annotations.DbHooks;
import com.theairebellion.zeus.db.query.QueryResponse;
import com.theairebellion.zeus.db.storage.StorageKeysDb;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.validator.core.Assertion;
import io.qameta.allure.Description;
import java.util.List;
import org.junit.jupiter.api.Test;

import static com.bakery.project.base.World.FORGE;
import static com.bakery.project.base.World.UNDERWORLD;
import static com.bakery.project.data.cleaner.TestDataCleaner.Data.DELETE_CREATED_ORDERS;
import static com.bakery.project.data.creator.TestDataCreator.Data.VALID_ORDER;
import static com.bakery.project.data.creator.TestDataCreator.Data.VALID_SELLER;
import static com.bakery.project.db.Queries.QUERY_ORDER;
import static com.bakery.project.db.Queries.QUERY_ORDER_PRODUCT;
import static com.bakery.project.preconditions.BakeryQuestPreconditions.Data.LOGIN_PRECONDITION;
import static com.bakery.project.preconditions.BakeryQuestPreconditions.Data.ORDER_PRECONDITION;
import static com.bakery.project.preconditions.BakeryQuestPreconditions.Data.SELLER_PRECONDITION;
import static com.theairebellion.zeus.db.validator.DbAssertionTarget.QUERY_RESULT;
import static com.theairebellion.zeus.framework.hooks.HookExecution.BEFORE;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.validator.core.AssertionTypes.CONTAINS_ALL;
import static com.theairebellion.zeus.validator.core.AssertionTypes.EQUALS_IGNORE_CASE;

@UI
@DB
@API
@DbHooks({
      @DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
})
public class BakeryDatabaseTests extends BaseTest {

   @Test
   @Description("Database usage in Test")
   @PreQuest({
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(VALID_SELLER)}, order = 1),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)}, order = 2)
   })
   public void createOrderDatabaseValidation(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(FORGE)
            .validateOrder(order)
            .then()
            .enters(UNDERWORLD)
            .query(QUERY_ORDER.withParam("id", 1))
            .validate(retrieve(StorageKeysDb.DB, QUERY_ORDER, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PRODUCT_BY_ID.getJsonPath(1))
                        .type(CONTAINS_ALL).expected(List.of(order.getProduct())).soft(true)
                        .build(),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.LOCATION_BY_ID.getJsonPath(1))
                        .type(CONTAINS_ALL).expected(List.of(order.getLocation())).soft(true)
                        .build()
            )
            .complete();
   }


   @Test
   @Description("Database usage in PreQuest and Test")
   @PreQuest({
         @Journey(value = SELLER_PRECONDITION,
               journeyData = {@JourneyData(VALID_SELLER)}, order = 1),
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(VALID_SELLER)}, order = 2),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)}, order = 3)
   })
   public void createOrderPreQuestDatabase(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(FORGE)
            .validateOrder(order)
            .then()
            .enters(UNDERWORLD)
            .query(QUERY_ORDER_PRODUCT.withParam("id", 1))
            .validate(retrieve(StorageKeysDb.DB, QUERY_ORDER_PRODUCT, QueryResponse.class),
                  Assertion.builder()
                        .target(QUERY_RESULT).key(DbResponsesJsonPaths.PRODUCT.getJsonPath(0))
                        .type(EQUALS_IGNORE_CASE).expected(order.getProduct()).soft(true)
                        .build()
            )
            .complete();
   }


   @Test
   @Description("Database cleanup with Ripper usage")
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)})
   })
   @Ripper(targets = {DELETE_CREATED_ORDERS})
   public void createOrderPreArgumentsAndRipper(Quest quest) {
      quest
            .enters(FORGE)
            .validateOrder(retrieve(PRE_ARGUMENTS, TestDataCreator.VALID_ORDER, Order.class))
            .complete();
   }

}
