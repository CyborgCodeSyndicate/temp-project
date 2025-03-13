package com.bakery.project;


import com.bakery.project.base.World;
import com.bakery.project.ui.authentication.AdminUI;
import com.bakery.project.ui.authentication.BakeryUILogging;
import com.bakery.project.ui.elements.Bakery.InputFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.UI;
import org.junit.jupiter.api.Test;

import static com.bakery.project.ui.elements.Bakery.CheckboxFields.PAST_ORDERS_CHECKBOX;

@UI
public class BakerySessionReUsageTest extends BaseTest {


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
