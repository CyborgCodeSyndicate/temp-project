package com.bakery.project;


import com.bakery.project.base.World;
import com.bakery.project.ui.authentication.AdminUi;
import com.bakery.project.ui.authentication.BakeryUiLogging;
import com.bakery.project.ui.elements.bakery.InputFields;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.UI;
import org.junit.jupiter.api.Test;

import static com.bakery.project.ui.elements.bakery.CheckboxFields.PAST_ORDERS_CHECKBOX;

@UI
public class BakerySessionReUsageTest extends BaseTest {


   @Test
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
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
   @AuthenticateViaUiAs(credentials = AdminUi.class, type = BakeryUiLogging.class, cacheCredentials = true)
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
