package com.example.project;


import com.example.project.data.creator.TestDataCreator;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.ui.authentication.AdminUI;
import com.example.project.ui.authentication.BakeryUILogging;
import com.theairebellion.zeus.framework.annotation.*;
import com.theairebellion.zeus.framework.base.BaseTest;
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

import static com.example.project.base.World.EARTH;
import static com.example.project.base.World.FORGE;
import static com.example.project.data.cleaner.TestDataCleaner.Data.DELETE_CREATED_ORDERS;
import static com.example.project.data.creator.TestDataCreator.Data.*;
import static com.example.project.preconditions.BakeryInterceptRequests.Data.INTERCEPT_REQUEST_AUTH;
import static com.example.project.preconditions.BakeryQuestPreconditions.Data.*;
import static com.example.project.ui.elements.Bakery.ButtonFields.*;
import static com.example.project.ui.elements.Bakery.SelectFields.LOCATION_DDL;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@UI
public class BakeryFeaturesTest extends BaseTest {


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
                        DefaultStorage.retrieve(LOCATION_DDL, List.class).size()
                ))
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
    @Description("Authenticate and PreQuest usage")
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
    @PreQuest({
            @Journey(value = ORDER_PRECONDITION,
                    journeyData = {@JourneyData(VALID_ORDER)})
    })
    public void createOrderAuthPreQuest(Quest quest,
                                  @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .validateOrder(order)
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
                .interceptor().validate(() -> assertEquals(List.of("$197.54"),
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='totalPrice')].value"), List.class)))
                .complete();
    }


    @Test()
    @Description("Interceptor and Late data usage")
    @InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH})
    @Ripper(targets = {DELETE_CREATED_ORDERS})
    public void createOrderInterceptorLateDate(Quest quest,
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
                .interceptor().validateResponseHaveStatus("?v-r=uidl", 2, true)
                .complete();
    }

}
