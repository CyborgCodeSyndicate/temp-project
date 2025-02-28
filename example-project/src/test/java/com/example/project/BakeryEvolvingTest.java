package com.example.project;


import com.example.project.data.test.TestData;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.ui.authentication.AdminUI;
import com.example.project.ui.authentication.BakeryUILogging;
import com.theairebellion.zeus.framework.annotation.Craft;
import com.theairebellion.zeus.framework.annotation.Journey;
import com.theairebellion.zeus.framework.annotation.JourneyData;
import com.theairebellion.zeus.framework.annotation.PreQuest;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.List;

import static com.example.project.base.World.EARTH;
import static com.example.project.base.World.FORGE;
import static com.example.project.data.creator.TestDataCreator.Data.*;
import static com.example.project.preconditions.BakeryQuestPreconditions.Data.LOGIN_PRECONDITION;
import static com.example.project.preconditions.BakeryQuestPreconditions.Data.ORDER_PRECONDITION;
import static com.example.project.ui.elements.Bakery.ButtonFields.*;
import static com.example.project.ui.elements.Bakery.InputFields.*;
import static com.example.project.ui.elements.Bakery.SelectFields.LOCATION_DDL;
import static com.example.project.ui.elements.Bakery.SelectFields.PRODUCTS_DDL;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UI
public class BakeryEvolvingTest extends BaseTest {


    @Test
    @Description("Raw usage")
    public void createOrderRaw(Quest quest) {
        quest
                .enters(EARTH)
                .browser().navigate("https://bakery-flow.demo.vaadin.com/")
                .input().insert(USERNAME_FIELD, "barista@vaadin.com")
                .input().insert(PASSWORD_FIELD, "barista")
                .button().click(SIGN_IN_BUTTON)
                .button().click(NEW_ORDER_BUTTON)
                .input().insert(CUSTOMER_FIELD, "John Terry")
                .input().insert(DETAILS_FIELD, "Address")
                .input().insert(NUMBER_FIELD, "+1-555-7777")
                .select().selectOption(LOCATION_DDL, "Store")
                .select().selectOptions(PRODUCTS_DDL, Strategy.FIRST)
                .button().click(REVIEW_ORDER_BUTTON)
                .button().click(PLACE_ORDER_BUTTON)
                .input().insert(SEARCH_BAR_FIELD, "John Terry")
                .validate(() -> {
                    SuperQuest superQuest = QuestHolder.get();
                    List<SmartWebElement> elements = superQuest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase("John Terry")));
                })
                .complete();
    }


    @Test
    @Description("Raw with Data usage")
    public void createOrderRawData(Quest quest) {
        final TestData testData = ConfigCache.getOrCreate(TestData.class);

        Seller seller = Seller.builder()
                .email(testData.sellerEmail())
                .password(testData.sellerPassword())
                .build();

        Order order = Order.builder()
                .customerName(testData.customerName())
                .customerDetails(testData.customerDetails())
                .phoneNumber(testData.phoneNumber())
                .location(testData.location())
                .product(testData.product())
                .build();

        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(USERNAME_FIELD, seller.getEmail())
                .input().insert(PASSWORD_FIELD, seller.getPassword())
                .button().click(SIGN_IN_BUTTON)
                .button().click(NEW_ORDER_BUTTON)
                .input().insert(CUSTOMER_FIELD, order.getCustomerName())
                .input().insert(DETAILS_FIELD, order.getCustomerDetails())
                .input().insert(NUMBER_FIELD, order.getPhoneNumber())
                .select().selectOption(LOCATION_DDL, order.getLocation())
                .select().selectOptions(PRODUCTS_DDL, order.getProduct())
                .button().click(REVIEW_ORDER_BUTTON)
                .button().click(PLACE_ORDER_BUTTON)
                .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
                .validate(() -> {
                    SuperQuest superQuest = QuestHolder.get();
                    List<SmartWebElement> elements = superQuest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
                })
                .complete();
    }


    @Test
    @Description("Craft usage")
    public void createOrderCraft(Quest quest,
                                 @Craft(model = VALID_SELLER) Seller seller,
                                 @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(USERNAME_FIELD, seller.getEmail())
                .input().insert(PASSWORD_FIELD, seller.getPassword())
                .button().click(SIGN_IN_BUTTON)
                .button().click(NEW_ORDER_BUTTON)
                .input().insert(CUSTOMER_FIELD, order.getCustomerName())
                .input().insert(DETAILS_FIELD, order.getCustomerDetails())
                .input().insert(NUMBER_FIELD, order.getPhoneNumber())
                .select().selectOption(LOCATION_DDL, order.getLocation())
                .select().selectOptions(PRODUCTS_DDL, order.getProduct())
                .button().click(REVIEW_ORDER_BUTTON)
                .button().click(PLACE_ORDER_BUTTON)
                .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
                .validate(() -> {
                    SuperQuest superQuest = QuestHolder.get();
                    List<SmartWebElement> elements = superQuest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
                })
                .complete();
    }


    @Test
    @Description("Insertion and Craft usage")
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
                .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
                .validate(() -> {
                    SuperQuest superQuest = QuestHolder.get();
                    List<SmartWebElement> elements = superQuest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
                })
                .complete();
    }


    @Test
    @Description("Service and Craft usage")
    public void createOrderService(Quest quest,
                                     @Craft(model = VALID_SELLER) Seller seller,
                                     @Craft(model = VALID_ORDER) Order order) {
        quest
                .enters(FORGE)
                .loginUser(seller)
                .createOrder(order)
                .validateOrder(order)
                .complete();
    }


    @Test
    @Description("Authentication, Craft and Service usage")
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
    @Description("PreQuest, Craft and Service usage")
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

}
