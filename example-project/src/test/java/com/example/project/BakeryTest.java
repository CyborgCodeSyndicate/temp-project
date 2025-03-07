package com.example.project;


import com.example.project.base.World;
import com.example.project.ui.elements.Bakery.ButtonFields;
import com.example.project.ui.elements.Bakery.CheckboxFields;
import com.example.project.ui.elements.Bakery.InputFields;
import com.example.project.ui.elements.Bakery.SelectFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

@UI
public class BakeryTest extends BaseTest {


    @Test
    @Description("COMPONENTS: Input, Button, Select")
    public void scenario_one(Quest quest) {
        quest
            .enters(World.EARTH)
            .browser().navigate("https://bakery-flow.demo.vaadin.com/")
            .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
            .input().insert(InputFields.PASSWORD_FIELD, "barista")
            .button().validateIsEnabled(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .button().click(ButtonFields.NEW_ORDER_BUTTON)
            .select().validateAvailableOptions(SelectFields.LOCATION_DDL, "Store", "Bakery")
            .select().selectOption(SelectFields.LOCATION_DDL, "Store")
            .select().validateSelectedOptions(SelectFields.LOCATION_DDL, "Store")
            .select().selectOptions(SelectFields.LOCATION_DDL, Strategy.LAST)
            .select().validateSelectedOptions(SelectFields.LOCATION_DDL, "Bakery")
            .select().validateAvailableOptions(SelectFields.LOCATION_DDL, "Store", "Bakery")
            .complete();
    }


    @Test
    public void scenario_two(Quest quest) {
        quest
            .enters(World.EARTH)
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
            .select().selectOption(SelectFields.LOCATION_DDL, "Bakery")
            .button().click(ButtonFields.REVIEW_ORDER_BUTTON)
            .button().validateIsEnabled(ButtonFields.PLACE_ORDER_BUTTON)
            .button().click(ButtonFields.PLACE_ORDER_BUTTON)
            .complete();
    }


    @Test
    public void scenario_three(Quest quest) {
        quest
            .enters(World.EARTH)
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
            .complete();
    }


    @Test
    public void scenario_four(Quest quest) {
        quest
            .enters(World.EARTH)
            .browser().navigate("https://bakery-flow.demo.vaadin.com/")
            .input().insert(InputFields.USERNAME_FIELD, "barista@vaadin.com")
            .input().insert(InputFields.PASSWORD_FIELD, "barista")
            .button().click(ButtonFields.SIGN_IN_BUTTON)
            .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
            .checkbox().validateIsEnabled(CheckboxFields.PAST_ORDERS_CHECKBOX)
            .checkbox().select(CheckboxFields.PAST_ORDERS_CHECKBOX)
            .checkbox().validateIsSelected(CheckboxFields.PAST_ORDERS_CHECKBOX)
            .complete();
    }

}
