package com.bakery.project;

import com.bakery.project.data.creator.TestDataCreator;
import com.bakery.project.data.extractions.CustomDataExtractor;
import com.bakery.project.db.hooks.DbHookFlows;
import com.bakery.project.model.bakery.Order;
import com.bakery.project.model.bakery.Seller;
import com.bakery.project.preconditions.BakeryInterceptRequests;
import com.bakery.project.ui.authentication.AdminUi;
import com.bakery.project.ui.authentication.BakeryUiLogging;
import com.theairebellion.zeus.api.annotations.API;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.annotations.DbHook;
import com.theairebellion.zeus.db.annotations.DbHooks;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.annotation.Ripper;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.validator.core.Assertion;
import io.qameta.allure.Description;
import java.util.List;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.bakery.project.base.World.EARTH;
import static com.bakery.project.base.World.FORGE;
import static com.bakery.project.base.World.OLYMPYS;
import static com.bakery.project.data.cleaner.TestDataCleaner.Data.DELETE_CREATED_ORDERS;
import static com.bakery.project.data.creator.TestDataCreator.Data.VALID_LATE_ORDER;
import static com.bakery.project.data.creator.TestDataCreator.Data.VALID_ORDER;
import static com.bakery.project.data.creator.TestDataCreator.Data.VALID_SELLER;
import static com.bakery.project.preconditions.BakeryInterceptRequests.Data.INTERCEPT_REQUEST_AUTH;
import static com.bakery.project.preconditions.BakeryQuestPreconditions.Data.LOGIN_PRECONDITION;
import static com.bakery.project.preconditions.BakeryQuestPreconditions.Data.ORDER_PRECONDITION;
import static com.bakery.project.rest.Endpoints.ENDPOINT_BAKERY;
import static com.bakery.project.service.CustomService.getJsessionCookie;
import static com.bakery.project.ui.elements.bakery.ButtonFields.NEW_ORDER_BUTTON;
import static com.bakery.project.ui.elements.bakery.ButtonFields.PLACE_ORDER_BUTTON;
import static com.bakery.project.ui.elements.bakery.ButtonFields.REVIEW_ORDER_BUTTON;
import static com.bakery.project.ui.elements.bakery.ButtonFields.SIGN_IN_BUTTON;
import static com.bakery.project.ui.elements.bakery.SelectFields.LOCATION_DDL;
import static com.theairebellion.zeus.api.validator.RestAssertionTarget.STATUS;
import static com.theairebellion.zeus.framework.hooks.HookExecution.BEFORE;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static com.theairebellion.zeus.validator.core.AssertionTypes.IS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@UI
@DB
@API
@DbHooks({
      @DbHook(when = BEFORE, type = DbHookFlows.Data.INITIALIZE_H2)
})
public class BakeryFeaturesTest extends BaseTestSequential {


   @Test
   @Description("Insertion data usage")
   public void createOrderInsertion(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(EARTH)
            .browser().navigate(getUiConfig().baseUrl())
            .insertion().insertData(seller)
            .button().click(SIGN_IN_BUTTON)
            .button().click(NEW_ORDER_BUTTON)
            .insertion().insertData(order)
            .button().click(REVIEW_ORDER_BUTTON)
            .button().click(PLACE_ORDER_BUTTON)
            .then()
            .enters(FORGE)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Storage usage")
   public void createOrderStorage(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller) {
      quest
            .enters(FORGE)
            .loginUser(seller)
            .then()
            .enters(EARTH)
            .button().click(NEW_ORDER_BUTTON)
            .select().getAvailableOptions(LOCATION_DDL)
            .validate(() -> Assertions.assertEquals(
                  2,
                  DefaultStorage.retrieve(LOCATION_DDL, List.class).size()))
            .validate(() -> Assertions.assertIterableEquals(
                  List.of("Store", "Bakery"),
                  DefaultStorage.retrieve(LOCATION_DDL, List.class)))
            .complete();
   }


   @Test
   @Description("PreQuest usage")
   @PreQuest({
         @Journey(value = LOGIN_PRECONDITION,
               journeyData = {@JourneyData(VALID_SELLER)}, order = 1),
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)}, order = 2)
   })
   public void createOrderPreQuest(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(FORGE)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class)
   public void createOrderAuth(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(FORGE)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authentication usage")
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class)
   public void createOrderAuth2(Quest quest,
         @Craft(model = VALID_ORDER) Order order) {
      quest
            .enters(OLYMPYS)
            .requestAndValidate(
                  ENDPOINT_BAKERY.withHeader("Cookie", getJsessionCookie()),
                  Assertion.builder().target(STATUS).type(IS).expected(HttpStatus.SC_OK).build())
            .then()
            .enters(FORGE)
            .createOrder(order)
            .validateOrder(order)
            .complete();
   }


   @Test
   @Description("Authenticate, PreQuest and PreArguments usage")
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)})
   })
   public void createOrderAuthPreQuestPreArguments(Quest quest) {
      quest
            .enters(FORGE)
            .validateOrder(retrieve(PRE_ARGUMENTS, TestDataCreator.VALID_ORDER, Order.class))
            .complete();
   }


   @Test
   @Disabled
   @Description("Interceptor raw usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   public void createOrderInterceptor(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller) {
      quest
            .enters(FORGE)
            .loginUser2(seller)
            .editOrder("Lionel Huber")
            .then()
            .enters(EARTH)
            .validate(() -> assertEquals(List.of("$197.54"),
                  retrieve(CustomDataExtractor
                              .responseBodyExtraction(BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH.getEndpointSubString(),
                                    "$[0].changes[?(@.key=='totalPrice')].value", "for(;;);"),
                        List.class)))
            .complete();
   }


   @Test()
   @Disabled
   @Description("Late data created with interceptor and ripper data cleanup usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   @Ripper(targets = {DELETE_CREATED_ORDERS})
   public void createOrderInterceptorLateDataAndRipper(Quest quest,
         @Craft(model = VALID_SELLER) Seller seller,
         @Craft(model = VALID_ORDER) Order order,
         @Craft(model = VALID_LATE_ORDER) Late<Order> lateOrder) {
      quest
            .enters(FORGE)
            .loginUser2(seller)
            .createOrder(order)
            .validateOrder(order)
            .createOrder(lateOrder.join())
            .validateOrder(lateOrder.join())
            .complete();
   }


   @Test
   @Disabled
   @Description("Interceptor with Storage and Late data re-usage")
   @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
   @PreQuest({
         @Journey(value = ORDER_PRECONDITION,
               journeyData = {@JourneyData(VALID_ORDER)})
   })
   public void createOrderInterceptorStorage(Quest quest,
         @Craft(model = VALID_LATE_ORDER) Late<Order> lateOrder) {
      quest
            .enters(FORGE)
            .validateOrder(retrieve(PRE_ARGUMENTS, TestDataCreator.VALID_ORDER, Order.class))
            .createOrder(lateOrder.join())
            .validateOrder(lateOrder.join())
            .then()
            .enters(EARTH)
            .interceptor().validateResponseHaveStatus(
                  BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH.getEndpointSubString(), 2, true)
            .complete();
   }

}
