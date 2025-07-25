package com.bakery.project.service;

import com.bakery.project.model.bakery.Order;
import com.bakery.project.model.bakery.Seller;
import com.bakery.project.ui.elements.bakery.ButtonFields;
import com.bakery.project.ui.elements.bakery.InputFields;
import com.bakery.project.ui.elements.bakery.LinkFields;
import com.bakery.project.ui.elements.bakery.SelectFields;
import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.bakery.project.base.World.EARTH;
import static com.bakery.project.ui.elements.bakery.ButtonFields.SIGN_IN_BUTTON;
import static com.bakery.project.ui.elements.bakery.InputFields.PASSWORD_FIELD;
import static com.bakery.project.ui.elements.bakery.InputFields.SEARCH_BAR_FIELD;
import static com.bakery.project.ui.elements.bakery.InputFields.USERNAME_FIELD;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestService("Custom")
public class CustomService extends FluentService {


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

   public static String getJsessionCookie() {
      return Objects.requireNonNull(
            QuestHolder.get().artifact(EARTH, SmartWebDriver.class)
                  .getOriginal()
                  .manage()
                  .getCookieNamed("JSESSIONID"),
            "JSESSIONID cookie not found!"
      ).toString();
   }
}
