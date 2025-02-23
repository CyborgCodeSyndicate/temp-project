package com.example.project;


import com.example.project.base.World;
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
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.project.data.creator.TestDataCreator.Data.VALID_ORDER;
import static com.example.project.ui.elements.Bakery.CheckboxFields.PAST_ORDERS_CHECKBOX;
import static com.example.project.ui.elements.Bakery.SelectFields.LOCATION_DDL;
import static com.theairebellion.zeus.framework.storage.StorageKeysTest.PRE_ARGUMENTS;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

@UI
public class BakeryLoginTest extends BaseTest {


    @Test
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
    public void scenario_four(Quest quest) throws InterruptedException {
        quest
                .enters(World.EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
                .checkbox().select(PAST_ORDERS_CHECKBOX)
                .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
                .complete();

    }

    @Test
    @AuthenticateViaUiAs(credentials = AdminUI.class, type = BakeryUILogging.class, cacheCredentials = true)
    public void scenario_five(Quest quest) throws InterruptedException {
        quest
                .enters(World.EARTH)
                .input().insert(InputFields.SEARCH_BAR_FIELD, "Amanda Nixon")
                .checkbox().validateIsEnabled(PAST_ORDERS_CHECKBOX)
                .checkbox().select(PAST_ORDERS_CHECKBOX)
                .checkbox().validateIsSelected(PAST_ORDERS_CHECKBOX)
                .complete();

    }

}
