package com.example.project;


import com.example.project.data.creator.TestDataCreator;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.preconditions.BakeryQuestPreconditions;
import com.example.project.ui.authentication.AdminUI;
import com.example.project.ui.authentication.BakeryUILogging;
import com.example.project.ui.elements.Bakery.ButtonFields;
import com.example.project.ui.elements.Bakery.InputFields;
import com.example.project.ui.elements.Bakery.SelectFields;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.project.base.World.EARTH;
import static com.example.project.base.World.FORGE;
//import static com.example.project.preconditions.BakeryInterceptRequests.INTERCEPT_REQUEST_AUTH;
//import static com.example.project.preconditions.BakeryInterceptRequests.INTERCEPT_REQUEST_SAVE;
import static com.example.project.data.creator.TestDataCreator.Data.*;
import static com.example.project.preconditions.BakeryInterceptRequests.Data.*;
import static com.example.project.ui.elements.Bakery.CheckboxFields.PAST_ORDERS_CHECKBOX;
import static com.example.project.ui.elements.Bakery.SelectFields.LOCATION_DDL;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.RESPONSES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UI
public class BakeryTest extends BaseTest {


    @Test
    @Description("COMPONENTS: Input, Button, Select")
    public void scenario_one(Quest quest) {
        quest
                .enters(EARTH)
                .browser().navigate("https://bakery-flow.demo.vaadin.com/")
                .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(InputFields.PASSWORD_FIELD, "barista")
                .button().validateIsEnabled(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .select().validateAvailableOptions(LOCATION_DDL, "Store", "Bakery")
                .select().selectOption(LOCATION_DDL, "Store")
                .select().validateSelectedOptions(LOCATION_DDL, "Store")
                .select().selectOptions(LOCATION_DDL, Strategy.LAST)
                .select().validateSelectedOptions(LOCATION_DDL, "Bakery")
                .select().validateAvailableOptions(LOCATION_DDL, "Store", "Bakery")
                .complete();
    }

    @Test
    public void scenario_two(Quest quest) {
        quest
                .enters(EARTH)
                .browser().navigate("https://bakery-flow.demo.vaadin.com/")
                .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(InputFields.PASSWORD_FIELD, "barista")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
                .input().validateValue(InputFields.CUSTOMER_FIELD, "John Terry")
                .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
                .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, "Strawberry Bun")
                .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
                .input().insert(InputFields.NUMBER_FIELD, "+1-555-7777")
                .input().insert(InputFields.DETAILS_FIELD, "Address")
                .select().selectOption(LOCATION_DDL, "Bakery")
                .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
                /*.validate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })*/
                .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
                /*.validate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })*/
                .button().click(ButtonFields.PLACE_ORDER_BUTTON)
                .complete();
    }

