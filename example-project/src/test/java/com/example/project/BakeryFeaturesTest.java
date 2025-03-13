package com.example.project;


import com.example.project.data.creator.TestDataCreator;
import com.example.project.db.h2.H2Database;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.preconditions.BakeryInterceptRequests;
import com.example.project.ui.authentication.AdminUI;
import com.example.project.ui.authentication.BakeryUILogging;
import com.theairebellion.zeus.db.annotations.DB;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTestSequential;
import com.theairebellion.zeus.framework.base.Services;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.project.base.World.*;
import static com.example.project.data.cleaner.TestDataCleaner.Data.DELETE_CREATED_ORDERS;
import static com.example.project.data.creator.TestDataCreator.Data.*;
import static com.example.project.db.Queries.QUERY_ORDER;
import static com.example.project.preconditions.BakeryInterceptRequests.Data.INTERCEPT_REQUEST_AUTH;
import static com.example.project.preconditions.BakeryQuestPreconditions.Data.LOGIN_PRECONDITION;
import static com.example.project.preconditions.BakeryQuestPreconditions.Data.ORDER_PRECONDITION;
import static com.example.project.ui.elements.Bakery.ButtonFields.*;
import static com.example.project.ui.elements.Bakery.SelectFields.LOCATION_DDL;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@UI
@DB
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
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class)
    public void createOrderAuth(Quest quest,
                                @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }


    @Test
    @Description("Authenticate, PreQuest and PreArguments usage")
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
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
                        retrieve(DataExtractorsUi
                                        .responseBodyExtraction(BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH.getEndpointSubString(),
                                                "$[0].changes[?(@.key=='totalPrice')].value", "for(;;);"),
                                List.class)))
                .complete();
    }


    @Test()
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
    @Description("Interceptor with Storage and Late data re-usage")
    @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
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


    @Override
    protected void beforeAll(final Services services) {
        DatabaseService service = services.service(UNDERWORLD, DatabaseService.class);
        DatabaseConfiguration dbConfig = QUERY_ORDER.config();
        H2Database.initialize(dbConfig, service);
    }

}
