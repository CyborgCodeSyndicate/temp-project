package com.example.project.service;

import com.example.project.data.creator.TestDataCreator;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.model.Student;
import com.example.project.ui.elements.Bakery.ButtonFields;
import com.example.project.ui.elements.Bakery.InputFields;
import com.example.project.ui.elements.Bakery.LinkFields;
import com.example.project.ui.elements.Bakery.SelectFields;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static com.example.project.base.World.EARTH;
import static com.example.project.ui.elements.Bakery.ButtonFields.NEW_ORDER_BUTTON;
import static com.example.project.ui.elements.Bakery.ButtonFields.SIGN_IN_BUTTON;
import static com.example.project.ui.elements.Bakery.CheckboxFields.PAST_ORDERS_CHECKBOX;
import static com.example.project.ui.elements.Bakery.InputFields.*;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestService("Custom")
public class CustomService extends FluentService {


    public CustomService somethingCustom(Student student) {
        quest.getStorage().sub(StorageKeysTest.ARGUMENTS).getAllByClass(TestDataCreator.VALID_STUDENT, Student.class);
        SmartWebDriver artifact = quest.artifact(EARTH, SmartWebDriver.class);

        return this;
    }


    public CustomService loginUser(String username, String password) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(USERNAME_FIELD, username)
                .input().insert(PASSWORD_FIELD, password)
                .button().click(SIGN_IN_BUTTON)
                //.button().validateIsVisible(NEW_ORDER_BUTTON);
                .input().validateIsEnabled(SEARCH_BAR_FIELD);
        return this;
    }


    public CustomService loginUser(Seller seller) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .input().insert(USERNAME_FIELD, seller.getEmail())
                .input().insert(PASSWORD_FIELD, seller.getPassword())
                .button().click(SIGN_IN_BUTTON)
                //.button().validateIsVisible(NEW_ORDER_BUTTON);
                .input().validateIsEnabled(SEARCH_BAR_FIELD);
        return this;
    }


    public CustomService loginUser2(Seller seller) {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .insertion().insertData(seller)
                .button().click(SIGN_IN_BUTTON)
                //.button().validateIsVisible(NEW_ORDER_BUTTON);
                .input().validateIsEnabled(SEARCH_BAR_FIELD);
        return this;
    }


    public CustomService logoutUser() {
        quest
                .enters(EARTH)
                .browser().navigate(getUiConfig().baseUrl())
                .link().click(LinkFields.LOGOUT_LINK)
                .validate(() -> quest.artifact(EARTH, SmartWebDriver.class).getWait()
                        .until(ExpectedConditions.presenceOfElementLocated(By.tagName("vaadin-login-overlay"))));
        return this;
    }

    public CustomService createOrder() {
        quest
                .enters(EARTH)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .input().insert(InputFields.CUSTOMER_FIELD, "John Terry")
                .input().validateValue(InputFields.CUSTOMER_FIELD, "John Terry")
                .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
                .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, "Strawberry Bun")
                .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
                .input().insert(InputFields.NUMBER_FIELD, "+1-555-7777")
                .input().insert(InputFields.DETAILS_FIELD, "Address")
                .select().selectOption(SelectFields.LOCATION_DDL, "Bakery")
                .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
                .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
                .button().click(ButtonFields.PLACE_ORDER_BUTTON);
        return this;
    }

    /*public CustomService createOrder(Order order) {
        quest
                .enters(EARTH)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .input().insert(InputFields.CUSTOMER_FIELD, order.getCustomerName())
                .input().validateValue(InputFields.CUSTOMER_FIELD, order.getCustomerName())
                .select().selectOptions(SelectFields.PRODUCTS_DDL, Strategy.FIRST)
                .select().validateSelectedOptions(SelectFields.PRODUCTS_DDL, order.getProduct())
                .select().validateAvailableOptions(SelectFields.PRODUCTS_DDL, 12)
                .input().insert(InputFields.NUMBER_FIELD, order.getPhoneNumber())
                .input().insert(InputFields.DETAILS_FIELD, order.getCustomerDetails())
                .select().selectOption(SelectFields.LOCATION_DDL, order.getLocation())
                .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
                .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
                .button().click(ButtonFields.PLACE_ORDER_BUTTON);
        return this;
    }*/

    //Insertion
    public CustomService createOrder(Order order) {
        quest
                .enters(EARTH)
                .button().click(ButtonFields.NEW_ORDER_BUTTON)
                .insertion().insertData(order)
                .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
                /*.validate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })*/
                .button().click(ButtonFields.PLACE_ORDER_BUTTON);
        return this;
    }

    public CustomService validateOrder(String customer) {
        quest
                .enters(EARTH)
                .validate(() -> {
                    List<SmartWebElement> elements = quest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(customer)));
                });
        return this;
    }

    public CustomService validateOrder(Order order) {
        quest
                .enters(EARTH)
                .input().insert(SEARCH_BAR_FIELD, order.getCustomerName())
                .validate(() -> {
                    List<SmartWebElement> elements = quest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    assertTrue(elements.stream().anyMatch(e -> e.getText().equalsIgnoreCase(order.getCustomerName())));
                })
                .button().click(ButtonFields.CLEAR_SEARCH);
        return this;
    }

    public CustomService editOrder(String customer) {
        quest
                .enters(EARTH)
                .input().insert(SEARCH_BAR_FIELD, customer)
                .validate(() -> {
                    /*try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }*/
                    List<SmartWebElement> elements = quest.artifact(EARTH, SmartWebDriver.class)
                            .findSmartElements(By.cssSelector("h3[class='name']"));
                    elements.stream().filter(e -> e.getText().equalsIgnoreCase(customer)).findFirst().get().click();
                })
                .button().click(ButtonFields.CANCEL_ORDER_BUTTON);
        return this;
    }
}