    @Test
    public void scenario_three(Quest quest) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(InputFields.PASSWORD_FIELD, "barista")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
                .input().validateValue(InputFields.CUSTOMER_FIELD, "John Terry")
                .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
                .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, "Strawberry Bun")
                .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
                .complete();
    }


    @Test
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class)
    public void scenario_four(Quest quest) {
        quest
                .enters(EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
                .checkbox().select(PAST_ORDERS_CHECKBOX)
                .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
                .complete();
    }


    @Test
    public void scenario_five(Quest quest) {
        quest
                .enters(FORGE)
                .loginUser("barista@vaadin.com", "barista")
                .then()
                .enters(EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
                .checkbox().select(PAST_ORDERS_CHECKBOX)
                .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
                .complete();
    }


    @Test
    public void scenario_six(Quest quest) {
        quest
                .enters(FORGE)
                .loginUser("barista@vaadin.com", "barista")
                .then()
                .enters(EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .then().enters(FORGE)
                .logoutUser()
                .complete();
    }


    @Test
    @Description("Validations in Custom Service")
    public void scenario_seven(Quest quest) {
        quest
                .enters(FORGE)
                .loginUser("barista@vaadin.com", "barista")
                .createOrder()
                .then()
                .enters(EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "John Terry")
                .then()
                .enters(FORGE)
                .validateOrder("John Terry")
                //.validate()
                //.validateTextInField(HTML.Tag.H3, "John Terry", true)
                .then()
                .enters(FORGE)
                .logoutUser()
                .complete();
    }


    @Test
    @Description("Validations: Checkbox")
    public void scenario_eight(Quest quest) {
        quest
                .enters(FORGE)
                .loginUser("barista@vaadin.com", "barista")
                .then()
                .enters(EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .checkbox().select(PAST_ORDERS_CHECKBOX)
                .checkbox().isSelected(PAST_ORDERS_CHECKBOX)
                .validate(() -> assertTrue(
                        retrieve(StorageKeysUi.UI, PAST_ORDERS_CHECKBOX, Boolean.class)))
                .checkbox().deSelect(PAST_ORDERS_CHECKBOX)
                .checkbox().isSelected(PAST_ORDERS_CHECKBOX)
                .validate(() -> Assertions.assertFalse(
                        DefaultStorage.retrieve(PAST_ORDERS_CHECKBOX, Boolean.class)))
                .complete();
    }


    @Test
    @Description("Validations: Select")
    public void scenario_nine(Quest quest) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(InputFields.PASSWORD_FIELD, "barista")
                .button().validateIsEnabled(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
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
    public void scenario_ten(Quest quest, @Craft(model = TestDataCreator.Data.VALID_SELLER) Seller seller) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(InputFields.USERNAME_FIELD, seller.getEmail())
                .input().insert(InputFields.PASSWORD_FIELD, seller.getPassword())
                .button().validateIsEnabled(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
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
    //@InterceptRequests(requestUrlSubStrings = {INTERCEPT_REQUEST_AUTH, INTERCEPT_REQUEST_SAVE}) //Intercepts bodies form requests
    @PreQuest({
            @Journey(value = BakeryQuestPreconditions.Data.SELLER_PRECONDITION,
                    journeyData = {@JourneyData(TestDataCreator.Data.VALID_SELLER)}, order = 1),
            @Journey(value = BakeryQuestPreconditions.Data.ORDER_PRECONDITION,
                    journeyData = {@JourneyData(VALID_ORDER)}, order = 2)
    })
    public void scenario_eleven(Quest quest, @Craft(model = "VALID_SELLER") Late<Seller> seller) {
        quest
                .enters(FORGE)
                .loginUser(retrieve(PRE_ARGUMENTS, TestDataCreator.VALID_SELLER, Seller.class))
                .validateOrder(retrieve(PRE_ARGUMENTS, TestDataCreator.VALID_ORDER, Order.class))
                .then()
                .enters(EARTH)
                .insertion().insertData(seller.join())
                .interceptor().validateResponseHaveStatus("/api", 2, true)
                .complete();
    }


    @Test
    @Description("PreQuest usage")
    @PreQuest({
            @Journey(value = BakeryQuestPreconditions.Data.LOGIN_PRECONDITION,
                    journeyData = {@JourneyData(TestDataCreator.Data.VALID_SELLER)}, order = 1),
            @Journey(value = BakeryQuestPreconditions.Data.ORDER_PRECONDITION,
                    journeyData = {@JourneyData(VALID_ORDER)}, order = 2)
    })
    public void scenario_twelve(Quest quest,
                                @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .validateOrder(order)
                .complete();
    }


    @Test
    @Description("PreQuest usage")
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
    @PreQuest({
            @Journey(value = BakeryQuestPreconditions.Data.ORDER_PRECONDITION,
                    journeyData = {@JourneyData(VALID_ORDER)})
    })
    public void scenario_thirteen(Quest quest,
                                  @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .validateOrder(order)
                .complete();
    }


    @Test
    @Description("PreQuest usage")
    @InterceptRequests(requestUrlSubStrings = {"?v-r=uidl"})
    public void scenario_fourtheen(Quest quest, @Craft(model = TestDataCreator.Data.VALID_SELLER) Seller seller) {
        quest
                .enters(FORGE)
                .loginUser2(seller)
                .editOrder("Lionel Huber")
                .then()
                .enters(EARTH)
                /*.interceptor().validate(() -> {
                    //SuperQuest superQuest = QuestHolder.get();
                    //List<ApiResponse> responses1 = superQuest.getStorage().sub(StorageKeysUi.UI).get(RESPONSES, new ParameterizedTypeReference<>() {});
                        assertEquals("$197.54", String.valueOf(
                                retrieve(DataExtractorsUi
                                        .responseBodyExtraction("?v-r=uidl",
                                                "$[0].changes[?(@.key=='totalPrice')].value"), String.class)));})*/


                .validate(() -> assertEquals(List.of("$197.54"),
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='totalPrice')].value"), List.class)))

                /*.validate(() -> assertEquals("$197.54",
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='totalPrice')].value"), String.class)))*/

                /*.validate(() -> assertEquals(170,
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='totalPrice')].value"), Integer.class)))

                .validate(() -> assertEquals(Arrays.asList("John", "Dave"),
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='customer')].value"), List.class)))

                .validate(() -> assertEquals(Arrays.asList(15, 17),
                        retrieve(DataExtractorsUi
                                .responseBodyExtraction("?v-r=uidl",
                                        "$[0].changes[?(@.key=='age')].value"), List.class)))*/

                .complete();
    }


    @Test
    @Description("Insertion usage")
    public void scenario_fifteen(Quest quest,
                                 @Craft(model = VALID_SELLER) Seller seller,
                                 @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .loginUser2(seller)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }

    @Test()
    @Description("Insertion usage")
    @InterceptRequests(requestUrlSubStrings = {"?v-r=uidl"})
    public void scenario_sixteen(Quest quest,
                                 @Craft(model = VALID_SELLER) Seller seller,
                                 @Craft(model = VALID_ORDER) Order order,
                                 @Craft(model = VALID_LATE_ORDER) Late<Order> lateOrder) {
        quest
                .enters(FORGE)
                .loginUser2(seller)
                .createOrder(order)
                .validateOrder(order)
                .then()
                .enters(EARTH)
                .button().click(ButtonFields.CLEAR_SEARCH)
                .then()
                .enters(FORGE)
                .createOrder(lateOrder.join())
                .complete();
    }

}
